/*
 * Copyright 2024 SOUP, Ji Sungbin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package land.sungbin.navermap.runtime.modifier

import androidx.collection.MutableIntObjectMap
import androidx.collection.mutableIntObjectMapOf
import androidx.compose.runtime.collection.MutableVector
import androidx.compose.runtime.collection.mutableVectorOf
import androidx.compose.ui.util.fastForEach
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors
import land.sungbin.navermap.runtime.contributor.MapViewContributor
import land.sungbin.navermap.runtime.contributor.NaverMapContributor
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.contributor.contains

private typealias ContributorMap = MutableIntObjectMap<MutableVector<Contributor>>

private typealias ContributionNode = MapModifierContributionNode
private typealias ContributionNodeDirtyMap = MutableIntObjectMap<MutableVector<FlaggedContributionNode>>

private typealias DirtyLevel = Int

private class FlaggedContributionNode(
  var cleanNode: ContributionNode? = null,
  var dirtyLevel: DirtyLevel? = null,
  var dirtyNode: ContributionNode? = null,
) {
  init {
    validate()
  }

  fun validate() {
    when (dirtyLevel) {
      ActionRemove -> {
        check(cleanNode == null) { "A node flagged as Remove cannot have a clean node." }
      }
      ActionUpdate, ActionReplace -> {
        check(cleanNode != null && dirtyNode != null) {
          "A Node flagged as Update or Replace must have both dirty and clean nodes."
        }
      }
    }
  }
}

internal class MapModifierNodeChain(private val supportKindSet: List<ContributionKind>) {
  private var contributorMap: ContributorMap? = null
  private var contributionNodeMap: ContributionNodeDirtyMap? = null

  inline fun <reified Owner> delegatorOrNull(kind: ContributionKind): Owner? {
    val contributors = contributorMap ?: return null
    return contributors[kind.mask]?.fold<Owner?>(null) { acc, contributor ->
      val delegate: Owner? = when (kind) {
        Contributors.NaverMap -> (contributor as NaverMapContributor).delegateNaverMap as Owner?
        Contributors.MapView -> (contributor as MapViewContributor).delegateMapView as Owner?
        Contributors.Overlay -> (contributor as OverlayContributor<*>).delegateOverlay as Owner?
        else -> null
      }
      if (delegate is Owner) {
        if (acc != null) throw IllegalStateException("[$kind] delegate was provided multiple times.")
        delegate
      } else acc
    }
  }

  /* prepareContributorsFrom -> trimContributors -> (lazy) contributes */
  fun <Owner> contributes(owner: Owner, kind: ContributionKind) {
    val contributors = contributorMap ?: return
    contributors[kind.mask]?.forEach { contributor -> contributor.execute(owner, kind) }
  }

  fun trimContributors() {
    val contributions = contributionNodeMap ?: return
    val contributionsToUpdate = mutableVectorOf<FlaggedContributionNode>()
    val contributionsToReplace = mutableVectorOf<FlaggedContributionNode>()
    val contributionsToRemove = mutableVectorOf<FlaggedContributionNode>()

    contributions.forEachValue { nodes ->
      nodes.forEach { node ->
        when (node.dirtyLevel) {
          ActionUpdate -> contributionsToUpdate.add(node)
          ActionReplace -> contributionsToReplace.add(node)
          ActionRemove -> contributionsToRemove.add(node)
        }
      }
    }

    contributionsToUpdate.forEach { updateNode ->
      val kindSet = updateNode.dirtyNode!!.kindSet
      supportKindSet.fastForEach { kind ->
        if (kind in kindSet) {
          val index = this.contributionNodeMap!![kind.mask]!!.indexOf(updateNode)
          check(index > -1) { "Must be index > 0" }
          val newNode = updateNode.cleanNode!!
          newNode.update(this.contributorMap!![kind.mask]!![index])
          this.contributionNodeMap!![kind.mask]!![index].markClean(newNode)
        }
      }
    }

    contributionsToReplace.forEach { replaceNode ->
      val kindSet = replaceNode.dirtyNode!!.kindSet
      supportKindSet.fastForEach { kind ->
        if (kind in kindSet) {
          val index = this.contributionNodeMap!![kind.mask]!!.indexOf(replaceNode)
          check(index > -1) { "Must be index > 0" }

          val prevNode = replaceNode.dirtyNode!!
          val newNode = replaceNode.cleanNode!!

          prevNode.onDetach(this.contributorMap!![kind.mask]!![index])
          this.contributionNodeMap!![kind.mask]!![index] = FlaggedContributionNode(cleanNode = newNode)
          this.contributorMap!![kind.mask]!![index] = newNode.create()
          newNode.onAttach(this.contributorMap!![kind.mask]!![index])
        }
      }
    }

    contributionsToRemove.forEach { removeNode ->
      val kindSet = removeNode.dirtyNode!!.kindSet
      supportKindSet.fastForEach { kind ->
        if (kind in kindSet) {
          val index = this.contributionNodeMap!![kind.mask]!!.indexOf(removeNode)
          check(index > -1) { "Must be index > 0" }
          removeNode.dirtyNode!!.onDetach(this.contributorMap!![kind.mask]!![index])
          this.contributorMap!![kind.mask]!!.removeAt(index)
          this.contributionNodeMap!![kind.mask]!!.removeAt(index)
        }
      }
    }

    supportKindSet.fastForEach { kind ->
      if (this.contributorMap!![kind.mask]!!.isEmpty()) {
        this.contributorMap!!.remove(kind.mask)
        this.contributionNodeMap!!.remove(kind.mask)
      }
    }

    this.contributorMap!!.trim()
    this.contributionNodeMap!!.trim()
  }

  fun prepareContributorsFrom(modifier: MapModifier) {
    val prevContributionNodeMap = contributionNodeMap
    val newContributionNodeMap = modifier.contributionNodes()

    val newMapSize = newContributionNodeMap?.size ?: 0

    if (prevContributionNodeMap == null && newMapSize == 0) {
      return // Nothing to do (previous and new contributors are empty)
    } else if (prevContributionNodeMap == null && newMapSize > 0) {
      // Initial contributors
      this.contributorMap = mutableIntObjectMapOf()
      newContributionNodeMap!!.forEach { kind, nodes ->
        this.contributorMap!![kind] = MutableVector(nodes.size) { index ->
          nodes[index].cleanNode!!.create()
        }
      }
      this.contributionNodeMap = newContributionNodeMap
    } else { // prevContributionNodes != null
      checkNotNull(this.contributorMap) { "contributorMap expected to be non-null" }
      checkNotNull(prevContributionNodeMap) { "prevContributionNodeMap expected to be non-null" }

      val beforeMapSize = prevContributionNodeMap.size
      check(beforeMapSize > 0) { "beforeMapSize expected more than zero" }

      if (newMapSize == 0) {
        // All contributors have been removed.
        this.contributionNodeMap!!.forEachValue { nodes ->
          nodes.forEach { it.markRemoving() }
        }
      } else if (beforeMapSize == newMapSize) {
        // Reuse nodes as much as possible, if the structure of the contributor hasn't changed.
        newContributionNodeMap!!.forEach { kind, newNodes ->
          val beforeContributionNodeMap = prevContributionNodeMap[kind]!!

          val newSize = newNodes.size
          val beforeSize = beforeContributionNodeMap.size

          if (newSize == beforeSize) {
            // Start optional dirty flagging because the structure of contributors hasn't changed.
            check(newSize > 0) { "contributorNode size expected more than zero" }
            repeat(newSize) { index ->
              val prevNode = prevContributionNodeMap[kind]!![index]
              val newNode = newNodes[index]

              when (val dirty = dirtyForModifiers(prevNode.cleanNode!!, newNode.cleanNode!!)) {
                ActionReuse -> Unit // everything is up-to-date
                ActionUpdate, ActionReplace -> {
                  // [ActionUpdate] Same node, but something changed
                  // [ActionReplace] Different node, replace it
                  this.contributionNodeMap!![kind]!![index].markDirty(
                    level = dirty,
                    dirtyNode = prevNode.cleanNode!!,
                    cleanNode = newNode.cleanNode!!,
                  )
                }
                else -> error("Invalid dirty level: $dirty")
              }
            }
          } else {
            // Since the structure of contributors has changed, it cannot be guaranteed that
            // the environment will be the same as before, even if it is the same node. So
            // rebuild contributors from the ground up.
            repeat(beforeSize) { index ->
              this.contributionNodeMap!![kind]!![index].markRemoving()
            }
            repeat(newSize) { index ->
              this.contributorMap!![kind]!! += newNodes[index].cleanNode!!.create()
              this.contributionNodeMap!![kind]!! += FlaggedContributionNode(cleanNode = newNodes[index].cleanNode!!)
            }
          }
        }
      } else { // newMapSize != 0 && newMapSize != beforeMapSize
        // Since the structure of contributors has changed, it cannot be guaranteed that
        // the environment will be the same as before, even if it is the same node. So
        // rebuild contributors from the ground up.
        newContributionNodeMap!!.forEach { kind, newNodes ->
          // 1. Removing all previous contributors
          this.contributionNodeMap!![kind]!!.forEach { it.markRemoving() }
          // 2. Add new contributors
          newNodes.forEach { newNode ->
            this.contributorMap!![kind]!! += newNode.cleanNode!!.create()
            this.contributionNodeMap!![kind]!! += FlaggedContributionNode(cleanNode = newNode.cleanNode!!)
          }
        }
      }
    }
  }

  @Suppress("UNCHECKED_CAST", "UPPER_BOUND_VIOLATED")
  private fun <Owner> Contributor.execute(owner: Owner, kind: ContributionKind) {
    when (kind) {
      Contributors.NaverMap -> with(this as NaverMapContributor) { (owner as NaverMap).contribute() }
      Contributors.MapView -> with(this as MapViewContributor) { (owner as MapView).contribute() }
      Contributors.Overlay -> with(this as OverlayContributor<Owner>) { owner.contribute() }
      else -> error("Invalid kind: $kind")
    }
  }

  private fun FlaggedContributionNode.markRemoving() {
    dirtyLevel = ActionRemove
    dirtyNode = cleanNode
    cleanNode = null
    validate()
  }

  private fun FlaggedContributionNode.markDirty(level: DirtyLevel, dirtyNode: ContributionNode, cleanNode: ContributionNode) {
    check(level != ActionRemove) { "For dirty flagging with ActionRemove, use markRemoving." }
    dirtyLevel = level
    this.dirtyNode = dirtyNode
    this.cleanNode = cleanNode
    validate()
  }

  private fun FlaggedContributionNode.markClean(cleanNode: ContributionNode) {
    dirtyLevel = null
    this.dirtyNode = null
    this.cleanNode = cleanNode
    validate()
  }

  private fun MapModifier.contributionNodes(): ContributionNodeDirtyMap? {
    if (this === MapModifier) return null
    val destination = mutableIntObjectMapOf<MutableVector<FlaggedContributionNode>>()
    forEachNode { node ->
      val contribution = node as? ContributionNode ?: return@forEachNode
      val kindSet = contribution.kindSet

      supportKindSet.fastForEach { kind ->
        if (kind in kindSet) {
          val nodes = destination.getOrPut(kind.mask) { mutableVectorOf() }
          nodes.add(FlaggedContributionNode(cleanNode = contribution))
        }
      }
    }
    return destination
  }
}

private const val ActionReuse: DirtyLevel = 0
private const val ActionUpdate: DirtyLevel = 1
private const val ActionReplace: DirtyLevel = 2
private const val ActionRemove: DirtyLevel = 3

/**
 * Here's the rules for reusing nodes for different modifiers:
 * 1. if modifiers are equals, we REUSE but NOT UPDATE
 * 2. if modifiers are same class, we REUSE and UPDATE
 * 3. else REPLACE (NO REUSE, NO UPDATE)
 */
private fun dirtyForModifiers(prev: MapModifierNode<*>, next: MapModifierNode<*>) =
  if (prev == next) {
    ActionReuse
  } else if (prev::class.java === next::class.java) {
    ActionUpdate
  } else {
    ActionReplace
  }
