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

package land.sungbin.navermap.runtime.modifier

import androidx.compose.runtime.Stable

@Stable
public sealed interface MapModifier {
  public fun <R> foldIn(initial: R, operation: (R, MapModifierNode<*>) -> R): R
  public fun <R> foldOut(initial: R, operation: (MapModifierNode<*>, R) -> R): R
  public fun any(predicate: (MapModifierNode<*>) -> Boolean): Boolean
  public fun all(predicate: (MapModifierNode<*>) -> Boolean): Boolean

  public infix fun then(other: MapModifier): MapModifier =
    if (other === MapModifier) this else CombinedMapModifier(this, other)

  public companion object : MapModifierNode<Nothing> {
    override fun <R> foldIn(initial: R, operation: (R, MapModifierNode<*>) -> R): R = initial
    override fun <R> foldOut(initial: R, operation: (MapModifierNode<*>, R) -> R): R = initial

    override fun any(predicate: (MapModifierNode<*>) -> Boolean): Boolean = false
    override fun all(predicate: (MapModifierNode<*>) -> Boolean): Boolean = true

    override fun hashCode(): Int = System.identityHashCode(this)
    override fun equals(other: Any?): Boolean = this === other

    override infix fun then(other: MapModifier): MapModifier = other
    override fun toString(): String = "MapModifier"
  }
}

@Stable
public class CombinedMapModifier(
  internal val outer: MapModifier,
  internal val inner: MapModifier,
) : MapModifier {
  override fun <R> foldIn(initial: R, operation: (R, MapModifierNode<*>) -> R): R =
    inner.foldIn(outer.foldIn(initial, operation), operation)

  override fun <R> foldOut(initial: R, operation: (MapModifierNode<*>, R) -> R): R =
    outer.foldOut(inner.foldOut(initial, operation), operation)

  override fun any(predicate: (MapModifierNode<*>) -> Boolean): Boolean =
    outer.any(predicate) || inner.any(predicate)

  override fun all(predicate: (MapModifierNode<*>) -> Boolean): Boolean =
    outer.all(predicate) && inner.all(predicate)

  override fun equals(other: Any?): Boolean =
    other is CombinedMapModifier && outer == other.outer && inner == other.inner

  override fun hashCode(): Int = outer.hashCode() + 31 * inner.hashCode()

  override fun toString(): String = "[" + foldIn("") { acc, element ->
    if (acc.isEmpty()) element.toString() else "$acc, $element"
  } + "]"
}
