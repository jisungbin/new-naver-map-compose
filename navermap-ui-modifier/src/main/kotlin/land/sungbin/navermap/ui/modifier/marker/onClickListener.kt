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

package land.sungbin.navermap.ui.modifier.marker

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.naver.maps.map.overlay.Overlay
import kotlin.Any
import kotlin.Unit
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.marker.MarkerDelegate.Companion.NoOp

@Immutable
private data class MarkerOnClickListenerModifierNode(
  private val arg0: Overlay.OnClickListener?,
  override var delegator: MarkerDelegate = NoOp,
) : MarkerModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    MarkerOnClickListenerContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, MarkerModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class MarkerOnClickListenerContributionNode(
  public val arg0: Overlay.OnClickListener?,
  public val `delegate`: MarkerDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind =
    land.sungbin.navermap.runtime.contributor.Contributors.Overlay

  override fun create(): Contributor = MarkerOnClickListenerContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is MarkerOnClickListenerContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }

  override fun onDetach(instance: Contributor) {
    require(instance is MarkerOnClickListenerContributor)
    instance.clear?.invoke()
    instance.clear = null
  }
}

private class MarkerOnClickListenerContributor(
  public var arg0: Overlay.OnClickListener?,
  public var `delegate`: MarkerDelegate,
) : OverlayContributor {
  public var clear: (() -> Unit)? = null

  override fun Any.contribute() {
    delegate.setOnClickListener(this, arg0)
    clear = { delegate.setOnClickListener(this, null) }
  }
}

@Stable
public fun MarkerModifier.onClickListener(arg0: Overlay.OnClickListener?): MarkerModifier =
  this then MarkerOnClickListenerModifierNode(arg0)
