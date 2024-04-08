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

package land.sungbin.navermap.runtime

import androidx.compose.runtime.Applier
import androidx.compose.ui.util.fastForEach
import land.sungbin.navermap.runtime.node.LayoutNode
import land.sungbin.navermap.runtime.node.MapNode

public class MapApplier(private val root: LayoutNode) : Applier<MapNode<*>> {
  private val stack = mutableListOf<MapNode<*>>()
  override var current: MapNode<*> = root
    private set

  init {
    root.attach()
  }

  override fun down(node: MapNode<*>) {
    stack.add(current)
    current = node
  }

  override fun up() {
    check(stack.isNotEmpty()) { "empty stack" }
    current = stack.removeAt(stack.size - 1)
  }

  override fun insertTopDown(index: Int, instance: MapNode<*>) {
    // Ignored. Insert is performed in [insertBottomUp] to build the tree bottom-up to
    // avoid duplicate notification when the child nodes enter the tree
  }

  override fun insertBottomUp(index: Int, instance: MapNode<*>) {
    current.insertAt(index, instance)
  }

  override fun move(from: Int, to: Int, count: Int) {
    current.move(from, to, count)
  }

  override fun remove(index: Int, count: Int) {
    current.removeAt(index, count)
  }

  override fun clear() {
    stack.fastForEach(MapNode<*>::removeAll)
    stack.clear()
    current = root.also(MapNode<*>::removeAll)
  }
}
