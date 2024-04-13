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

package land.sungbin.navermap.ui.modifier.pathoverlay

import androidx.compose.runtime.Stable
import com.naver.maps.map.overlay.OverlayImage
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.pathoverlay.PathOverlayDelegate.Companion.NoOp

@Stable
private data class PathOverlayPatternImageModifierNode(
  private val arg0: OverlayImage?,
  override var delegator: PathOverlayDelegate = NoOp,
) : PathOverlayModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    PathOverlayPatternImageContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, PathOverlayModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class PathOverlayPatternImageContributionNode(
  public val arg0: OverlayImage?,
  public val `delegate`: PathOverlayDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = PathOverlayPatternImageContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is PathOverlayPatternImageContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }

  override fun onDetach(instance: Contributor) {
    require(instance is PathOverlayPatternImageContributor)
    instance.clear?.invoke()
    instance.clear = null
  }
}

private class PathOverlayPatternImageContributor(
  public var arg0: OverlayImage?,
  public var `delegate`: PathOverlayDelegate,
) : OverlayContributor {
  public var clear: (() -> Unit)? = null

  override fun Any.contribute() {
    delegate.setPatternImage(this, arg0)
    clear = { delegate.setPatternImage(this, null) }
  }
}

/**
 * See
 * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html#setPatternImage(com.naver.maps.map.overlay.OverlayImage))
 */
@Stable
public fun PathOverlayModifier.patternImage(arg0: OverlayImage?): PathOverlayModifier =
  this then PathOverlayPatternImageModifierNode(arg0)
