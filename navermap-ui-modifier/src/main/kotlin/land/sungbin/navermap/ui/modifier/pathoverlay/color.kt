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

package land.sungbin.navermap.ui.modifier.pathoverlay

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlin.Any
import kotlin.Int
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.pathoverlay.PathOverlayDelegate.Companion.NoOp

@Immutable
private data class PathOverlayColorModifierNode(
  private val arg0: Int,
  override var delegator: PathOverlayDelegate = NoOp,
) : PathOverlayModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    PathOverlayColorContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, PathOverlayModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class PathOverlayColorContributionNode(
  public val arg0: Int,
  public val `delegate`: PathOverlayDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = PathOverlayColorContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is PathOverlayColorContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }
}

private class PathOverlayColorContributor(
  public var arg0: Int,
  public var `delegate`: PathOverlayDelegate,
) : OverlayContributor {
  override fun Any.contribute() {
    delegate.setColor(this, arg0)
  }
}

@Stable
public fun PathOverlayModifier.color(arg0: Int): PathOverlayModifier =
  this then PathOverlayColorModifierNode(arg0)
