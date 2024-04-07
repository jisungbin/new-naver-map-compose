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

package land.sungbin.navermap.runtime.node

import androidx.compose.runtime.collection.MutableVector
import androidx.compose.runtime.collection.mutableVectorOf

public abstract class MapNode<Owner> {
  public abstract var symbol: Symbol<Owner>

  protected val children: MutableVector<MapNode<*>> = mutableVectorOf()

  public abstract fun attach()
  public abstract fun detach()

  public fun insertAt(index: Int, instance: MapNode<*>) {
    children.add(index, instance)
  }

  public fun removeAt(index: Int, count: Int) {
    require(count >= 0) { "count ($count) must be greater than 0" }
    for (i in index + count - 1 downTo index) {
      val child = children.removeAt(i)
      child.detach()
    }
  }

  public fun move(from: Int, to: Int, count: Int) {
    if (from == to) return

    for (i in 0 until count) {
      val fromIndex = if (from > to) from + i else from
      val toIndex = if (from > to) to + i else to + count - 2
      val child = children.removeAt(fromIndex)
      children.add(toIndex, child)
    }
  }

  public fun removeAll() {
    for (i in children.size - 1 downTo 0) {
      val child = children.removeAt(i)
      child.detach()
    }
  }
}
