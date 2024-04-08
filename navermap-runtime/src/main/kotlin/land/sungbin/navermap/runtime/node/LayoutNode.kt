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

import land.sungbin.navermap.runtime.contributor.Contributors
import land.sungbin.navermap.runtime.delegate.MapViewDelegator
import land.sungbin.navermap.runtime.delegate.NaverMapDelegator
import land.sungbin.navermap.runtime.modifier.MapModifier
import land.sungbin.navermap.runtime.modifier.MapModifierNodeChain

public typealias DelegatedMapView = Any
public typealias DelegatedNaverMap = Any

public class LayoutNode(
  modifier: MapModifier,
  private var factory: (() -> MapViewDelegator)?,
  private var lifecycle: MapNodeLifecycleCallback = EmptyMapNodeLifecycleCallback,
) : MapNode<MapViewDelegator>() {
  private val nodes = MapModifierNodeChain(supportKindSet = listOf(Contributors.MapView, Contributors.NaverMap))
  internal val mapSymbol: Symbol<NaverMapDelegator> = Symbol()

  init {
    requireNotNull(factory) { "The factory argument in the LayoutNode constructor must be non-null." }
    nodes.prepareContributorsFrom(modifier)
  }

  public var modifier: MapModifier = modifier
    set(value) {
      nodes.prepareContributorsFrom(value)
      nodes.trimContributors()
      if (delegator.isBound()) nodes.contributes(delegator.owner, Contributors.MapView)
      if (mapSymbol.isBound()) nodes.contributes(mapSymbol.owner, Contributors.NaverMap)
      field = value
    }

  override fun attach() {
    val delegatorIfExist = nodes.delegatorOrNull<MapViewDelegator>(Contributors.MapView)
    delegator.bound(delegatorIfExist ?: factory!!())
    factory = null
    lifecycle.onAttached()
    nodes.contributes(delegator.owner, Contributors.NaverMap)
    delegator.owner.followNaverMap()
  }

  override fun detach() {
    delegator.unbound()
    mapSymbol.unbound()
    modifier = MapModifier // Remove all contributors
    lifecycle.onDetached()
    lifecycle = EmptyMapNodeLifecycleCallback
  }

  private fun MapViewDelegator.followNaverMap() = getMapAsync { map ->
    mapSymbol.bound(map)
    nodes.contributes(map, Contributors.NaverMap)
    this@LayoutNode.children.forEach { node ->
      if (node is OverlayNode && !node.isAttached) node.attach()
    }
  }
}

@Suppress("NOTHING_TO_INLINE")
public inline fun LayoutNode.mapView(): MapViewDelegator = delegator.owner

@Suppress("NOTHING_TO_INLINE")
internal inline fun LayoutNode.naverMap(): NaverMapDelegator = mapSymbol.owner
