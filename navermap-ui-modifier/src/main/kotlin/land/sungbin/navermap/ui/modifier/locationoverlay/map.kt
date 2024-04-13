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

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.naver.maps.map.NaverMap
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.locationoverlay.LocationOverlayDelegate.Companion.NoOp

@Immutable
private data class LocationOverlayMapModifierNode(
  private val arg0: NaverMap?,
  override var delegator: LocationOverlayDelegate = NoOp,
) : LocationOverlayModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    LocationOverlayMapContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, LocationOverlayModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class LocationOverlayMapContributionNode(
  public val arg0: NaverMap?,
  public val `delegate`: LocationOverlayDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = LocationOverlayMapContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is LocationOverlayMapContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }

  override fun onDetach(instance: Contributor) {
    require(instance is LocationOverlayMapContributor)
    instance.clear?.invoke()
    instance.clear = null
  }
}

private class LocationOverlayMapContributor(
  public var arg0: NaverMap?,
  public var `delegate`: LocationOverlayDelegate,
) : OverlayContributor {
  public var clear: (() -> Unit)? = null

  override fun Any.contribute() {
    delegate.setMap(this, arg0)
    clear = { delegate.setMap(this, null) }
  }
}

@Stable
@Deprecated(
  "Deprecated from the original API.",
  ReplaceWith(""),
)
public fun LocationOverlayModifier.map(arg0: NaverMap?): LocationOverlayModifier =
  this then LocationOverlayMapModifierNode(arg0)
