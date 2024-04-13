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

package land.sungbin.navermap.ui.modifier.pathoverlay

import androidx.compose.runtime.Stable
import java.lang.System.identityHashCode
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.pathoverlay.PathOverlayDelegate.Companion.NoOp

public interface PathOverlayModifier {
  public var delegator: PathOverlayDelegate

  public fun getContributionNode(): MapModifierContributionNode?

  public fun <R : Any> fold(initial: R, operation: (R, PathOverlayModifier) -> R): R

  public infix fun then(other: PathOverlayModifier): PathOverlayModifier = if
                                                                             (other === PathOverlayModifier) this else CombinedPathOverlayModifier(this, other)

  public companion object : PathOverlayModifier {
    override var delegator: PathOverlayDelegate = NoOp

    override fun getContributionNode(): MapModifierContributionNode? = null

    override fun <R : Any> fold(initial: R, operation: (R, PathOverlayModifier) -> R): R = initial

    override infix fun then(other: PathOverlayModifier): PathOverlayModifier = other

    override fun hashCode(): Int = identityHashCode(this)

    override fun equals(other: Any?): Boolean = this === other

    override fun toString(): String = "PathOverlayModifier"
  }
}

@Stable
public class CombinedPathOverlayModifier(
  private val outer: PathOverlayModifier,
  private val `inner`: PathOverlayModifier,
) : PathOverlayModifier {
  override var delegator: PathOverlayDelegate = NoOp

  override fun getContributionNode(): MapModifierContributionNode? = null

  override fun <R : Any> fold(initial: R, operation: (R, PathOverlayModifier) -> R): R =
    inner.fold(outer.fold(initial, operation), operation)

  override fun equals(other: Any?): Boolean = other is CombinedPathOverlayModifier &&
    outer == other.outer && inner == other.inner

  override fun hashCode(): Int = outer.hashCode() + 31 * inner.hashCode()

  override fun toString(): String = "[" + fold("") { acc, element ->
    if (acc.isEmpty())
      element.toString() else """$acc, $element"""
  } + "]"
}
