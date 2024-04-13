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

package land.sungbin.navermap.ui.modifier.polylineoverlay

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.PolylineOverlay

public interface PolylineOverlayDelegate {
  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PolylineOverlay.html#setMap(com.naver.maps.map.NaverMap))
   */
  public fun setMap(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PolylineOverlay.html#setGlobalZIndex(int))
   */
  public fun setGlobalZIndex(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PolylineOverlay.html#setCoords(java.util.List))
   */
  public fun setCoords(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PolylineOverlay.html#setWidth(int))
   */
  public fun setWidth(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PolylineOverlay.html#setColor(int))
   */
  public fun setColor(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PolylineOverlay.html#setPattern(int...))
   */
  public fun setPattern(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PolylineOverlay.html#setCapType(com.naver.maps.map.overlay.PolylineOverlay.LineCap))
   */
  public fun setCapType(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PolylineOverlay.html#setJoinType(com.naver.maps.map.overlay.PolylineOverlay.LineJoin))
   */
  public fun setJoinType(instance: Any, arg0: Any) {
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
    public val NoOp: PolylineOverlayDelegate = object : PolylineOverlayDelegate {}
  }
}

public object RealPolylineOverlayDelegate : PolylineOverlayDelegate {
  override fun setMap(instance: Any, arg0: Any?) {
    require(instance is PolylineOverlay)
    require(arg0 is NaverMap?)
    instance.setMap(arg0)
  }

  override fun setGlobalZIndex(instance: Any, arg0: Any) {
    require(instance is PolylineOverlay)
    require(arg0 is Int)
    instance.setGlobalZIndex(arg0)
  }

  @Suppress("CANNOT_CHECK_FOR_ERASED")
  override fun setCoords(instance: Any, arg0: Any) {
    require(instance is PolylineOverlay)
    require(arg0 is List<LatLng>)
    instance.setCoords(arg0)
  }

  override fun setWidth(instance: Any, arg0: Any) {
    require(instance is PolylineOverlay)
    require(arg0 is Int)
    instance.setWidth(arg0)
  }

  override fun setColor(instance: Any, arg0: Any) {
    require(instance is PolylineOverlay)
    require(arg0 is Int)
    instance.setColor(arg0)
  }

  override fun setPattern(instance: Any, arg0: Any) {
    require(instance is PolylineOverlay)
    require(arg0 is IntArray)
    instance.setPattern(*arg0)
  }

  override fun setCapType(instance: Any, arg0: Any) {
    require(instance is PolylineOverlay)
    require(arg0 is PolylineOverlay.LineCap)
    instance.setCapType(arg0)
  }

  override fun setJoinType(instance: Any, arg0: Any) {
    require(instance is PolylineOverlay)
    require(arg0 is PolylineOverlay.LineJoin)
    instance.setJoinType(arg0)
  }

  override fun setOnClickListener(instance: Any, arg0: Any?) {
    require(instance is PolylineOverlay)
    require(arg0 is Overlay.OnClickListener?)
    instance.setOnClickListener(arg0)
  }

  override fun setTag(instance: Any, arg0: Any?) {
    require(instance is PolylineOverlay)
    instance.setTag(arg0)
  }

  override fun setVisible(instance: Any, arg0: Any) {
    require(instance is PolylineOverlay)
    require(arg0 is Boolean)
    instance.setVisible(arg0)
  }

  override fun setMinZoom(instance: Any, arg0: Any) {
    require(instance is PolylineOverlay)
    require(arg0 is Double)
    instance.setMinZoom(arg0)
  }

  override fun setMaxZoom(instance: Any, arg0: Any) {
    require(instance is PolylineOverlay)
    require(arg0 is Double)
    instance.setMaxZoom(arg0)
  }

  override fun setMinZoomInclusive(instance: Any, arg0: Any) {
    require(instance is PolylineOverlay)
    require(arg0 is Boolean)
    instance.setMinZoomInclusive(arg0)
  }

  override fun setMaxZoomInclusive(instance: Any, arg0: Any) {
    require(instance is PolylineOverlay)
    require(arg0 is Boolean)
    instance.setMaxZoomInclusive(arg0)
  }

  override fun setZIndex(instance: Any, arg0: Any) {
    require(instance is PolylineOverlay)
    require(arg0 is Int)
    instance.setZIndex(arg0)
  }
}
