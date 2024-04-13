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

package land.sungbin.navermap.ui.modifier.marker

import androidx.compose.runtime.Stable
import com.naver.maps.map.overlay.Align
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.marker.MarkerDelegate.Companion.NoOp

@Stable
private data class MarkerCaptionAlignsModifierNode(
  private val arg0: Array<Align>,
  override var delegator: MarkerDelegate = NoOp,
) : MarkerModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    MarkerCaptionAlignsContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, MarkerModifier) -> R): R =
    operation(initial, this)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is MarkerCaptionAlignsModifierNode) return false

    if (!arg0.contentEquals(other.arg0)) return false
    if (delegator != other.delegator) return false

    return true
  }

  override fun hashCode(): Int {
    var result = arg0.contentHashCode()
    result = 31 * result + delegator.hashCode()
    return result
  }
}

@Stable
private data class MarkerCaptionAlignsContributionNode(
  public val arg0: Array<Align>,
  public val `delegate`: MarkerDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = MarkerCaptionAlignsContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is MarkerCaptionAlignsContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is MarkerCaptionAlignsContributionNode) return false

    if (!arg0.contentEquals(other.arg0)) return false
    if (`delegate` != other.`delegate`) return false

    return true
  }

  override fun hashCode(): Int {
    var result = arg0.contentHashCode()
    result = 31 * result + `delegate`.hashCode()
    return result
  }
}

private class MarkerCaptionAlignsContributor(
  public var arg0: Array<Align>,
  public var `delegate`: MarkerDelegate,
) : OverlayContributor {
  override fun Any.contribute() {
    delegate.setCaptionAligns(this, arg0)
  }
}

/**
 * See
 * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setCaptionAligns(com.naver.maps.map.overlay.Align...))
 */
@Stable
public fun MarkerModifier.captionAligns(arg0: Array<Align>): MarkerModifier =
  this then MarkerCaptionAlignsModifierNode(arg0)
