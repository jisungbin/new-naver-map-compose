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

package land.sungbin.navermap.ui.modifier.circleoverlay

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.naver.maps.geometry.LatLng
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.circleoverlay.CircleOverlayDelegate.Companion.NoOp

@Immutable
private data class CircleOverlayCenterModifierNode(
  private val arg0: LatLng,
  override var delegator: CircleOverlayDelegate = NoOp,
) : CircleOverlayModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    CircleOverlayCenterContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, CircleOverlayModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class CircleOverlayCenterContributionNode(
  public val arg0: LatLng,
  public val `delegate`: CircleOverlayDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = CircleOverlayCenterContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is CircleOverlayCenterContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }
}

private class CircleOverlayCenterContributor(
  public var arg0: LatLng,
  public var `delegate`: CircleOverlayDelegate,
) : OverlayContributor {
  override fun Any.contribute() {
    delegate.setCenter(this, arg0)
  }
}

@Stable
public fun CircleOverlayModifier.center(arg0: LatLng): CircleOverlayModifier =
  this then CircleOverlayCenterModifierNode(arg0)
