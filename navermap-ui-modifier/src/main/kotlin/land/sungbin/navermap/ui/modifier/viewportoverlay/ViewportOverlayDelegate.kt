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

package land.sungbin.navermap.ui.modifier.viewportoverlay

import android.graphics.PointF
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.ViewportOverlay
import kotlin.Any
import kotlin.Boolean
import kotlin.Double
import kotlin.Float
import kotlin.Int
import kotlin.Suppress

public interface ViewportOverlayDelegate {
  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setGlobalZIndex(int))
   */
  public fun setGlobalZIndex(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setImage(com.naver.maps.map.overlay.OverlayImage))
   */
  public fun setImage(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setWidth(int))
   */
  public fun setWidth(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setHeight(int))
   */
  public fun setHeight(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setAnchor(android.graphics.PointF))
   */
  public fun setAnchor(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setAlign(com.naver.maps.map.overlay.Align))
   */
  public fun setAlign(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setOffsetX(int))
   */
  public fun setOffsetX(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setOffsetY(int))
   */
  public fun setOffsetY(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setAlpha(float))
   */
  public fun setAlpha(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setMap(com.naver.maps.map.NaverMap))
   */
  public fun setMap(instance: Any, arg0: Any?) {
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
    public val NoOp: ViewportOverlayDelegate = object : ViewportOverlayDelegate {}
  }
}

public object RealViewportOverlayDelegate : ViewportOverlayDelegate {
  override fun setGlobalZIndex(instance: Any, arg0: Any) {
    require(instance is ViewportOverlay)
    require(arg0 is Int)
    instance.setGlobalZIndex(arg0)
  }

  override fun setImage(instance: Any, arg0: Any) {
    require(instance is ViewportOverlay)
    require(arg0 is OverlayImage)
    instance.setImage(arg0)
  }

  override fun setWidth(instance: Any, arg0: Any) {
    require(instance is ViewportOverlay)
    require(arg0 is Int)
    instance.setWidth(arg0)
  }

  override fun setHeight(instance: Any, arg0: Any) {
    require(instance is ViewportOverlay)
    require(arg0 is Int)
    instance.setHeight(arg0)
  }

  override fun setAnchor(instance: Any, arg0: Any) {
    require(instance is ViewportOverlay)
    require(arg0 is PointF)
    instance.setAnchor(arg0)
  }

  override fun setAlign(instance: Any, arg0: Any) {
    require(instance is ViewportOverlay)
    require(arg0 is Align)
    instance.setAlign(arg0)
  }

  override fun setOffsetX(instance: Any, arg0: Any) {
    require(instance is ViewportOverlay)
    require(arg0 is Int)
    instance.setOffsetX(arg0)
  }

  override fun setOffsetY(instance: Any, arg0: Any) {
    require(instance is ViewportOverlay)
    require(arg0 is Int)
    instance.setOffsetY(arg0)
  }

  override fun setAlpha(instance: Any, arg0: Any) {
    require(instance is ViewportOverlay)
    require(arg0 is Float)
    instance.setAlpha(arg0)
  }

  override fun setMap(instance: Any, arg0: Any?) {
    require(instance is ViewportOverlay)
    require(arg0 is NaverMap?)
    instance.setMap(arg0)
  }

  override fun setOnClickListener(instance: Any, arg0: Any?) {
    require(instance is ViewportOverlay)
    require(arg0 is Overlay.OnClickListener?)
    instance.setOnClickListener(arg0)
  }

  override fun setTag(instance: Any, arg0: Any?) {
    require(instance is ViewportOverlay)
    instance.setTag(arg0)
  }

  override fun setVisible(instance: Any, arg0: Any) {
    require(instance is ViewportOverlay)
    require(arg0 is Boolean)
    instance.setVisible(arg0)
  }

  override fun setMinZoom(instance: Any, arg0: Any) {
    require(instance is ViewportOverlay)
    require(arg0 is Double)
    instance.setMinZoom(arg0)
  }

  override fun setMaxZoom(instance: Any, arg0: Any) {
    require(instance is ViewportOverlay)
    require(arg0 is Double)
    instance.setMaxZoom(arg0)
  }

  override fun setMinZoomInclusive(instance: Any, arg0: Any) {
    require(instance is ViewportOverlay)
    require(arg0 is Boolean)
    instance.setMinZoomInclusive(arg0)
  }

  override fun setMaxZoomInclusive(instance: Any, arg0: Any) {
    require(instance is ViewportOverlay)
    require(arg0 is Boolean)
    instance.setMaxZoomInclusive(arg0)
  }

  override fun setZIndex(instance: Any, arg0: Any) {
    require(instance is ViewportOverlay)
    require(arg0 is Int)
    instance.setZIndex(arg0)
  }
}
