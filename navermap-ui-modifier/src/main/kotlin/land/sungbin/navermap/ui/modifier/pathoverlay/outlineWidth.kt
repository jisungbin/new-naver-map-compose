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

@file:Suppress("RedundantVisibilityModifier")

package land.sungbin.navermap.ui.modifier.pathoverlay

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.pathoverlay.PathOverlayDelegate.Companion.NoOp

@Immutable
private data class PathOverlayOutlineWidthModifierNode(
  private val arg0: Int,
  override var delegator: PathOverlayDelegate = NoOp,
) : PathOverlayModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    PathOverlayOutlineWidthContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, PathOverlayModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class PathOverlayOutlineWidthContributionNode(
  public val arg0: Int,
  public val `delegate`: PathOverlayDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = PathOverlayOutlineWidthContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is PathOverlayOutlineWidthContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }
}

private class PathOverlayOutlineWidthContributor(
  public var arg0: Int,
  public var `delegate`: PathOverlayDelegate,
) : OverlayContributor {
  override fun Any.contribute() {
    delegate.setOutlineWidth(this, arg0)
  }
}

@Stable
public fun PathOverlayModifier.outlineWidth(arg0: Int): PathOverlayModifier =
  this then PathOverlayOutlineWidthModifierNode(arg0)
