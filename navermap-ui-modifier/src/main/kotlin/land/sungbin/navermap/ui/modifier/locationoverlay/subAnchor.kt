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

import android.graphics.PointF
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.locationoverlay.LocationOverlayDelegate.Companion.NoOp

@Immutable
private data class LocationOverlaySubAnchorModifierNode(
  private val arg0: PointF,
  override var delegator: LocationOverlayDelegate = NoOp,
) : LocationOverlayModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    LocationOverlaySubAnchorContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, LocationOverlayModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class LocationOverlaySubAnchorContributionNode(
  public val arg0: PointF,
  public val `delegate`: LocationOverlayDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = LocationOverlaySubAnchorContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is LocationOverlaySubAnchorContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }
}

private class LocationOverlaySubAnchorContributor(
  public var arg0: PointF,
  public var `delegate`: LocationOverlayDelegate,
) : OverlayContributor {
  override fun Any.contribute() {
    delegate.setSubAnchor(this, arg0)
  }
}

@Stable
public fun LocationOverlayModifier.subAnchor(arg0: PointF): LocationOverlayModifier =
  this then LocationOverlaySubAnchorModifierNode(arg0)
