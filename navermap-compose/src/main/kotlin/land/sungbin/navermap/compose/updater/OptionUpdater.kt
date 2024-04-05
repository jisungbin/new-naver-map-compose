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

package land.sungbin.navermap.compose.updater

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReusableComposeNode
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import com.naver.maps.map.LocationSource
import com.naver.maps.map.NaverMap
import java.util.Locale
import land.sungbin.navermap.compose.NaverMapComposable
import land.sungbin.navermap.compose.mapApplier
import land.sungbin.navermap.compose.options.CameraPositionState
import land.sungbin.navermap.compose.options.MapClickListeners
import land.sungbin.navermap.compose.options.MapProperties
import land.sungbin.navermap.compose.options.MapUiSettings
import land.sungbin.navermap.compose.runtime.MapApplier
import land.sungbin.navermap.compose.runtime.MapPropertiesNode
import land.sungbin.navermap.compose.runtime.requireMap

internal val NoPadding = PaddingValues()

/**
 * Used to keep the primary map properties up to date. This should never leave
 * the map composition.
 */
@Suppress("NOTHING_TO_INLINE")
@Composable
@NaverMapComposable
internal inline fun MapOptionUpdater(
  cameraPositionState: CameraPositionState,
  clickListeners: MapClickListeners,
  contentPadding: PaddingValues = NoPadding,
  locationSource: LocationSource?,
  locale: Locale? = null,
  mapProperties: MapProperties,
  mapUiSettings: MapUiSettings,
) {
  val map = currentComposer.mapApplier.root.requireMap()
  val density = LocalDensity.current
  val layoutDirection = LocalLayoutDirection.current

  ReusableComposeNode<MapPropertiesNode, MapApplier>(
    factory = {
      MapPropertiesNode(
        map = map,
        cameraPositionState = cameraPositionState,
        clickListeners = clickListeners,
        density = density,
        layoutDirection = layoutDirection,
      )
    },
  ) {
    // The node holds density and layoutDirection so that the updater blocks can be
    // non-capturing, allowing the compiler to turn them into singletons
    update(density) { this.density = it }
    update(layoutDirection) { this.layoutDirection = it }
    set(locale) { map.locale = it }

    set(mapProperties.extent) { map.extent = it }
    set(mapProperties.minZoom) { map.minZoom = it }
    set(mapProperties.maxZoom) { map.maxZoom = it }
    set(mapProperties.maxTilt) { map.maxTilt = it }
    set(contentPadding) {
      val node = this
      with(this.density) {
        map.setContentPadding(
          it.calculateLeftPadding(node.layoutDirection).roundToPx(),
          it.calculateTopPadding().roundToPx(),
          it.calculateRightPadding(node.layoutDirection).roundToPx(),
          it.calculateBottomPadding().roundToPx(),
        )
      }
    }
    set(mapProperties.defaultCameraAnimationDuration) {
      map.defaultCameraAnimationDuration = it
    }
    update(cameraPositionState) { this.cameraPositionState = it }

    set(mapUiSettings.pickTolerance) {
      with(this.density) {
        map.uiSettings.pickTolerance = it.roundToPx()
      }
    }
    set(mapUiSettings.isScrollGesturesEnabled) { map.uiSettings.isScrollGesturesEnabled = it }
    set(mapUiSettings.isZoomGesturesEnabled) { map.uiSettings.isZoomGesturesEnabled = it }
    set(mapUiSettings.isTiltGesturesEnabled) { map.uiSettings.isTiltGesturesEnabled = it }
    set(mapUiSettings.isRotateGesturesEnabled) { map.uiSettings.isRotateGesturesEnabled = it }
    set(mapUiSettings.isStopGesturesEnabled) { map.uiSettings.isStopGesturesEnabled = it }
    set(mapUiSettings.scrollGesturesFriction) { map.uiSettings.scrollGesturesFriction = it }
    set(mapUiSettings.zoomGesturesFriction) { map.uiSettings.zoomGesturesFriction = it }
    set(mapUiSettings.rotateGesturesFriction) { map.uiSettings.rotateGesturesFriction = it }
    set(mapUiSettings.isCompassEnabled) { map.uiSettings.isCompassEnabled = it }
    set(mapUiSettings.isScaleBarEnabled) { map.uiSettings.isScaleBarEnabled = it }
    set(mapUiSettings.isZoomControlEnabled) { map.uiSettings.isZoomControlEnabled = it }
    set(mapUiSettings.isIndoorLevelPickerEnabled) {
      map.uiSettings.isIndoorLevelPickerEnabled = it
    }
    set(mapUiSettings.isLocationButtonEnabled) { map.uiSettings.isLocationButtonEnabled = it }
    set(mapUiSettings.isLogoClickEnabled) { map.uiSettings.isLogoClickEnabled = it }
    set(mapUiSettings.logoGravity) { map.uiSettings.logoGravity = it }
    set(mapUiSettings.logoMargin) {
      val node = this
      with(this.density) {
        map.uiSettings.setLogoMargin(
          it.calculateLeftPadding(node.layoutDirection).roundToPx(),
          it.calculateTopPadding().roundToPx(),
          it.calculateRightPadding(node.layoutDirection).roundToPx(),
          it.calculateBottomPadding().roundToPx(),
        )
      }
    }
    set(mapProperties.fpsLimit) { map.fpsLimit = it }

    set(mapProperties.isIndoorEnabled) { map.isIndoorEnabled = it }
    set(mapProperties.mapType) { map.mapType = it.value }
    set(mapProperties.isBuildingLayerGroupEnabled) {
      map.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, it)
    }
    set(mapProperties.isTransitLayerGroupEnabled) {
      map.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, it)
    }
    set(mapProperties.isBicycleLayerGroupEnabled) {
      map.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, it)
    }
    set(mapProperties.isTrafficLayerGroupEnabled) {
      map.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, it)
    }
    set(mapProperties.isCadastralLayerGroupEnabled) {
      map.setLayerGroupEnabled(NaverMap.LAYER_GROUP_CADASTRAL, it)
    }
    set(mapProperties.isMountainLayerGroupEnabled) {
      map.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, it)
    }
    set(mapProperties.isLiteModeEnabled) { map.isLiteModeEnabled = it }
    set(mapProperties.isNightModeEnabled) { map.isNightModeEnabled = it }
    set(mapProperties.buildingHeight) { map.buildingHeight = it }
    set(mapProperties.lightness) { map.lightness = it }
    set(mapProperties.symbolScale) { map.symbolScale = it }
    set(mapProperties.symbolPerspectiveRatio) { map.symbolPerspectiveRatio = it }
    set(mapProperties.indoorFocusRadius) {
      with(this.density) {
        map.indoorFocusRadius = it.roundToPx()
      }
    }
    set(mapProperties.backgroundColor) { map.backgroundColor = it.toArgb() }
    set(mapProperties.backgroundResource) { map.setBackgroundResource(it) }
    set(locationSource) { map.locationSource = it }
    set(mapProperties.locationTrackingMode) { map.locationTrackingMode = it.value }

    update(clickListeners) { this.clickListeners = it }
  }
}
