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

package land.sungbin.navermap.ui.modifier.polygonoverlay

import androidx.compose.runtime.Stable
import com.naver.maps.geometry.LatLng
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.polygonoverlay.PolygonOverlayDelegate.Companion.NoOp

@Stable
private data class PolygonOverlayHolesModifierNode(
  private val arg0: List<List<LatLng>>,
  override var delegator: PolygonOverlayDelegate = NoOp,
) : PolygonOverlayModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    PolygonOverlayHolesContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, PolygonOverlayModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class PolygonOverlayHolesContributionNode(
  public val arg0: List<List<LatLng>>,
  public val `delegate`: PolygonOverlayDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = PolygonOverlayHolesContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is PolygonOverlayHolesContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }
}

private class PolygonOverlayHolesContributor(
  public var arg0: List<List<LatLng>>,
  public var `delegate`: PolygonOverlayDelegate,
) : OverlayContributor {
  override fun Any.contribute() {
    delegate.setHoles(this, arg0)
  }
}

/**
 * See
 * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PolygonOverlay.html#setHoles(java.util.List))
 */
@Stable
public fun PolygonOverlayModifier.holes(arg0: List<List<LatLng>>): PolygonOverlayModifier =
  this then PolygonOverlayHolesModifierNode(arg0)
