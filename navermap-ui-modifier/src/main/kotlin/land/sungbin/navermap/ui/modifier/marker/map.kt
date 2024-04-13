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
import com.naver.maps.map.NaverMap
import kotlin.Any
import kotlin.Unit
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.marker.MarkerDelegate.Companion.NoOp

@Immutable
private data class MarkerMapModifierNode(
  private val arg0: NaverMap?,
  override var delegator: MarkerDelegate = NoOp,
) : MarkerModifier {
  override fun getContributionNode(): MapModifierContributionNode = MarkerMapContributionNode(
    arg0,
    delegator,
  )

  override fun <R : Any> fold(initial: R, operation: (R, MarkerModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class MarkerMapContributionNode(
  public val arg0: NaverMap?,
  public val `delegate`: MarkerDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = MarkerMapContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is MarkerMapContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }

  override fun onDetach(instance: Contributor) {
    require(instance is MarkerMapContributor)
    instance.clear?.invoke()
    instance.clear = null
  }
}

private class MarkerMapContributor(
  public var arg0: NaverMap?,
  public var `delegate`: MarkerDelegate,
) : OverlayContributor {
  public var clear: (() -> Unit)? = null

  override fun Any.contribute() {
    delegate.setMap(this, arg0)
    clear = { delegate.setMap(this, null) }
  }
}

@Stable
public fun MarkerModifier.map(arg0: NaverMap?): MarkerModifier =
  this then MarkerMapModifierNode(arg0)
