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

package land.sungbin.navermap.ui.modifier.groundoverlay

import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.GroundOverlay
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage

public interface GroundOverlayDelegate {
  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/GroundOverlay.html#setMap(com.naver.maps.map.NaverMap))
   */
  public fun setMap(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/GroundOverlay.html#setGlobalZIndex(int))
   */
  public fun setGlobalZIndex(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/GroundOverlay.html#setBounds(com.naver.maps.geometry.LatLngBounds))
   */
  public fun setBounds(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/GroundOverlay.html#setImage(com.naver.maps.map.overlay.OverlayImage))
   */
  public fun setImage(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/GroundOverlay.html#setAlpha(float))
   */
  public fun setAlpha(instance: Any, arg0: Any) {
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
    public val NoOp: GroundOverlayDelegate = object : GroundOverlayDelegate {}
  }
}

public object RealGroundOverlayDelegate : GroundOverlayDelegate {
  override fun setMap(instance: Any, arg0: Any?) {
    require(instance is GroundOverlay)
    require(arg0 is NaverMap?)
    instance.setMap(arg0)
  }

  override fun setGlobalZIndex(instance: Any, arg0: Any) {
    require(instance is GroundOverlay)
    require(arg0 is Int)
    instance.setGlobalZIndex(arg0)
  }

  override fun setBounds(instance: Any, arg0: Any) {
    require(instance is GroundOverlay)
    require(arg0 is LatLngBounds)
    instance.setBounds(arg0)
  }

  override fun setImage(instance: Any, arg0: Any) {
    require(instance is GroundOverlay)
    require(arg0 is OverlayImage)
    instance.setImage(arg0)
  }

  override fun setAlpha(instance: Any, arg0: Any) {
    require(instance is GroundOverlay)
    require(arg0 is Float)
    instance.setAlpha(arg0)
  }

  override fun setOnClickListener(instance: Any, arg0: Any?) {
    require(instance is GroundOverlay)
    require(arg0 is Overlay.OnClickListener?)
    instance.setOnClickListener(arg0)
  }

  override fun setTag(instance: Any, arg0: Any?) {
    require(instance is GroundOverlay)
    instance.setTag(arg0)
  }

  override fun setVisible(instance: Any, arg0: Any) {
    require(instance is GroundOverlay)
    require(arg0 is Boolean)
    instance.setVisible(arg0)
  }

  override fun setMinZoom(instance: Any, arg0: Any) {
    require(instance is GroundOverlay)
    require(arg0 is Double)
    instance.setMinZoom(arg0)
  }

  override fun setMaxZoom(instance: Any, arg0: Any) {
    require(instance is GroundOverlay)
    require(arg0 is Double)
    instance.setMaxZoom(arg0)
  }

  override fun setMinZoomInclusive(instance: Any, arg0: Any) {
    require(instance is GroundOverlay)
    require(arg0 is Boolean)
    instance.setMinZoomInclusive(arg0)
  }

  override fun setMaxZoomInclusive(instance: Any, arg0: Any) {
    require(instance is GroundOverlay)
    require(arg0 is Boolean)
    instance.setMaxZoomInclusive(arg0)
  }

  override fun setZIndex(instance: Any, arg0: Any) {
    require(instance is GroundOverlay)
    require(arg0 is Int)
    instance.setZIndex(arg0)
  }
}
