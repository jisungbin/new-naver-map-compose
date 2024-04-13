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

package land.sungbin.navermap.ui.modifier.infowindow

import androidx.compose.runtime.Stable
import com.naver.maps.map.overlay.InfoWindow
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.infowindow.InfoWindowDelegate.Companion.NoOp

@Stable
private data class InfoWindowAdapterModifierNode(
  private val arg0: InfoWindow.Adapter,
  override var delegator: InfoWindowDelegate = NoOp,
) : InfoWindowModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    InfoWindowAdapterContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, InfoWindowModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class InfoWindowAdapterContributionNode(
  public val arg0: InfoWindow.Adapter,
  public val `delegate`: InfoWindowDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = InfoWindowAdapterContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is InfoWindowAdapterContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }
}

private class InfoWindowAdapterContributor(
  public var arg0: InfoWindow.Adapter,
  public var `delegate`: InfoWindowDelegate,
) : OverlayContributor {
  override fun Any.contribute() {
    delegate.setAdapter(this, arg0)
  }
}

/**
 * See
 * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/InfoWindow.html#setAdapter(com.naver.maps.map.overlay.InfoWindow.Adapter))
 */
@Stable
public fun InfoWindowModifier.adapter(arg0: InfoWindow.Adapter): InfoWindowModifier =
  this then InfoWindowAdapterModifierNode(arg0)
