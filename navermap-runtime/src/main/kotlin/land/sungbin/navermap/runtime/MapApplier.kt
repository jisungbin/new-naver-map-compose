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

import androidx.compose.runtime.AbstractApplier
import land.sungbin.navermap.runtime.node.HostMapNode
import land.sungbin.navermap.runtime.node.LayoutNode
import land.sungbin.navermap.runtime.node.MapNode
import org.jetbrains.annotations.TestOnly

/** Enable to log changes to the LayoutNode tree. This logging is quite chatty. */
@set:TestOnly
internal var DebugChanges = false

public class MapApplier(host: HostMapNode = HostMapNode()) : AbstractApplier<MapNode<*>>(host) {
  private var layoutNode: LayoutNode? = null

  override fun down(node: MapNode<*>) {
    if (DebugChanges) println("down $node")
    if (node is MapNode.Root) node.attach()
    if (node is MapNode.Child) node.attachIfReady()
    super.down(node)
  }

  override fun up() {
    if (DebugChanges) println("up")
    super.up()
  }

  override fun insertTopDown(index: Int, instance: MapNode<*>) {
    if (DebugChanges) println("insertTopDown $instance at $index")
    if (instance is LayoutNode && layoutNode == null) layoutNode = instance
    if (instance is MapNode.Child) {
      val parent = checkNotNull(layoutNode) { "OverlayNode must be added as a child of LayoutNode." }
      instance.layoutNode = parent
    }
    current.insertAt(index, instance)
  }

  override fun insertBottomUp(index: Int, instance: MapNode<*>) {
    if (DebugChanges) println("IGNORE insertBottomUp $instance at $index")
    // Ignored. 'insertTopDown' is used to use a top-down linear structure.
  }

  override fun remove(index: Int, count: Int) {
    if (DebugChanges) println("remove $count at $index")
    current.removeAt(index, count)
  }

  override fun move(from: Int, to: Int, count: Int) {
    if (DebugChanges) println("move $count from $from to $to")
    current.move(from, to, count)
  }

  override fun onClear() {
    if (DebugChanges) println("onClear")
    root.removeAll()
    layoutNode = null
  }
}
