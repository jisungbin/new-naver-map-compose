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
public interface MapModifierNode<T> : MapModifier {
  public fun onAttach(instance: T) {}
  public fun onDetach(instance: T) {}

  override fun <R> foldIn(initial: R, operation: (R, MapModifierNode<*>) -> R): R =
    operation(initial, this)

  override fun <R> foldOut(initial: R, operation: (MapModifierNode<*>, R) -> R): R =
    operation(this, initial)

  override fun any(predicate: (MapModifierNode<*>) -> Boolean): Boolean = predicate(this)
  override fun all(predicate: (MapModifierNode<*>) -> Boolean): Boolean = predicate(this)

  public override fun hashCode(): Int
  public override fun equals(other: Any?): Boolean
}
