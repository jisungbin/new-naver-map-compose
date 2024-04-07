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
import kotlin.contracts.contract

@Stable
public sealed interface MapModifier {
  public val head: MapModifier?

  public infix fun then(other: MapModifierNode<*>): MapModifier =
    CombinedMapModifier(head = this, tail = other)

  public infix fun then(other: MapModifier): MapModifier {
    if (other !is CombinedMapModifier) return this
    else {
      var current = this
      var traversal = this
      while (traversal is CombinedMapModifier) {
        current = current then traversal.tail
        traversal = traversal.head
      }
      return current
    }
  }

  public companion object : MapModifier {
    override val head: MapModifier? = null
    override fun hashCode(): Int = 0
    override fun equals(other: Any?): Boolean = other === this
  }
}

@Stable
internal class CombinedMapModifier(
  override val head: MapModifier,
  val tail: MapModifierNode<*>,
) : MapModifier {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is CombinedMapModifier) return false

    if (head != other.head) return false
    if (tail != other.tail) return false

    return true
  }

  override fun hashCode(): Int {
    var result = head.hashCode()
    result = 31 * result + tail.hashCode()
    return result
  }
}

internal inline fun MapModifier.forEachNode(block: (node: MapModifierNode<*>) -> Unit) {
  contract { callsInPlace(block) }
  if (this !is CombinedMapModifier) return

  var current: MapModifier? = this

  while (current is CombinedMapModifier) {
    block(current.tail)
    current = current.head
  }
}
