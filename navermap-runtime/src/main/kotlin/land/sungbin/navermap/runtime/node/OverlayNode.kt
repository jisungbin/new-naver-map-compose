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

import androidx.compose.runtime.ComposeNodeLifecycleCallback
import land.sungbin.navermap.runtime.DebugChanges
import land.sungbin.navermap.runtime.InternalNaverMapRuntimeApi
import land.sungbin.navermap.runtime.contributor.Contributors
import land.sungbin.navermap.runtime.delegate.OverlayDelegator
import land.sungbin.navermap.runtime.modifier.MapModifier
import land.sungbin.navermap.runtime.modifier.MapModifierNodeChain

public typealias DelegatedOverlay = Any

public class OverlayNode(
  modifier: MapModifier = MapModifier,
  private var factory: (() -> OverlayDelegator)?,
  @InternalNaverMapRuntimeApi
  override var layoutNode: LayoutNode? = null,
  private var lifecycle: MapNodeLifecycleCallback = EmptyMapNodeLifecycleCallback,
) : MapNode<OverlayDelegator>(), MapNode.Child, ComposeNodeLifecycleCallback {
  private val nodes = MapModifierNodeChain(supportKindSet = listOf(Contributors.Overlay))
  override var isAttached: Boolean = false

  init {
    if (DebugChanges) println("OverlayNode init ($this)")
    requireNotNull(factory) { "The factory argument in the OverlayNode constructor must be non-null." }
    nodes.prepareContributorsFrom(modifier)
  }

  public var modifier: MapModifier = modifier
    set(value) {
      nodes.prepareContributorsFrom(value)
      nodes.trimContributors()
      if (delegator.isBound()) nodes.contributes(delegator.owner, Contributors.Overlay)
      field = value
    }

  override fun attachIfReady() {
    if (
      !isAttached &&
      layoutNode?.delegator?.isBound() == true &&
      layoutNode?.mapSymbol?.isBound() == true
    )
      attach()
  }

  override fun attach() {
    if (DebugChanges) println("attach $this")
    if (!delegator.isBound()) {
      val delegateIfExist = nodes.delegatorOrNull<OverlayDelegator>(Contributors.Overlay)
      delegator.bound(delegateIfExist ?: factory!!())
      delegator.owner.setMap(layoutNode().naverMap())
      factory = null
      isAttached = true
    }
    lifecycle.onAttached()
    nodes.contributes(delegator.owner, Contributors.Overlay)
  }

  override fun detach() {
    if (DebugChanges) println("detach $this")
    if (delegator.isBound()) delegator.owner.setMap(null)
    delegator.unbound()
    modifier = MapModifier // Remove all contributors
    lifecycle.onDetached()
    lifecycle = EmptyMapNodeLifecycleCallback
    layoutNode = null
  }

  override fun onReuse() {
    // TODO: Supports Overlay reusing.
    //  Overlays that are not currently visible on the map can be reused.
    //  - https://android-review.googlesource.com/c/platform/frameworks/support/+/2392879/27/compose/runtime/runtime/src/commonTest/kotlin/androidx/compose/runtime/CompositionReusingTests.kt
  }

  override fun onDeactivate() {}

  override fun onRelease() {
    detach()
  }

  @Suppress("NOTHING_TO_INLINE")
  private inline fun layoutNode() = layoutNode ?: error("LayoutNode is not ready!")
}

@Suppress("NOTHING_TO_INLINE")
public inline fun OverlayNode.overlay(): OverlayDelegator = delegator.owner
