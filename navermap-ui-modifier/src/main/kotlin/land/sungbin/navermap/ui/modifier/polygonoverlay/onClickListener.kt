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
import com.naver.maps.map.overlay.Overlay
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.polygonoverlay.PolygonOverlayDelegate.Companion.NoOp

@Stable
private data class PolygonOverlayOnClickListenerModifierNode(
  private val arg0: Overlay.OnClickListener?,
  override var delegator: PolygonOverlayDelegate = NoOp,
) : PolygonOverlayModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    PolygonOverlayOnClickListenerContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, PolygonOverlayModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class PolygonOverlayOnClickListenerContributionNode(
  public val arg0: Overlay.OnClickListener?,
  public val `delegate`: PolygonOverlayDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind =
    land.sungbin.navermap.runtime.contributor.Contributors.Overlay

  override fun create(): Contributor = PolygonOverlayOnClickListenerContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is PolygonOverlayOnClickListenerContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }

  override fun onDetach(instance: Contributor) {
    require(instance is PolygonOverlayOnClickListenerContributor)
    instance.clear?.invoke()
    instance.clear = null
  }
}

private class PolygonOverlayOnClickListenerContributor(
  public var arg0: Overlay.OnClickListener?,
  public var `delegate`: PolygonOverlayDelegate,
) : OverlayContributor {
  public var clear: (() -> Unit)? = null

  override fun Any.contribute() {
    delegate.setOnClickListener(this, arg0)
    clear = { delegate.setOnClickListener(this, null) }
  }
}

/**
 * See
 * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setOnClickListener(com.naver.maps.map.overlay.Overlay.OnClickListener))
 */
@Stable
public fun PolygonOverlayModifier.onClickListener(arg0: Overlay.OnClickListener?): PolygonOverlayModifier = this then PolygonOverlayOnClickListenerModifierNode(arg0)
