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

package land.sungbin.navermap.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.Ref
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMapOptions
import land.sungbin.navermap.runtime.InternalNaverMapRuntimeApi
import land.sungbin.navermap.runtime.MapApplier
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors
import land.sungbin.navermap.runtime.contributor.MapViewContributor
import land.sungbin.navermap.runtime.modifier.MapModifier
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.runtime.node.LayoutNode
import land.sungbin.navermap.ui.contributor.mapLifecycle
import com.naver.maps.map.R as NaverMapR

private val NoContent: @[Composable NaverMapComposable] () -> Unit = {}

@Composable
public fun NaverMap(
  modifier: Modifier = Modifier,
  mapContentModifier: MapModifier = MapModifier,
  mapContent: @[Composable NaverMapComposable] () -> Unit = NoContent,
) {
  val context = LocalContext.current
  val parentComposer = currentComposer
  val parentRecomposeScope = currentRecomposeScope

  val map = remember { Ref<MapView>() }
  if (map.value != null) {
    AndroidView(modifier = modifier, factory = { map.value!! })
  }

  val parentCompositionContext = rememberCompositionContext()
  val compositionLocalContext = currentCompositionLocalContext

  val lifecycle = LocalLifecycleOwner.current.lifecycle
  val previousState = remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
  val savedInstanceState = rememberSaveable { Bundle() }

  val mapLayoutModifier = remember {
    MapModifier
      .then(
        MapViewInstanceInterceptorNode { instance ->
          map.value = instance
          parentRecomposeScope.invalidate()
        },
      )
      .mapLifecycle(context, lifecycle, previousState, savedInstanceState)
  }
  val mapLayoutNode = remember {
    LayoutNode(
      modifier = mapLayoutModifier then mapContentModifier,
      factory = { unwrapAppCompat(MapView(context, NaverMapOptions())) },
    )
  }

  val mapComposition = remember { Composition(MapApplier(mapLayoutNode), parent = parentCompositionContext) }

  LaunchedEffect(mapComposition) {
    @OptIn(InternalComposeApi::class, InternalNaverMapRuntimeApi::class)
    mapLayoutNode.onCompositionReleaseRequest = {
      parentComposer.recordSideEffect(mapComposition::dispose)
      map.value = null
    }
  }

  LaunchedEffect(mapContentModifier) {
    mapLayoutNode.modifier = mapLayoutModifier then mapContentModifier
  }

  mapComposition.setContent {
    CompositionLocalProvider(compositionLocalContext, content = mapContent)
  }
}

@Immutable
private class MapViewInstanceInterceptorNode(
  var onReady: ((map: MapView) -> Unit)?,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Contributors.MapView

  override fun create(): Contributor = object : MapViewContributor {
    override fun MapView.contribute() {
      onReady?.invoke(this)
      onReady = null
    }
  }

  override fun onDetach(instacne: Contributor) {
    onReady = null
  }

  override fun update(contributor: Contributor) {}

  override fun equals(other: Any?): Boolean = true
  override fun hashCode(): Int = 0
}

private fun unwrapAppCompat(map: MapView) = map.apply {
  val compassIcon = findViewById<ImageView>(NaverMapR.id.navermap_compass_icon)
  compassIcon.setImageResource(NaverMapR.drawable.navermap_compass)

  val locationIcon = findViewById<ImageView>(NaverMapR.id.navermap_location_icon)
  locationIcon.setImageResource(NaverMapR.drawable.navermap_location_none_normal)

  val zoomInIcon = findViewById<ImageView>(NaverMapR.id.navermap_zoom_in)
  zoomInIcon.setImageResource(NaverMapR.drawable.navermap_zoom_in)

  val zoomOutIcon = findViewById<ImageView>(NaverMapR.id.navermap_zoom_out)
  zoomOutIcon.setImageResource(NaverMapR.drawable.navermap_zoom_out)
}
