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

package land.sungbin.navermap.ui.modifier.generator.template

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.runtime.node.DelegatedOverlay

interface MarkerDelegate {
  fun setPosition(instance: DelegatedOverlay, latlng: LatLng?) {}

  companion object {
    val NoOp = object : MarkerDelegate {}
  }
}

@Suppress("unused")
object RealMarkerDelegate : MarkerDelegate {
  override fun setPosition(instance: DelegatedOverlay, latlng: LatLng?) {
    require(instance is Marker)
    instance.position = latlng!!
  }
}

interface MarkerModifier {
  var delegator: MarkerDelegate
  fun getContributionNode(): MapModifierContributionNode?

  fun <R> fold(initial: R, operation: (R, MarkerModifier) -> R): R

  infix fun then(other: MarkerModifier): MarkerModifier =
    if (other === MarkerModifier) this else CombinedMarkerModifier(this, other)

  companion object : MarkerModifier {
    override var delegator = MarkerDelegate.NoOp

    override fun getContributionNode(): MapModifierContributionNode? = null
    override fun <R> fold(initial: R, operation: (R, MarkerModifier) -> R): R = initial
    override infix fun then(other: MarkerModifier): MarkerModifier = other

    override fun hashCode(): Int = System.identityHashCode(this)
    override fun equals(other: Any?): Boolean = this === other
    override fun toString(): String = "MarkerModifier"
  }
}

@Stable
class CombinedMarkerModifier(
  private val outer: MarkerModifier,
  private val inner: MarkerModifier,
) : MarkerModifier {
  override var delegator: MarkerDelegate = MarkerDelegate.NoOp
  override fun getContributionNode(): MapModifierContributionNode? = null

  override fun <R> fold(initial: R, operation: (R, MarkerModifier) -> R): R =
    inner.fold(outer.fold(initial, operation), operation)

  override fun equals(other: Any?): Boolean =
    other is CombinedMarkerModifier && outer == other.outer && inner == other.inner

  override fun hashCode(): Int = outer.hashCode() + 31 * inner.hashCode()

  override fun toString(): String = "[" + fold("") { acc, element ->
    if (acc.isEmpty()) element.toString() else "$acc, $element"
  } + "]"
}

@Stable
fun MarkerModifier.offset(latlng: LatLng): MarkerModifier =
  this then MarkerLatLngModifierNode(latlng)

fun main() {
  val modifier = MarkerModifier.offset(LatLng.INVALID)
  print(modifier)
}

@Immutable
private data class MarkerLatLngModifierNode(
  private val latlng: LatLng,
  override var delegator: MarkerDelegate = MarkerDelegate.NoOp,
) : MarkerModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    MarkerLatLngContributionNode(latlng, delegator)

  override fun <R> fold(initial: R, operation: (R, MarkerModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class MarkerLatLngContributionNode(
  val latlng: LatLng,
  val delegate: MarkerDelegate = MarkerDelegate.NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Contributors.Overlay

  override fun create(): Contributor = MarkerLatLngContributor(latlng, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is MarkerLatLngContributor)
    contributor.latlng = latlng
    contributor.delegate = delegate
  }

  override fun onDetach(instance: Contributor) {
    require(instance is MarkerLatLngContributor)
    instance.clear?.invoke()
    instance.clear = null
  }
}

private class MarkerLatLngContributor(
  var latlng: LatLng,
  var delegate: MarkerDelegate,
) : OverlayContributor {
  var clear: (() -> Unit)? = null

  override fun DelegatedOverlay.contribute() {
    delegate.setPosition(this, latlng)
    clear = { delegate.setPosition(this, null) }
  }
}
