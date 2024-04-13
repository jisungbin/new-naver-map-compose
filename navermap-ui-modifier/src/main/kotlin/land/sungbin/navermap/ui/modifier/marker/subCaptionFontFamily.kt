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
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.marker.MarkerDelegate.Companion.NoOp

@Stable
private data class MarkerSubCaptionFontFamilyModifierNode(
  private val arg0: Array<String>,
  override var delegator: MarkerDelegate = NoOp,
) : MarkerModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    MarkerSubCaptionFontFamilyContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, MarkerModifier) -> R): R =
    operation(initial, this)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is MarkerSubCaptionFontFamilyModifierNode) return false

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
private data class MarkerSubCaptionFontFamilyContributionNode(
  public val arg0: Array<String>,
  public val `delegate`: MarkerDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = MarkerSubCaptionFontFamilyContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is MarkerSubCaptionFontFamilyContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is MarkerSubCaptionFontFamilyContributionNode) return false

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

private class MarkerSubCaptionFontFamilyContributor(
  public var arg0: Array<String>,
  public var `delegate`: MarkerDelegate,
) : OverlayContributor {
  override fun Any.contribute() {
    delegate.setSubCaptionFontFamily(this, arg0)
  }
}

/**
 * See
 * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setSubCaptionFontFamily(java.lang.String...))
 */
@Stable
public fun MarkerModifier.subCaptionFontFamily(arg0: Array<String>): MarkerModifier =
  this then MarkerSubCaptionFontFamilyModifierNode(arg0)
