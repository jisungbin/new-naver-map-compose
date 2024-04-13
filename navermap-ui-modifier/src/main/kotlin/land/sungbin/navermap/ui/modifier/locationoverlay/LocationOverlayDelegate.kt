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

package land.sungbin.navermap.ui.modifier.locationoverlay

import android.graphics.PointF
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage

public interface LocationOverlayDelegate {
  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setMap(com.naver.maps.map.NaverMap))
   */
  public fun setMap(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setGlobalZIndex(int))
   */
  public fun setGlobalZIndex(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setPosition(com.naver.maps.geometry.LatLng))
   */
  public fun setPosition(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setBearing(float))
   */
  public fun setBearing(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setIcon(com.naver.maps.map.overlay.OverlayImage))
   */
  public fun setIcon(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setIconWidth(int))
   */
  public fun setIconWidth(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setIconHeight(int))
   */
  public fun setIconHeight(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setAnchor(android.graphics.PointF))
   */
  public fun setAnchor(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setSubIcon(com.naver.maps.map.overlay.OverlayImage))
   */
  public fun setSubIcon(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setSubIconWidth(int))
   */
  public fun setSubIconWidth(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setSubIconHeight(int))
   */
  public fun setSubIconHeight(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setSubAnchor(android.graphics.PointF))
   */
  public fun setSubAnchor(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setCircleRadius(int))
   */
  public fun setCircleRadius(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setCircleColor(int))
   */
  public fun setCircleColor(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setCircleOutlineWidth(int))
   */
  public fun setCircleOutlineWidth(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/LocationOverlay.html#setCircleOutlineColor(int))
   */
  public fun setCircleOutlineColor(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setOnClickListener(com.naver.maps.map.overlay.Overlay.OnClickListener))
   */
  public fun setOnClickListener(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setTag(java.lang.Object))
   */
  public fun setTag(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setVisible(boolean))
   */
  public fun setVisible(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setMinZoom(double))
   */
  public fun setMinZoom(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setMaxZoom(double))
   */
  public fun setMaxZoom(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setMinZoomInclusive(boolean))
   */
  public fun setMinZoomInclusive(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setMaxZoomInclusive(boolean))
   */
  public fun setMaxZoomInclusive(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setZIndex(int))
   */
  public fun setZIndex(instance: Any, arg0: Any) {
  }

  public companion object {
    public val NoOp: LocationOverlayDelegate = object : LocationOverlayDelegate {}
  }
}

public object RealLocationOverlayDelegate : LocationOverlayDelegate {
  @Suppress("DEPRECATION")
  override fun setMap(instance: Any, arg0: Any?) {
    require(instance is LocationOverlay)
    require(arg0 is NaverMap?)
    instance.setMap(arg0)
  }

  override fun setGlobalZIndex(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is Int)
    instance.setGlobalZIndex(arg0)
  }

  override fun setPosition(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is LatLng)
    instance.setPosition(arg0)
  }

  override fun setBearing(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is Float)
    instance.setBearing(arg0)
  }

  override fun setIcon(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is OverlayImage)
    instance.setIcon(arg0)
  }

  override fun setIconWidth(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is Int)
    instance.setIconWidth(arg0)
  }

  override fun setIconHeight(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is Int)
    instance.setIconHeight(arg0)
  }

  override fun setAnchor(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is PointF)
    instance.setAnchor(arg0)
  }

  override fun setSubIcon(instance: Any, arg0: Any?) {
    require(instance is LocationOverlay)
    require(arg0 is OverlayImage?)
    instance.setSubIcon(arg0)
  }

  override fun setSubIconWidth(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is Int)
    instance.setSubIconWidth(arg0)
  }

  override fun setSubIconHeight(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is Int)
    instance.setSubIconHeight(arg0)
  }

  override fun setSubAnchor(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is PointF)
    instance.setSubAnchor(arg0)
  }

  override fun setCircleRadius(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is Int)
    instance.setCircleRadius(arg0)
  }

  override fun setCircleColor(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is Int)
    instance.setCircleColor(arg0)
  }

  override fun setCircleOutlineWidth(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is Int)
    instance.setCircleOutlineWidth(arg0)
  }

  override fun setCircleOutlineColor(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is Int)
    instance.setCircleOutlineColor(arg0)
  }

  override fun setOnClickListener(instance: Any, arg0: Any?) {
    require(instance is LocationOverlay)
    require(arg0 is Overlay.OnClickListener?)
    instance.setOnClickListener(arg0)
  }

  override fun setTag(instance: Any, arg0: Any?) {
    require(instance is LocationOverlay)
    instance.setTag(arg0)
  }

  override fun setVisible(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is Boolean)
    instance.setVisible(arg0)
  }

  override fun setMinZoom(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is Double)
    instance.setMinZoom(arg0)
  }

  override fun setMaxZoom(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is Double)
    instance.setMaxZoom(arg0)
  }

  override fun setMinZoomInclusive(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is Boolean)
    instance.setMinZoomInclusive(arg0)
  }

  override fun setMaxZoomInclusive(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is Boolean)
    instance.setMaxZoomInclusive(arg0)
  }

  override fun setZIndex(instance: Any, arg0: Any) {
    require(instance is LocationOverlay)
    require(arg0 is Int)
    instance.setZIndex(arg0)
  }
}
