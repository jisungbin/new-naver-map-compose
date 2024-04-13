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

@file:Suppress("UsePropertyAccessSyntax")

package land.sungbin.navermap.ui.modifier.navermap

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationSource
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import java.util.Locale

public interface NaverMapDelegate {
  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setLocale(java.util.Locale))
   */
  public fun setLocale(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setCameraPosition(com.naver.maps.map.CameraPosition))
   */
  public fun setCameraPosition(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setDefaultCameraAnimationDuration(int))
   */
  public fun setDefaultCameraAnimationDuration(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setExtent(com.naver.maps.geometry.LatLngBounds))
   */
  public fun setExtent(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setMinZoom(double))
   */
  public fun setMinZoom(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setMaxZoom(double))
   */
  public fun setMaxZoom(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setMaxTilt(double))
   */
  public fun setMaxTilt(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setMapType(com.naver.maps.map.NaverMap.MapType))
   */
  public fun setMapType(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setLayerGroupEnabled(java.lang.String,boolean))
   */
  public fun setLayerGroupEnabled(
    instance: Any,
    arg0: Any,
    arg1: Any,
  ) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setLiteModeEnabled(boolean))
   */
  public fun setLiteModeEnabled(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setNightModeEnabled(boolean))
   */
  public fun setNightModeEnabled(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setBuildingHeight(float))
   */
  public fun setBuildingHeight(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setLightness(float))
   */
  public fun setLightness(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setSymbolScale(float))
   */
  public fun setSymbolScale(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setSymbolPerspectiveRatio(float))
   */
  public fun setSymbolPerspectiveRatio(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setIndoorEnabled(boolean))
   */
  public fun setIndoorEnabled(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setIndoorFocusRadius(int))
   */
  public fun setIndoorFocusRadius(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setBackgroundColor(int))
   */
  public fun setBackgroundColor(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setBackgroundResource(int))
   */
  public fun setBackgroundResource(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setBackground(android.graphics.drawable.Drawable))
   */
  public fun setBackground(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setBackgroundBitmap(android.graphics.Bitmap))
   */
  public fun setBackgroundBitmap(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setLocationTrackingMode(com.naver.maps.map.LocationTrackingMode))
   */
  public fun setLocationTrackingMode(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setLocationSource(com.naver.maps.map.LocationSource))
   */
  public fun setLocationSource(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setContentPadding(int,int,int,int))
   */
  public fun setContentPadding(
    instance: Any,
    arg0: Any,
    arg1: Any,
    arg2: Any,
    arg3: Any,
  ) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setContentPadding(int,int,int,int,boolean))
   */
  public fun setContentPadding(
    instance: Any,
    arg0: Any,
    arg1: Any,
    arg2: Any,
    arg3: Any,
    arg4: Any,
  ) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setFpsLimit(int))
   */
  public fun setFpsLimit(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setCameraIdlePending(boolean))
   */
  public fun setCameraIdlePending(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setOnMapClickListener(com.naver.maps.map.NaverMap.OnMapClickListener))
   */
  public fun setOnMapClickListener(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setOnMapLongClickListener(com.naver.maps.map.NaverMap.OnMapLongClickListener))
   */
  public fun setOnMapLongClickListener(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setOnMapDoubleTapListener(com.naver.maps.map.NaverMap.OnMapDoubleTapListener))
   */
  public fun setOnMapDoubleTapListener(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setOnMapTwoFingerTapListener(com.naver.maps.map.NaverMap.OnMapTwoFingerTapListener))
   */
  public fun setOnMapTwoFingerTapListener(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setOnSymbolClickListener(com.naver.maps.map.NaverMap.OnSymbolClickListener))
   */
  public fun setOnSymbolClickListener(instance: Any, arg0: Any?) {
  }

  public companion object {
    public val NoOp: NaverMapDelegate = object : NaverMapDelegate {}
  }
}

public object RealNaverMapDelegate : NaverMapDelegate {
  override fun setLocale(instance: Any, arg0: Any?) {
    require(instance is NaverMap)
    require(arg0 is Locale?)
    instance.setLocale(arg0)
  }

  override fun setCameraPosition(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is CameraPosition)
    instance.setCameraPosition(arg0)
  }

  override fun setDefaultCameraAnimationDuration(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is Int)
    instance.setDefaultCameraAnimationDuration(arg0)
  }

  override fun setExtent(instance: Any, arg0: Any?) {
    require(instance is NaverMap)
    require(arg0 is LatLngBounds?)
    instance.setExtent(arg0)
  }

  override fun setMinZoom(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is Double)
    instance.setMinZoom(arg0)
  }

  override fun setMaxZoom(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is Double)
    instance.setMaxZoom(arg0)
  }

  override fun setMaxTilt(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is Double)
    instance.setMaxTilt(arg0)
  }

  override fun setMapType(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is NaverMap.MapType)
    instance.setMapType(arg0)
  }

  override fun setLayerGroupEnabled(
    instance: Any,
    arg0: Any,
    arg1: Any,
  ) {
    require(instance is NaverMap)
    require(arg0 is String)
    require(arg1 is Boolean)
    instance.setLayerGroupEnabled(arg0, arg1)
  }

  override fun setLiteModeEnabled(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is Boolean)
    instance.setLiteModeEnabled(arg0)
  }

  override fun setNightModeEnabled(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is Boolean)
    instance.setNightModeEnabled(arg0)
  }

  override fun setBuildingHeight(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is Float)
    instance.setBuildingHeight(arg0)
  }

  override fun setLightness(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is Float)
    instance.setLightness(arg0)
  }

  override fun setSymbolScale(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is Float)
    instance.setSymbolScale(arg0)
  }

  override fun setSymbolPerspectiveRatio(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is Float)
    instance.setSymbolPerspectiveRatio(arg0)
  }

  override fun setIndoorEnabled(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is Boolean)
    instance.setIndoorEnabled(arg0)
  }

  override fun setIndoorFocusRadius(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is Int)
    instance.setIndoorFocusRadius(arg0)
  }

  override fun setBackgroundColor(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is Int)
    instance.setBackgroundColor(arg0)
  }

  override fun setBackgroundResource(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is Int)
    instance.setBackgroundResource(arg0)
  }

  override fun setBackground(instance: Any, arg0: Any?) {
    require(instance is NaverMap)
    require(arg0 is Drawable?)
    instance.setBackground(arg0)
  }

  override fun setBackgroundBitmap(instance: Any, arg0: Any?) {
    require(instance is NaverMap)
    require(arg0 is Bitmap?)
    instance.setBackgroundBitmap(arg0)
  }

  override fun setLocationTrackingMode(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is LocationTrackingMode)
    instance.setLocationTrackingMode(arg0)
  }

  override fun setLocationSource(instance: Any, arg0: Any?) {
    require(instance is NaverMap)
    require(arg0 is LocationSource?)
    instance.setLocationSource(arg0)
  }

  override fun setContentPadding(
    instance: Any,
    arg0: Any,
    arg1: Any,
    arg2: Any,
    arg3: Any,
  ) {
    require(instance is NaverMap)
    require(arg0 is Int)
    require(arg1 is Int)
    require(arg2 is Int)
    require(arg3 is Int)
    instance.setContentPadding(arg0, arg1, arg2, arg3)
  }

  override fun setContentPadding(
    instance: Any,
    arg0: Any,
    arg1: Any,
    arg2: Any,
    arg3: Any,
    arg4: Any,
  ) {
    require(instance is NaverMap)
    require(arg0 is Int)
    require(arg1 is Int)
    require(arg2 is Int)
    require(arg3 is Int)
    require(arg4 is Boolean)
    instance.setContentPadding(arg0, arg1, arg2, arg3, arg4)
  }

  override fun setFpsLimit(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is Int)
    instance.setFpsLimit(arg0)
  }

  override fun setCameraIdlePending(instance: Any, arg0: Any) {
    require(instance is NaverMap)
    require(arg0 is Boolean)
    instance.setCameraIdlePending(arg0)
  }

  override fun setOnMapClickListener(instance: Any, arg0: Any?) {
    require(instance is NaverMap)
    require(arg0 is NaverMap.OnMapClickListener?)
    instance.setOnMapClickListener(arg0)
  }

  override fun setOnMapLongClickListener(instance: Any, arg0: Any?) {
    require(instance is NaverMap)
    require(arg0 is NaverMap.OnMapLongClickListener?)
    instance.setOnMapLongClickListener(arg0)
  }

  override fun setOnMapDoubleTapListener(instance: Any, arg0: Any?) {
    require(instance is NaverMap)
    require(arg0 is NaverMap.OnMapDoubleTapListener?)
    instance.setOnMapDoubleTapListener(arg0)
  }

  override fun setOnMapTwoFingerTapListener(instance: Any, arg0: Any?) {
    require(instance is NaverMap)
    require(arg0 is NaverMap.OnMapTwoFingerTapListener?)
    instance.setOnMapTwoFingerTapListener(arg0)
  }

  override fun setOnSymbolClickListener(instance: Any, arg0: Any?) {
    require(instance is NaverMap)
    require(arg0 is NaverMap.OnSymbolClickListener?)
    instance.setOnSymbolClickListener(arg0)
  }
}
