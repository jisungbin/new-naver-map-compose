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
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.polygonoverlay.PolygonOverlayDelegate.Companion.NoOp

@Stable
private data class PolygonOverlayColorModifierNode(
  private val arg0: Int,
  override var delegator: PolygonOverlayDelegate = NoOp,
) : PolygonOverlayModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    PolygonOverlayColorContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, PolygonOverlayModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class PolygonOverlayColorContributionNode(
  public val arg0: Int,
  public val `delegate`: PolygonOverlayDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = PolygonOverlayColorContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is PolygonOverlayColorContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }
}

private class PolygonOverlayColorContributor(
  public var arg0: Int,
  public var `delegate`: PolygonOverlayDelegate,
) : OverlayContributor {
  override fun Any.contribute() {
    delegate.setColor(this, arg0)
  }
}

/**
 * See
 * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PolygonOverlay.html#setColor(int))
 */
@Stable
public fun PolygonOverlayModifier.color(arg0: Int): PolygonOverlayModifier =
  this then PolygonOverlayColorModifierNode(arg0)
