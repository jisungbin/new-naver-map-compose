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

package land.sungbin.navermap.ui.contributor

import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.naver.maps.map.MapView
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors
import land.sungbin.navermap.runtime.contributor.MapViewContributor
import land.sungbin.navermap.runtime.modifier.MapModifier
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode

@Stable
public fun MapModifier.mapLifecycle(
  context: Context,
  lifecycle: Lifecycle,
  previousState: MutableState<Lifecycle.Event>,
  savedInstanceState: Bundle,
): MapModifier = this then MapLifecycleContributionNode(
  context = context,
  lifecycle = lifecycle,
  previousState = previousState,
  savedInstanceState = savedInstanceState,
)

@Stable
private data class MapLifecycleContributionNode(
  private val context: Context,
  private val lifecycle: Lifecycle,
  private val previousState: MutableState<Lifecycle.Event>,
  private val savedInstanceState: Bundle,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Contributors.MapView

  override fun create(): Contributor =
    MapLifecycleContributor(
      context = context,
      lifecycle = lifecycle,
      previousState = previousState,
      savedInstanceState = savedInstanceState,
    )

  override fun update(contributor: Contributor) {
    if (contributor !is MapLifecycleContributor) error("Invalid contributor type: $contributor")
    contributor.context = context
    contributor.lifecycle = lifecycle
    contributor.previousState = previousState
    contributor.savedInstanceState = savedInstanceState
  }

  override fun onDetach(instacne: Contributor) {
    if (instacne !is MapLifecycleContributor) error("Invalid contributor type: $instacne")
    instacne.mapViewClear?.invoke()
    instacne.mapViewClear = null
  }
}

private class MapLifecycleContributor(
  var context: Context,
  var lifecycle: Lifecycle,
  var previousState: MutableState<Lifecycle.Event>,
  var savedInstanceState: Bundle,
) : MapViewContributor {
  var mapViewClear: (() -> Unit)? = null

  override fun MapView.contribute() {
    val mapLifecycleObserver = lifecycleObserver(
      savedInstanceState = savedInstanceState.takeUnless(Bundle::isEmpty),
      previousState = previousState,
    )
    val callbacks = componentCallbacks()

    lifecycle.addObserver(mapLifecycleObserver)
    context.registerComponentCallbacks(callbacks)

    mapViewClear = {
      onSaveInstanceState(savedInstanceState)
      lifecycle.removeObserver(mapLifecycleObserver)
      context.unregisterComponentCallbacks(callbacks)

      when (previousState.value) {
        Lifecycle.Event.ON_CREATE, Lifecycle.Event.ON_STOP -> {
          onDestroy()
        }
        Lifecycle.Event.ON_START, Lifecycle.Event.ON_PAUSE -> {
          onStop()
          onDestroy()
        }
        Lifecycle.Event.ON_RESUME -> {
          onPause()
          onStop()
          onDestroy()
        }
        else -> {}
      }
    }
  }
}

private fun MapView.lifecycleObserver(
  savedInstanceState: Bundle?,
  previousState: MutableState<Lifecycle.Event>,
) = LifecycleEventObserver { _, event ->
  when (event) {
    Lifecycle.Event.ON_CREATE -> onCreate(savedInstanceState)
    Lifecycle.Event.ON_START -> onStart()
    Lifecycle.Event.ON_RESUME -> onResume()
    Lifecycle.Event.ON_PAUSE -> onPause()
    Lifecycle.Event.ON_STOP -> onStop()
    Lifecycle.Event.ON_DESTROY -> onDestroy()
    else -> error("Unhandled lifecycle event: $event")
  }
  previousState.value = event
}

private fun MapView.componentCallbacks() = object : ComponentCallbacks {
  override fun onConfigurationChanged(config: Configuration) {}
  override fun onLowMemory() {
    this@componentCallbacks.onLowMemory()
  }
}
