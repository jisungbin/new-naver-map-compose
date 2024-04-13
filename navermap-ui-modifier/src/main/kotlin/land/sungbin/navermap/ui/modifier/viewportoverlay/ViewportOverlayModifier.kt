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

import androidx.compose.runtime.Stable
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.viewportoverlay.ViewportOverlayDelegate.Companion.NoOp
import java.lang.System.identityHashCode

public interface ViewportOverlayModifier {
  public var delegator: ViewportOverlayDelegate

  public fun getContributionNode(): MapModifierContributionNode?

  public fun <R : Any> fold(initial: R, operation: (R, ViewportOverlayModifier) -> R): R

  public infix fun then(other: ViewportOverlayModifier): ViewportOverlayModifier = if
    (other === ViewportOverlayModifier) this else CombinedViewportOverlayModifier(this, other)

  public companion object : ViewportOverlayModifier {
    override var delegator: ViewportOverlayDelegate = NoOp

    override fun getContributionNode(): MapModifierContributionNode? = null

    override fun <R : Any> fold(initial: R, operation: (R, ViewportOverlayModifier) -> R): R =
      initial

    override infix fun then(other: ViewportOverlayModifier): ViewportOverlayModifier = other

    override fun hashCode(): Int = identityHashCode(this)

    override fun equals(other: Any?): Boolean = this === other

    override fun toString(): String = "ViewportOverlayModifier"
  }
}

@Stable
public class CombinedViewportOverlayModifier(
  private val outer: ViewportOverlayModifier,
  private val `inner`: ViewportOverlayModifier,
) : ViewportOverlayModifier {
  override var delegator: ViewportOverlayDelegate = NoOp

  override fun getContributionNode(): MapModifierContributionNode? = null

  override fun <R : Any> fold(initial: R, operation: (R, ViewportOverlayModifier) -> R): R =
    inner.fold(outer.fold(initial, operation), operation)

  override fun equals(other: Any?): Boolean = other is CombinedViewportOverlayModifier &&
    outer == other.outer && inner == other.inner

  override fun hashCode(): Int = outer.hashCode() + 31 * inner.hashCode()

  override fun toString(): String = "[" + fold("") { acc, element ->
    if (acc.isEmpty())
      element.toString() else """$acc, $element"""
  } + "]"
}
