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

package land.sungbin.navermap.ui.modifier.groundoverlay

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlin.Any
import kotlin.Int
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.groundoverlay.GroundOverlayDelegate.Companion.NoOp

@Immutable
private data class GroundOverlayGlobalZIndexModifierNode(
  private val arg0: Int,
  override var delegator: GroundOverlayDelegate = NoOp,
) : GroundOverlayModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    GroundOverlayGlobalZIndexContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, GroundOverlayModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class GroundOverlayGlobalZIndexContributionNode(
  public val arg0: Int,
  public val `delegate`: GroundOverlayDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = GroundOverlayGlobalZIndexContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is GroundOverlayGlobalZIndexContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }
}

private class GroundOverlayGlobalZIndexContributor(
  public var arg0: Int,
  public var `delegate`: GroundOverlayDelegate,
) : OverlayContributor {
  override fun Any.contribute() {
    delegate.setGlobalZIndex(this, arg0)
  }
}

@Stable
public fun GroundOverlayModifier.globalZIndex(arg0: Int): GroundOverlayModifier =
  this then GroundOverlayGlobalZIndexModifierNode(arg0)
