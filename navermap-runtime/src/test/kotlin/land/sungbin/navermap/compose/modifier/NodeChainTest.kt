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

package land.sungbin.navermap.compose.modifier

import assertk.Assert
import assertk.all
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.doesNotContainKey
import assertk.assertions.hasMessage
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.key
import assertk.assertions.single
import assertk.fail
import io.mockk.mockk
import io.mockk.verifyAll
import land.sungbin.navermap.compose.assertion.containsInstanceExactly
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors
import land.sungbin.navermap.runtime.contributor.EmptyContributor
import land.sungbin.navermap.runtime.modifier.ActionRemove
import land.sungbin.navermap.runtime.modifier.ActionReplace
import land.sungbin.navermap.runtime.modifier.ActionReuse
import land.sungbin.navermap.runtime.modifier.ActionUpdate
import land.sungbin.navermap.runtime.modifier.ContributionNode
import land.sungbin.navermap.runtime.modifier.FlaggedContributionNode
import land.sungbin.navermap.runtime.modifier.MapModifier
import land.sungbin.navermap.runtime.modifier.MapModifierNodeChain
import land.sungbin.navermap.runtime.modifier.asReadOnlyMap
import land.sungbin.navermap.runtime.modifier.dirtyForModifiers
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.Test

// NOTE: APIs such as containsOnly that check whether a particular element
//  is included are built into assertk, but I don't use them due to differences
//  in how assertk implements the check I want.
class NodeChainTest {
  @Test fun dirtyForModifiersReuse() {
    val node = NothingMapModifierNode()
    val dirty = dirtyForModifiers(node, node)
    assertThat(dirty).isEqualTo(ActionReuse)
  }

  @Test fun dirtyForModifiersUpdate() {
    val node = NothingMapModifierNode()
    val node2 = NothingMapModifierNode()
    val dirty = dirtyForModifiers(node, node2)
    assertThat(dirty).isEqualTo(ActionUpdate)
  }

  @Test fun dirtyForModifiersReplace() {
    val node = NothingMapModifierNode()
    val node2 = UnitMapModifierNode()
    val dirty = dirtyForModifiers(node, node2)
    assertThat(dirty).isEqualTo(ActionReplace)
  }

  @Test fun flaggedContributionNodeRemoveValidationFail() {
    val cleanNode = AnyContributionNode()
    val dirtyNode = AnyContributionNode()
    val dirty = ActionRemove

    assertFailure { FlaggedContributionNode(cleanNode, dirty, dirtyNode) }
      .hasMessage("A node flagged as Remove cannot have a clean node.")
  }

  @Test fun flaggedContributionNodeUpdateValidationFail() {
    val cleanNode = AnyContributionNode()
    val dirty = ActionUpdate

    assertFailure { FlaggedContributionNode(cleanNode, dirty, null) }
      .hasMessage("A Node flagged as Update or Replace must have both dirty and clean nodes.")
  }

  @Test fun flaggedContributionNodeReplaceValidationFail() {
    val dirtyNode = AnyContributionNode()
    val dirty = ActionReplace

    assertFailure { FlaggedContributionNode(null, dirty, dirtyNode) }
      .hasMessage("A Node flagged as Update or Replace must have both dirty and clean nodes.")
  }

  @Test fun contributionNodesEmpty() {
    val chain = MapModifierNodeChain(emptyList())
    val nodes = with(chain) { MapModifier.contributionNodes() }.asReadOnlyMap()
    assertThat(nodes).isEmpty()
  }

  @Test fun contributionNodesContainsAllNode() {
    val chain = MapModifierNodeChain(listOf(Contributors.Any, Contributors.MapView, Contributors.NaverMap))

    val anyNode = AnyContributionNode()
    val mapViewNode = MapViewContributionNode()
    val mapViewNode2 = MapViewContributionNode()
    val mapViewNode3 = MapViewContributionNode()
    val naverMapNode = NaverMapContributionNode()
    val naverMapNode2 = NaverMapContributionNode()
    val overlayNode = OverlayContributionNode()
    val modifiers = MapModifier
      .then(anyNode)
      .then(mapViewNode)
      .then(mapViewNode2)
      .then(mapViewNode3)
      .then(naverMapNode)
      .then(naverMapNode2)
      .then(overlayNode)

    val nodeMap = with(chain) { modifiers.contributionNodes() }.asReadOnlyMap()

    assertThat(nodeMap).all {
      doesNotContainKey(Contributors.Overlay.mask)
      key(Contributors.Any.mask).containsExactly(anyNode.asCleanFlagged())
      key(Contributors.MapView.mask).containsExactly(
        mapViewNode.asCleanFlagged(),
        mapViewNode2.asCleanFlagged(),
        mapViewNode3.asCleanFlagged(),
      )
      key(Contributors.NaverMap.mask).containsExactly(
        naverMapNode.asCleanFlagged(),
        naverMapNode2.asCleanFlagged(),
      )
    }
  }

  @Test fun prepareContributorsFromNothingToDoState() {
    val chain = MapModifierNodeChain(listOf(Contributors.Any, Contributors.MapView, Contributors.NaverMap))
    chain.prepareContributorsFrom(MapModifier)
    assertThat(chain.testGetContributionNodeMap()).isNull()
  }

  @Test fun prepareContributorsFromInitializingState() {
    val chain = MapModifierNodeChain(listOf(Contributors.Any, Contributors.MapView, Contributors.NaverMap))

    val anyNode = AnyContributionNode()
    val mapViewNode = MapViewContributionNode()
    val mapViewNode2 = MapViewContributionNode()
    val mapViewNode3 = MapViewContributionNode()
    val naverMapNode = NaverMapContributionNode()
    val naverMapNode2 = NaverMapContributionNode()
    val overlayNode = OverlayContributionNode()
    val mapViewAndNaverMapNode = MapViewAndNaverMapContributionNode()

    val initializingModifier = MapModifier
      .then(anyNode)
      .then(mapViewNode)
      .then(mapViewNode2)
      .then(mapViewNode3)
      .then(naverMapNode)
      .then(naverMapNode2)
      .then(overlayNode)
      .then(mapViewAndNaverMapNode)

    chain.prepareContributorsFrom(initializingModifier)

    val contributorMap = chain.testGetContributorMap()

    assertThat(contributorMap)
      .isNotNull()
      .all {
        doesNotContainKey(Contributors.Overlay.mask)
        key(Contributors.Any.mask).containsInstanceExactly(anyNode.contributor)
        key(Contributors.MapView.mask).containsInstanceExactly(
          mapViewNode.contributor,
          mapViewNode2.contributor,
          mapViewNode3.contributor,
          mapViewAndNaverMapNode.contributor,
        )
        key(Contributors.NaverMap.mask).containsInstanceExactly(
          naverMapNode.contributor,
          naverMapNode2.contributor,
          mapViewAndNaverMapNode.contributor,
        )
      }

    val contributionNodeMap = chain.testGetContributionNodeMap()

    assertThat(contributionNodeMap)
      .isNotNull()
      .all {
        doesNotContainKey(Contributors.Overlay.mask)
        key(Contributors.Any.mask).containsExactly(anyNode.asCleanFlagged())
        key(Contributors.MapView.mask).containsExactly(
          mapViewNode.asCleanFlagged(),
          mapViewNode2.asCleanFlagged(),
          mapViewNode3.asCleanFlagged(),
          mapViewAndNaverMapNode.asCleanFlagged(),
        )
        key(Contributors.NaverMap.mask).containsExactly(
          naverMapNode.asCleanFlagged(),
          naverMapNode2.asCleanFlagged(),
          mapViewAndNaverMapNode.asCleanFlagged(),
        )
      }
  }

  @Test fun prepareContributorsFromRemoveAllState() {
    val chain = MapModifierNodeChain(listOf(Contributors.Any, Contributors.MapView, Contributors.NaverMap))

    val anyNode = AnyContributionNode()
    val mapViewNode = MapViewContributionNode()
    val mapViewNode2 = MapViewContributionNode()
    val mapViewNode3 = MapViewContributionNode()
    val naverMapNode = NaverMapContributionNode()
    val naverMapNode2 = NaverMapContributionNode()
    val mapViewAndNaverMapNode = MapViewAndNaverMapContributionNode()

    val initializingModifier = MapModifier
      .then(anyNode)
      .then(mapViewNode)
      .then(mapViewNode2)
      .then(mapViewNode3)
      .then(naverMapNode)
      .then(naverMapNode2)
      .then(mapViewAndNaverMapNode)

    chain.prepareContributorsFrom(initializingModifier)
    chain.prepareContributorsFrom(MapModifier)

    val contributionNodeMap = chain.testGetContributionNodeMap()

    assertThat(contributionNodeMap)
      .isNotNull()
      .transform { nodeMap -> nodeMap.values.flatten() }
      .isAllRemoving()
  }

  @Test fun prepareContributorsFromActionReuseState() {
    val chain = MapModifierNodeChain(listOf(Contributors.Any, Contributors.MapView, Contributors.NaverMap))

    val anyNode = AnyContributionNode()
    val mapViewNode = MapViewContributionNode()
    val mapViewNode2 = MapViewContributionNode()
    val mapViewNode3 = MapViewContributionNode()
    val naverMapNode = NaverMapContributionNode()
    val naverMapNode2 = NaverMapContributionNode()
    val mapViewAndNaverMapNode = MapViewAndNaverMapContributionNode()

    val initializingModifier = MapModifier
      .then(anyNode)
      .then(mapViewNode)
      .then(mapViewNode2)
      .then(mapViewNode3)
      .then(naverMapNode)
      .then(naverMapNode2)
      .then(mapViewAndNaverMapNode)

    chain.prepareContributorsFrom(initializingModifier)

    val initializedContributionNodeMap = chain.testGetContributionNodeMap()
    assertThat(initializedContributionNodeMap).isNotNull()

    chain.prepareContributorsFrom(initializingModifier)

    assertThat(chain.testGetContributionNodeMap()).isEqualTo(initializedContributionNodeMap)
  }

  @ValueSource(ints = [ActionUpdate, ActionReplace])
  @ParameterizedTest
  fun prepareContributorsFromActionUpdateOrActionReplaceState(testDirtyLevel: Int) {
    val chain = MapModifierNodeChain(listOf(Contributors.Any, Contributors.MapView, Contributors.NaverMap))

    val anyNode = AnyContributionNode()
    val mapViewNode = MapViewContributionNode()
    val mapViewNode2 = MapViewContributionNode()
    val mapViewNode3 = MapViewContributionNode()
    val naverMapNode = NaverMapContributionNode()
    val naverMapNode2 = NaverMapContributionNode()
    val mapViewAndNaverMapNode = MapViewAndNaverMapContributionNode()

    val initializingModifier = MapModifier
      .then(anyNode)
      .then(mapViewNode)
      .then(mapViewNode2)
      .then(mapViewNode3)
      .then(naverMapNode)
      .then(naverMapNode2)
      .then(mapViewAndNaverMapNode)

    chain.prepareContributorsFrom(initializingModifier)

    val newNaverMapNode = run {
      if (testDirtyLevel == ActionUpdate) NaverMapContributionNode()
      else object : TestBaseContributionNode() {
        override val kindSet = Contributors.NaverMap
        override val contributor = object : EmptyContributor {}
        override val attacher: OnAttach? = null
        override val detacher: OnDetach? = null
        override val createrEffect: OnCreateSideEffect? = null
        override val updater: OnUpdate? = null
      }
    }
    val newMapViewAndNaverMapNode = run {
      if (testDirtyLevel == ActionUpdate) MapViewAndNaverMapContributionNode()
      else object : TestBaseContributionNode() {
        override val kindSet = Contributors.MapView + Contributors.NaverMap
        override val contributor = object : EmptyContributor {}
        override val attacher: OnAttach? = null
        override val detacher: OnDetach? = null
        override val createrEffect: OnCreateSideEffect? = null
        override val updater: OnUpdate? = null
      }
    }

    val updatingModifier = MapModifier
      .then(anyNode)
      .then(mapViewNode)
      .then(mapViewNode2)
      .then(mapViewNode3)
      .then(newNaverMapNode)
      .then(naverMapNode2)
      .then(newMapViewAndNaverMapNode)

    chain.prepareContributorsFrom(updatingModifier)

    val updatedContributionNodeMap = chain.testGetContributionNodeMap()

    assertThat(updatedContributionNodeMap)
      .isNotNull()
      .given { nodeMap ->
        assertThat(nodeMap[Contributors.Any.mask]).isNotNull().single().isClean()

        checkNotNull(nodeMap[Contributors.MapView.mask]).forEachIndexed { index, it ->
          if (index != 3) assertThat(it).isClean()

          if (index == 3) {
            if (it.dirtyLevel != testDirtyLevel)
              fail("Expected dirtyLevel=$testDirtyLevel, but got ${it.dirtyLevel}", actual = it)

            if (it.dirtyNode != mapViewAndNaverMapNode || it.cleanNode != newMapViewAndNaverMapNode)
              fail(
                "Expected dirtyNode=$mapViewAndNaverMapNode, cleanNode=$newMapViewAndNaverMapNode, but got dirtyNode=${it.dirtyNode}, cleanNode=${it.cleanNode}",
                actual = it,
              )
          }
        }

        checkNotNull(nodeMap[Contributors.NaverMap.mask]).forEachIndexed { index, it ->
          if (index == 1) assertThat(it).isClean()
          else {
            val prevNode = if (index == 0) naverMapNode else mapViewAndNaverMapNode
            val newNode = if (index == 0) newNaverMapNode else newMapViewAndNaverMapNode

            if (it.dirtyLevel != testDirtyLevel)
              fail("Expected dirtyLevel=$testDirtyLevel, but got ${it.dirtyLevel}.", actual = it)

            if (it.dirtyNode != prevNode || it.cleanNode != newNode)
              fail(
                "Expected dirtyNode=$prevNode, cleanNode=$newNode, but got dirtyNode=${it.dirtyNode}, cleanNode=${it.cleanNode}",
                actual = it,
              )
          }
        }
      }
  }

  @Test fun prepareContributorsFromContributorStructureChangingState() {
    val chain = MapModifierNodeChain(
      listOf(
        Contributors.Any,
        Contributors.MapView,
        Contributors.NaverMap,
        Contributors.Overlay,
      ),
    )

    val anyNode = AnyContributionNode()
    val mapViewNode = MapViewContributionNode()
    val mapViewNode2 = MapViewContributionNode()
    val mapViewNode3 = MapViewContributionNode()
    val naverMapNode = NaverMapContributionNode()
    val naverMapNode2 = NaverMapContributionNode()
    val mapViewAndNaverMapNode = MapViewAndNaverMapContributionNode()
    val overlayNode = OverlayContributionNode()

    val initializingModifier = MapModifier
      .then(anyNode)
      .then(mapViewNode)
      .then(mapViewNode2)
      .then(mapViewNode3)
      .then(naverMapNode)
      .then(naverMapNode2)
      .then(mapViewAndNaverMapNode)
      .then(overlayNode)

    chain.prepareContributorsFrom(initializingModifier)

    val newOverlayNode = OverlayContributionNode()

    val changingModifier = MapModifier
      .then(anyNode)
      .then(mapViewNode)
      .then(mapViewNode2)
      .then(mapViewNode3)
      .then(newOverlayNode) // [changed] naverMapNode -> newOverlayNode
      .then(naverMapNode2)
      .then(mapViewAndNaverMapNode)
      .then(overlayNode)

    chain.prepareContributorsFrom(changingModifier)

    val changedContributors = chain.testGetContributorMap()

    assertThat(changedContributors)
      .isNotNull()
      .key(Contributors.Overlay.mask)
      // TIP: The point at which duplicate instances are cleaned up is trim()
      .containsInstanceExactly(
        overlayNode.contributor,
        newOverlayNode.contributor,
        overlayNode.contributor,
      )

    val changedContributionNodeMap = chain.testGetContributionNodeMap()

    assertThat(changedContributionNodeMap)
      .isNotNull()
      .given { nodeMap ->
        assertThat(nodeMap[Contributors.Any.mask]).isNotNull().isAllClean()
        assertThat(nodeMap[Contributors.MapView.mask]).isNotNull().isAllClean()

        val naverMapNodes = checkNotNull(nodeMap[Contributors.NaverMap.mask])
        assertThat(naverMapNodes.slice(0..2)).isAllRemoving() // previous nodes
        assertThat(naverMapNodes.slice(3..4)).isAllClean() // new node

        assertThat(nodeMap[Contributors.Overlay.mask])
          .isNotNull()
          .containsExactly(
            overlayNode.asRemoveFlagged(),
            newOverlayNode.asCleanFlagged(),
            overlayNode.asCleanFlagged(),
          )
      }
  }

  @Test fun prepareContributorsFromContributionNodeMapStructureSizeChangingState() {
    val chain = MapModifierNodeChain(
      listOf(
        Contributors.Any,
        Contributors.MapView,
        Contributors.NaverMap,
        Contributors.Overlay,
      ),
    )

    val anyNode = AnyContributionNode()
    val mapViewNode = MapViewContributionNode()
    val mapViewNode2 = MapViewContributionNode()
    val mapViewNode3 = MapViewContributionNode()
    val naverMapNode = NaverMapContributionNode()
    val naverMapNode2 = NaverMapContributionNode()
    val mapViewAndNaverMapNode = MapViewAndNaverMapContributionNode()

    val initializingModifier = MapModifier
      .then(anyNode)
      .then(mapViewNode)
      .then(mapViewNode2)
      .then(mapViewNode3)
      .then(naverMapNode)
      .then(naverMapNode2)
      .then(mapViewAndNaverMapNode)

    chain.prepareContributorsFrom(initializingModifier)

    val newOverlayNode = OverlayContributionNode()
    val newOverlayNode2 = OverlayContributionNode()

    val changingModifier = MapModifier
      .then(newOverlayNode)
      .then(newOverlayNode2)

    chain.prepareContributorsFrom(changingModifier)

    val changedContributors = chain.testGetContributorMap()

    assertThat(changedContributors)
      .isNotNull()
      .key(Contributors.Overlay.mask)
      .containsInstanceExactly(
        newOverlayNode.contributor,
        newOverlayNode2.contributor,
      )

    val changedContributionNodeMap = chain.testGetContributionNodeMap()

    assertThat(changedContributionNodeMap)
      .isNotNull()
      .given { nodeMap ->
        assertThat(nodeMap[Contributors.Any.mask]).isNotNull().isAllRemoving()
        assertThat(nodeMap[Contributors.MapView.mask]).isNotNull().isAllRemoving()
        assertThat(nodeMap[Contributors.NaverMap.mask]).isNotNull().isAllRemoving()
        assertThat(nodeMap[Contributors.Overlay.mask])
          .isNotNull()
          .containsExactly(
            newOverlayNode.asCleanFlagged(),
            newOverlayNode2.asCleanFlagged(),
          )
      }
  }

  @Test fun prepareContributorsFromContributionNodeMapStructureKindSetChangingState() {
    val chain = MapModifierNodeChain(
      listOf(
        Contributors.Any,
        Contributors.MapView,
        Contributors.NaverMap,
        Contributors.Overlay,
      ),
    )

    val anyNode = AnyContributionNode()
    val mapViewNode = MapViewContributionNode()
    val mapViewNode2 = MapViewContributionNode()
    val mapViewNode3 = MapViewContributionNode()
    val naverMapNode = NaverMapContributionNode()
    val naverMapNode2 = NaverMapContributionNode()

    val initializingModifier = MapModifier
      .then(anyNode)
      .then(mapViewNode)
      .then(mapViewNode2)
      .then(mapViewNode3)
      .then(naverMapNode)
      .then(naverMapNode2)

    chain.prepareContributorsFrom(initializingModifier)

    val newOverlayNode = OverlayContributionNode()

    val changingModifier = MapModifier
      .then(anyNode)
      .then(mapViewNode)
      .then(mapViewNode2)
      .then(mapViewNode3)
      .then(newOverlayNode) // naverMapNode -> newOverlayNode

    chain.prepareContributorsFrom(changingModifier)

    val changedContributors = chain.testGetContributorMap()

    assertThat(changedContributors)
      .isNotNull()
      .key(Contributors.Overlay.mask)
      .containsInstanceExactly(newOverlayNode.contributor)

    val changedContributionNodeMap = chain.testGetContributionNodeMap()

    assertThat(changedContributionNodeMap)
      .isNotNull()
      .given { nodeMap ->
        val anyNodes = checkNotNull(nodeMap[Contributors.Any.mask])
        assertThat(anyNodes[0]).isRemoving()
        assertThat(anyNodes[1]).isClean()

        val mapViewNodes = checkNotNull(nodeMap[Contributors.MapView.mask])
        assertThat(mapViewNodes.slice(0..2)).isAllRemoving()
        assertThat(mapViewNodes.slice(3..5)).isAllClean()

        assertThat(nodeMap[Contributors.NaverMap.mask])
          .isNotNull()
          .isAllRemoving()

        assertThat(nodeMap[Contributors.Overlay.mask])
          .isNotNull()
          .containsExactly(newOverlayNode.asCleanFlagged())
      }
  }

  @Test fun trimContributorsUpdateNodes() {
    val chain = MapModifierNodeChain(listOf(Contributors.MapView, Contributors.NaverMap))

    val updateVerifier = mockk<(Contributor) -> Unit>(relaxed = true)

    val mapViewNode = MapViewContributionNode()
    val mapViewNode2 = MapViewContributionNode { updateVerifier(it) }
    val naverMapNode = NaverMapContributionNode()
    val naverMapNode2 = NaverMapContributionNode { updateVerifier(it) }
    val mapViewAndNaverMapContributionNode = MapViewAndNaverMapContributionNode()
    val mapViewAndNaverMapContributionNode2 = MapViewAndNaverMapContributionNode { updateVerifier(it) }

    val initializingModifier = MapModifier
      .then(mapViewNode)
      .then(naverMapNode)
      .then(mapViewAndNaverMapContributionNode)
    val updatingModifier = MapModifier
      .then(mapViewNode2)
      .then(naverMapNode2)
      .then(mapViewAndNaverMapContributionNode2)

    chain.prepareContributorsFrom(initializingModifier)
    chain.prepareContributorsFrom(updatingModifier)
    chain.trimContributors()

    val trimmedContributors = chain.testGetContributorMap()
    val trimmedContributorNodeMap = chain.testGetContributionNodeMap()

    assertThat(trimmedContributors)
      .isNotNull()
      .all {
        key(Contributors.MapView.mask).containsInstanceExactly(mapViewNode.contributor, mapViewAndNaverMapContributionNode.contributor)
        key(Contributors.NaverMap.mask).containsInstanceExactly(naverMapNode.contributor, mapViewAndNaverMapContributionNode.contributor)
      }
    assertThat(trimmedContributorNodeMap)
      .isNotNull()
      .transform { it.values.flatten() }
      .isAllClean()

    verifyAll {
      updateVerifier(mapViewNode.contributor)
      updateVerifier(naverMapNode.contributor)
      updateVerifier(mapViewAndNaverMapContributionNode.contributor)
      updateVerifier(mapViewAndNaverMapContributionNode.contributor)
    }
  }

  @Test fun trimContributorsReplaceNodes() {
    val chain = MapModifierNodeChain(
      listOf(
        Contributors.Any,
        Contributors.MapView,
        Contributors.NaverMap,
        Contributors.Overlay,
      ),
    )

    val attachVerifier = mockk<(Contributor) -> Unit>(relaxed = true)
    val detachVerifier = mockk<(Contributor) -> Unit>(relaxed = true)
    val createVerifier = mockk<(Contributor) -> Unit>(relaxed = true)
    val updateVerifier = mockk<(Contributor) -> Unit>(relaxed = true)

    val node = AnyContributionNode(
      detacher = { detachVerifier(it) },
      updater = { updateVerifier(it) },
    )
    val node2 = MapViewContributionNode(
      attacher = { attachVerifier(it) },
      createrEffect = { createVerifier(it) },
      updater = { updateVerifier(it) },
    )
    val node3 = NaverMapContributionNode(
      detacher = { detachVerifier(it) },
      updater = { updateVerifier(it) },
    )
    val node4 = OverlayContributionNode(
      attacher = { attachVerifier(it) },
      createrEffect = { createVerifier(it) },
      updater = { updateVerifier(it) },
    )

    val initializingModifier = MapModifier.then(node).then(node2)
    val replacingModifier = MapModifier.then(node3).then(node4)

    chain.prepareContributorsFrom(initializingModifier)
    chain.prepareContributorsFrom(replacingModifier)
    chain.trimContributors()

    val trimmedContributors = chain.testGetContributorMap()
    val trimmedContributorNodeMap = chain.testGetContributionNodeMap()

    assertThat(trimmedContributors)
      .isNotNull()
      .all {
        key(Contributors.NaverMap.mask).containsInstanceExactly(node3.contributor)
        key(Contributors.Overlay.mask).containsInstanceExactly(node4.contributor)
      }
    assertThat(trimmedContributorNodeMap)
      .isNotNull()
      .transform { it.values.flatten() }
      .isAllClean()
  }

  @Test fun trimContributorsDeleteAllRemovedNodes() {
    val chain = MapModifierNodeChain(listOf(Contributors.MapView, Contributors.NaverMap))

    val mapViewNode = MapViewContributionNode()
    val mapViewNode2 = MapViewContributionNode()
    val mapViewNode3 = MapViewContributionNode()
    val naverMapNode = NaverMapContributionNode()
    val naverMapNode2 = NaverMapContributionNode()

    val initializingModifier = MapModifier
      .then(mapViewNode)
      .then(mapViewNode2)
      .then(mapViewNode3)
      .then(naverMapNode)
      .then(naverMapNode2)
    val removingModifier = MapModifier then naverMapNode2

    chain.prepareContributorsFrom(initializingModifier)
    chain.prepareContributorsFrom(removingModifier)
    chain.trimContributors()

    val trimmedContributors = chain.testGetContributorMap()

    assertThat(trimmedContributors)
      .isNotNull()
      .given { maps ->
        assertThat(maps.keys).single().isEqualTo(Contributors.NaverMap.mask)
        assertThat(maps.values.flatten()).single().isEqualTo(naverMapNode2.contributor)
      }
  }

  @Test fun trimContributorsDeleteAllNodes() {
    val chain = MapModifierNodeChain(listOf(Contributors.MapView, Contributors.NaverMap))

    val mapViewNode = MapViewContributionNode()
    val mapViewNode2 = MapViewContributionNode()
    val mapViewNode3 = MapViewContributionNode()
    val naverMapNode = NaverMapContributionNode()
    val naverMapNode2 = NaverMapContributionNode()

    val initializingModifier = MapModifier
      .then(mapViewNode)
      .then(mapViewNode2)
      .then(mapViewNode3)
      .then(naverMapNode)
      .then(naverMapNode2)
    val removingModifier = MapModifier

    chain.prepareContributorsFrom(initializingModifier)
    chain.prepareContributorsFrom(removingModifier)
    chain.trimContributors()

    val trimmedContributors = chain.testGetContributorMap()

    assertThat(trimmedContributors)
      .isNotNull()
      .isEmpty()
  }
}

private fun interface OnCreateSideEffect {
  fun action(contributor: Contributor)
}

private fun interface OnUpdate {
  fun update(contributor: Contributor)
}

private fun interface OnAttach {
  fun attach(instance: Contributor)
}

private fun interface OnDetach {
  fun detach(instance: Contributor)
}

private class AnyContributionNode(
  override val contributor: Contributor = object : EmptyContributor {},
  override val attacher: OnAttach? = null,
  override val detacher: OnDetach? = null,
  override val createrEffect: OnCreateSideEffect? = null,
  override val updater: OnUpdate? = null,
) : TestBaseContributionNode() {
  override val kindSet = Contributors.Any
}

private class MapViewContributionNode(
  override val contributor: Contributor = object : EmptyContributor {},
  override val attacher: OnAttach? = null,
  override val detacher: OnDetach? = null,
  override val createrEffect: OnCreateSideEffect? = null,
  override val updater: OnUpdate? = null,
) : TestBaseContributionNode() {
  override val kindSet = Contributors.MapView
}

private class NaverMapContributionNode(
  override val contributor: Contributor = object : EmptyContributor {},
  override val attacher: OnAttach? = null,
  override val detacher: OnDetach? = null,
  override val createrEffect: OnCreateSideEffect? = null,
  override val updater: OnUpdate? = null,
) : TestBaseContributionNode() {
  override val kindSet = Contributors.NaverMap
}

private class MapViewAndNaverMapContributionNode(
  override val contributor: Contributor = object : EmptyContributor {},
  override val attacher: OnAttach? = null,
  override val detacher: OnDetach? = null,
  override val createrEffect: OnCreateSideEffect? = null,
  override val updater: OnUpdate? = null,
) : TestBaseContributionNode() {
  override val kindSet = Contributors.MapView + Contributors.NaverMap
}

private class OverlayContributionNode(
  override val contributor: Contributor = object : EmptyContributor {},
  override val attacher: OnAttach? = null,
  override val detacher: OnDetach? = null,
  override val createrEffect: OnCreateSideEffect? = null,
  override val updater: OnUpdate? = null,
) : TestBaseContributionNode() {
  override val kindSet = Contributors.Overlay
}

private abstract class TestBaseContributionNode : ContributionNode {
  abstract val contributor: Contributor
  abstract val createrEffect: OnCreateSideEffect?
  abstract val updater: OnUpdate?
  abstract val attacher: OnAttach?
  abstract val detacher: OnDetach?

  abstract override val kindSet: ContributionKind

  override fun create(): Contributor {
    createrEffect?.action(contributor)
    return contributor
  }

  override fun update(contributor: Contributor) {
    updater?.update(contributor)
  }

  override fun onAttach(instacne: Contributor) {
    attacher?.attach(instacne)
  }

  override fun onDetach(instacne: Contributor) {
    detacher?.detach(instacne)
  }

  override fun hashCode(): Int = System.identityHashCode(this)
  override fun equals(other: Any?) = this === other
}

private fun ContributionNode.asCleanFlagged() =
  FlaggedContributionNode(cleanNode = this)

private fun ContributionNode.asRemoveFlagged() =
  FlaggedContributionNode(
    dirtyLevel = ActionRemove,
    dirtyNode = this,
    cleanNode = null,
  )

private fun Assert<FlaggedContributionNode>.isClean() = given {
  if (it.dirtyLevel != null)
    fail("Expected dirtyLevel=null, but got ${it.dirtyLevel}", actual = it)
}

private fun Assert<List<FlaggedContributionNode>>.isAllClean() = given { nodes ->
  nodes.forEach { assertThat(it).isClean() }
}

private fun Assert<FlaggedContributionNode>.isRemoving() = given {
  if (it.dirtyLevel != ActionRemove)
    fail("Expected dirtyLevel=$ActionRemove, but got ${it.dirtyLevel}", actual = it)

  if (it.dirtyNode == null)
    fail("Expected dirtyNode!=null, but got null.", expected = it)

  if (it.cleanNode != null)
    fail("Expected cleanNode=null, but got ${it.cleanNode}", actual = it)
}

private fun Assert<List<FlaggedContributionNode>>.isAllRemoving() = given { nodes ->
  nodes.forEach { assertThat(it).isRemoving() }
}
