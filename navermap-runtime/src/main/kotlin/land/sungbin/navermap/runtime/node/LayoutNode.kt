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

import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import land.sungbin.navermap.runtime.contributor.Contributors
import land.sungbin.navermap.runtime.modifier.MapModifier
import land.sungbin.navermap.runtime.modifier.MapModifierNodeChain

public class LayoutNode(
  modifier: MapModifier,
  private var factory: (() -> MapView)?,
  private var lifecycle: MapNodeLifecycleCallback = EmptyMapNodeLifecycleCallback,
) : MapNode<MapView>() {
  private val nodes = MapModifierNodeChain(supportKindSet = listOf(Contributors.MapView, Contributors.NaverMap))

  override var symbol: Symbol<MapView> = Symbol()
  internal val mapSymbol: Symbol<NaverMap> = Symbol()

  init {
    requireNotNull(factory) { "The factory argument in the LayoutNode constructor must be non-null." }
    nodes.prepareContributorsFrom(modifier)
  }

  public var modifier: MapModifier = modifier
    set(value) {
      nodes.prepareContributorsFrom(value)
      nodes.trimContributors()
      if (symbol.isBound()) nodes.contributes(symbol.owner, Contributors.MapView)
      if (mapSymbol.isBound()) nodes.contributes(mapSymbol.owner, Contributors.NaverMap)
      field = value
    }

  override fun attach() {
    val delegateIfExist = nodes.delegatorOrNull<MapView>(Contributors.NaverMap)
    if (delegateIfExist != null) symbol.bound(delegateIfExist) else symbol.bound(factory!!())
    factory = null
    lifecycle.onAttached()
    nodes.contributes(symbol.owner, Contributors.NaverMap)
    symbol.owner.followMapHost()
  }

  override fun detach() {
    symbol.unbound()
    mapSymbol.unbound()
    modifier = MapModifier // Remove all contributors
    lifecycle.onDetached()
    lifecycle = EmptyMapNodeLifecycleCallback
  }

  private fun MapView.followMapHost() = getMapAsync { host ->
    mapSymbol.bound(host)
    nodes.contributes(host, Contributors.NaverMap)
    this@LayoutNode.children.forEach { node ->
      if (node is OverlayNode<*> && !node.isAttached) node.attach()
    }
  }
}

@Suppress("NOTHING_TO_INLINE")
public inline fun LayoutNode.map(): MapView = symbol.owner

@Suppress("NOTHING_TO_INLINE")
internal inline fun LayoutNode.host(): NaverMap = mapSymbol.owner
