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

public interface ViewportOverlayDelegate {
  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setGlobalZIndex(int))
   */
  public fun setGlobalZIndex(instance: Any, arg0: Int) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setImage(com.naver.maps.map.overlay.OverlayImage))
   */
  public fun setImage(instance: Any, arg0: OverlayImage) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setWidth(int))
   */
  public fun setWidth(instance: Any, arg0: Int) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setHeight(int))
   */
  public fun setHeight(instance: Any, arg0: Int) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setAnchor(android.graphics.PointF))
   */
  public fun setAnchor(instance: Any, arg0: PointF) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setAlign(com.naver.maps.map.overlay.Align))
   */
  public fun setAlign(instance: Any, arg0: Align) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setOffsetX(int))
   */
  public fun setOffsetX(instance: Any, arg0: Int) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setOffsetY(int))
   */
  public fun setOffsetY(instance: Any, arg0: Int) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/ViewportOverlay.html#setAlpha(float))
   */
  public fun setAlpha(instance: Any, arg0: Float) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setMap(com.naver.maps.map.NaverMap))
   */
  public fun setMap(instance: Any, arg0: NaverMap?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setOnClickListener(com.naver.maps.map.overlay.Overlay.OnClickListener))
   */
  public fun setOnClickListener(instance: Any, arg0: Overlay.OnClickListener?) {
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
  public fun setVisible(instance: Any, arg0: Boolean) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setMinZoom(double))
   */
  public fun setMinZoom(instance: Any, arg0: Double) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setMaxZoom(double))
   */
  public fun setMaxZoom(instance: Any, arg0: Double) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setMinZoomInclusive(boolean))
   */
  public fun setMinZoomInclusive(instance: Any, arg0: Boolean) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setMaxZoomInclusive(boolean))
   */
  public fun setMaxZoomInclusive(instance: Any, arg0: Boolean) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setZIndex(int))
   */
  public fun setZIndex(instance: Any, arg0: Int) {
  }

  public companion object {
    public val NoOp: ViewportOverlayDelegate = object : ViewportOverlayDelegate {}
  }
}

public object RealViewportOverlayDelegate : ViewportOverlayDelegate {
  override fun setGlobalZIndex(instance: Any, arg0: Int) {
    require(instance is ViewportOverlay)
    instance.setGlobalZIndex(arg0)
  }

  override fun setImage(instance: Any, arg0: OverlayImage) {
    require(instance is ViewportOverlay)
    instance.setImage(arg0)
  }

  override fun setWidth(instance: Any, arg0: Int) {
    require(instance is ViewportOverlay)
    instance.setWidth(arg0)
  }

  override fun setHeight(instance: Any, arg0: Int) {
    require(instance is ViewportOverlay)
    instance.setHeight(arg0)
  }

  override fun setAnchor(instance: Any, arg0: PointF) {
    require(instance is ViewportOverlay)
    instance.setAnchor(arg0)
  }

  override fun setAlign(instance: Any, arg0: Align) {
    require(instance is ViewportOverlay)
    instance.setAlign(arg0)
  }

  override fun setOffsetX(instance: Any, arg0: Int) {
    require(instance is ViewportOverlay)
    instance.setOffsetX(arg0)
  }

  override fun setOffsetY(instance: Any, arg0: Int) {
    require(instance is ViewportOverlay)
    instance.setOffsetY(arg0)
  }

  override fun setAlpha(instance: Any, arg0: Float) {
    require(instance is ViewportOverlay)
    instance.setAlpha(arg0)
  }

  override fun setMap(instance: Any, arg0: NaverMap?) {
    require(instance is ViewportOverlay)
    instance.setMap(arg0)
  }

  override fun setOnClickListener(instance: Any, arg0: Overlay.OnClickListener?) {
    require(instance is ViewportOverlay)
    instance.setOnClickListener(arg0)
  }

  override fun setTag(instance: Any, arg0: Any?) {
    require(instance is ViewportOverlay)
    instance.setTag(arg0)
  }

  override fun setVisible(instance: Any, arg0: Boolean) {
    require(instance is ViewportOverlay)
    instance.setVisible(arg0)
  }

  override fun setMinZoom(instance: Any, arg0: Double) {
    require(instance is ViewportOverlay)
    instance.setMinZoom(arg0)
  }

  override fun setMaxZoom(instance: Any, arg0: Double) {
    require(instance is ViewportOverlay)
    instance.setMaxZoom(arg0)
  }

  override fun setMinZoomInclusive(instance: Any, arg0: Boolean) {
    require(instance is ViewportOverlay)
    instance.setMinZoomInclusive(arg0)
  }

  override fun setMaxZoomInclusive(instance: Any, arg0: Boolean) {
    require(instance is ViewportOverlay)
    instance.setMaxZoomInclusive(arg0)
  }

  override fun setZIndex(instance: Any, arg0: Int) {
    require(instance is ViewportOverlay)
    instance.setZIndex(arg0)
  }
}
