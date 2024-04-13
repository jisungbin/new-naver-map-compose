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

package land.sungbin.navermap.ui.modifier.locationoverlay

import androidx.compose.runtime.Stable
import com.naver.maps.geometry.LatLng
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.locationoverlay.LocationOverlayDelegate.Companion.NoOp

@Stable
private data class LocationOverlayPositionModifierNode(
  private val arg0: LatLng,
  override var delegator: LocationOverlayDelegate = NoOp,
) : LocationOverlayModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    LocationOverlayPositionContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, LocationOverlayModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class LocationOverlayPositionContributionNode(
  public val arg0: LatLng,
  public val `delegate`: LocationOverlayDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = LocationOverlayPositionContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is LocationOverlayPositionContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }
}

private class LocationOverlayPositionContributor(
  public var arg0: LatLng,
  public var `delegate`: LocationOverlayDelegate,
) : OverlayContributor {
  override fun Any.contribute() {
    delegate.setPosition(this, arg0)
  }
}

/**
 * See
 * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setPosition(com.naver.maps.geometry.LatLng))
 */
@Stable
public fun LocationOverlayModifier.position(arg0: LatLng): LocationOverlayModifier =
  this then LocationOverlayPositionModifierNode(arg0)
