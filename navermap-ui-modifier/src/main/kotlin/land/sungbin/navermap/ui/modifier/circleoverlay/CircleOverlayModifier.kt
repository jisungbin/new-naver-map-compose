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

package land.sungbin.navermap.ui.modifier.circleoverlay

import androidx.compose.runtime.Stable
import java.lang.System.identityHashCode
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.circleoverlay.CircleOverlayDelegate.Companion.NoOp

public interface CircleOverlayModifier {
  public var delegator: CircleOverlayDelegate

  public fun getContributionNode(): MapModifierContributionNode?

  public fun <R : Any> fold(initial: R, operation: (R, CircleOverlayModifier) -> R): R

  public infix fun then(other: CircleOverlayModifier): CircleOverlayModifier = if
    (other === CircleOverlayModifier) this else CombinedCircleOverlayModifier(this, other)

  public companion object : CircleOverlayModifier {
    override var delegator: CircleOverlayDelegate = NoOp

    override fun getContributionNode(): MapModifierContributionNode? = null

    override fun <R : Any> fold(initial: R, operation: (R, CircleOverlayModifier) -> R): R = initial

    override infix fun then(other: CircleOverlayModifier): CircleOverlayModifier = other

    override fun hashCode(): Int = identityHashCode(this)

    override fun equals(other: Any?): Boolean = this === other

    override fun toString(): String = "CircleOverlayModifier"
  }
}

@Stable
public class CombinedCircleOverlayModifier(
  private val outer: CircleOverlayModifier,
  private val `inner`: CircleOverlayModifier,
) : CircleOverlayModifier {
  override var delegator: CircleOverlayDelegate = NoOp

  override fun getContributionNode(): MapModifierContributionNode? = null

  override fun <R : Any> fold(initial: R, operation: (R, CircleOverlayModifier) -> R): R =
    inner.fold(outer.fold(initial, operation), operation)

  override fun equals(other: Any?): Boolean = other is CombinedCircleOverlayModifier &&
    outer == other.outer && inner == other.inner

  override fun hashCode(): Int = outer.hashCode() + 31 * inner.hashCode()

  override fun toString(): String = "[" + fold("") { acc, element ->
    if (acc.isEmpty())
      element.toString() else """$acc, $element"""
  } + "]"
}
