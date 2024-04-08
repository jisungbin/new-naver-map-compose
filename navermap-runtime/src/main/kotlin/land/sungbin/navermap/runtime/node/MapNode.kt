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

import androidx.annotation.CallSuper
import androidx.compose.runtime.ComposeNodeLifecycleCallback
import androidx.compose.runtime.collection.MutableVector
import androidx.compose.runtime.collection.mutableVectorOf
import land.sungbin.navermap.runtime.InternalNaverMapRuntimeApi
import land.sungbin.navermap.runtime.delegate.Delegator
import org.jetbrains.annotations.ApiStatus.OverrideOnly

public abstract class MapNode<D : Delegator>(
  @InternalNaverMapRuntimeApi // Called when the node is removed from the composition
  public var onCompositionReleaseRequest: (() -> Unit)? = null,
) : ComposeNodeLifecycleCallback {
  protected val children: MutableVector<MapNode<*>> = mutableVectorOf()

  @InternalNaverMapRuntimeApi
  public val delegator: Symbol<D> = Symbol()

  public abstract fun attach()

  // OverrideOnly: Auto call when the node is removed from the composition
  @OverrideOnly
  @InternalNaverMapRuntimeApi
  public abstract fun detach()

  public fun insertAt(index: Int, instance: MapNode<*>) {
    children.add(index, instance)
  }

  public fun removeAt(index: Int, count: Int) {
    require(count >= 0) { "count ($count) must be greater than 0" }
    for (i in index + count - 1 downTo index) {
      children.removeAt(i)
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
      children.removeAt(i)
    }
  }

  override fun onReuse() {
    // Nothing to do
  }

  override fun onDeactivate() {
    // Nothing to do
  }

  @CallSuper
  override fun onRelease() {
    children.forEach(MapNode<*>::detach)
    val onCompositionReleaseRequest = onCompositionReleaseRequest
    // Only root node has onCompositionReleaseRequest callback
    if (onCompositionReleaseRequest != null) {
      onCompositionReleaseRequest.invoke()
      this.onCompositionReleaseRequest = null
    }
  }
}
