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

package land.sungbin.navermap.ui.modifier.polygonoverlay

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlin.Any
import kotlin.Unit
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.polygonoverlay.PolygonOverlayDelegate.Companion.NoOp

@Immutable
private data class PolygonOverlayTagModifierNode(
  private val arg0: Any?,
  override var delegator: PolygonOverlayDelegate = NoOp,
) : PolygonOverlayModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    PolygonOverlayTagContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, PolygonOverlayModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class PolygonOverlayTagContributionNode(
  public val arg0: Any?,
  public val `delegate`: PolygonOverlayDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = PolygonOverlayTagContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is PolygonOverlayTagContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }

  override fun onDetach(instance: Contributor) {
    require(instance is PolygonOverlayTagContributor)
    instance.clear?.invoke()
    instance.clear = null
  }
}

private class PolygonOverlayTagContributor(
  public var arg0: Any?,
  public var `delegate`: PolygonOverlayDelegate,
) : OverlayContributor {
  public var clear: (() -> Unit)? = null

  override fun Any.contribute() {
    delegate.setTag(this, arg0)
    clear = { delegate.setTag(this, null) }
  }
}

@Stable
public fun PolygonOverlayModifier.tag(arg0: Any?): PolygonOverlayModifier =
  this then PolygonOverlayTagModifierNode(arg0)
