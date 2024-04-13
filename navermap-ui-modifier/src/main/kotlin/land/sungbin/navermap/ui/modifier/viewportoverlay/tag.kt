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

package land.sungbin.navermap.ui.modifier.viewportoverlay

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlin.Any
import kotlin.Unit
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.viewportoverlay.ViewportOverlayDelegate.Companion.NoOp

@Immutable
private data class ViewportOverlayTagModifierNode(
  private val arg0: Any?,
  override var delegator: ViewportOverlayDelegate = NoOp,
) : ViewportOverlayModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    ViewportOverlayTagContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, ViewportOverlayModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class ViewportOverlayTagContributionNode(
  public val arg0: Any?,
  public val `delegate`: ViewportOverlayDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = ViewportOverlayTagContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is ViewportOverlayTagContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }

  override fun onDetach(instance: Contributor) {
    require(instance is ViewportOverlayTagContributor)
    instance.clear?.invoke()
    instance.clear = null
  }
}

private class ViewportOverlayTagContributor(
  public var arg0: Any?,
  public var `delegate`: ViewportOverlayDelegate,
) : OverlayContributor {
  public var clear: (() -> Unit)? = null

  override fun Any.contribute() {
    delegate.setTag(this, arg0)
    clear = { delegate.setTag(this, null) }
  }
}

@Stable
public fun ViewportOverlayModifier.tag(arg0: Any?): ViewportOverlayModifier =
  this then ViewportOverlayTagModifierNode(arg0)
