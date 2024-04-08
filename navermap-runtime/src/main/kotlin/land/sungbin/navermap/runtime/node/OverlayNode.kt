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

import com.naver.maps.map.overlay.Overlay
import land.sungbin.navermap.runtime.contributor.Contributors
import land.sungbin.navermap.runtime.modifier.MapModifier
import land.sungbin.navermap.runtime.modifier.MapModifierNodeChain
import land.sungbin.navermap.token.OverlayFactory

public class OverlayNode<O : Overlay>(
  modifier: MapModifier,
  private var factory: OverlayFactory<O>?,
  private var layoutNode: LayoutNode? = null,
  private var lifecycle: MapNodeLifecycleCallback = EmptyMapNodeLifecycleCallback,
) : MapNode<O>() {
  private val nodes = MapModifierNodeChain(supportKindSet = listOf(Contributors.Overlay))

  internal var isAttached: Boolean = false
    private set

  init {
    requireNotNull(factory) { "The factory argument in the OverlayNode constructor must be non-null." }
    nodes.prepareContributorsFrom(modifier)
    if (layoutNode().mapSymbol.isBound()) attach()
  }

  public var modifier: MapModifier = modifier
    set(value) {
      nodes.prepareContributorsFrom(value)
      nodes.trimContributors()
      if (symbol.isBound()) nodes.contributes(symbol.owner, Contributors.Overlay)
      field = value
    }

  override fun attach() {
    val delegateIfExist = nodes.delegatorOrNull<Overlay>(Contributors.Overlay)
    @Suppress("UNCHECKED_CAST")
    if (delegateIfExist != null) symbol.bound(delegateIfExist as O) else symbol.bound(factory!!.createOverlay())
    factory = null
    symbol.owner.map = layoutNode().naverMap()
    lifecycle.onAttached()
    nodes.contributes(symbol.owner, Contributors.Overlay)
    isAttached = true
  }

  override fun detach() {
    symbol.unbound()
    modifier = MapModifier // Remove all contributors
    symbol.owner.map = null
    lifecycle.onDetached()
    lifecycle = EmptyMapNodeLifecycleCallback
    layoutNode = null
  }

  @Suppress("NOTHING_TO_INLINE")
  private inline fun layoutNode() = layoutNode ?: error("LayoutNode is not ready!")
}

@Suppress("NOTHING_TO_INLINE")
public inline fun <O : Overlay> OverlayNode<O>.overlay(): O = symbol.owner
