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

import androidx.compose.runtime.Stable
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.polygonoverlay.PolygonOverlayDelegate.Companion.NoOp
import java.lang.System.identityHashCode

public interface PolygonOverlayModifier {
  public var delegator: PolygonOverlayDelegate

  public fun getContributionNode(): MapModifierContributionNode?

  public fun <R : Any> fold(initial: R, operation: (R, PolygonOverlayModifier) -> R): R

  public infix fun then(other: PolygonOverlayModifier): PolygonOverlayModifier = if
                                                                                   (other === PolygonOverlayModifier) this else CombinedPolygonOverlayModifier(this, other)

  public companion object : PolygonOverlayModifier {
    override var delegator: PolygonOverlayDelegate = NoOp

    override fun getContributionNode(): MapModifierContributionNode? = null

    override fun <R : Any> fold(initial: R, operation: (R, PolygonOverlayModifier) -> R): R =
      initial

    override infix fun then(other: PolygonOverlayModifier): PolygonOverlayModifier = other

    override fun hashCode(): Int = identityHashCode(this)

    override fun equals(other: Any?): Boolean = this === other

    override fun toString(): String = "PolygonOverlayModifier"
  }
}

@Stable
public class CombinedPolygonOverlayModifier(
  private val outer: PolygonOverlayModifier,
  private val `inner`: PolygonOverlayModifier,
) : PolygonOverlayModifier {
  override var delegator: PolygonOverlayDelegate = NoOp

  override fun getContributionNode(): MapModifierContributionNode? = null

  override fun <R : Any> fold(initial: R, operation: (R, PolygonOverlayModifier) -> R): R =
    inner.fold(outer.fold(initial, operation), operation)

  override fun equals(other: Any?): Boolean = other is CombinedPolygonOverlayModifier &&
    outer == other.outer && inner == other.inner

  override fun hashCode(): Int = outer.hashCode() + 31 * inner.hashCode()

  override fun toString(): String = "[" + fold("") { acc, element ->
    if (acc.isEmpty())
      element.toString() else """$acc, $element"""
  } + "]"
}
