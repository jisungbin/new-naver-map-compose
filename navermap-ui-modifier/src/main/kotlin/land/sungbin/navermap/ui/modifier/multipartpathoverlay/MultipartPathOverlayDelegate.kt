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

package land.sungbin.navermap.ui.modifier.multipartpathoverlay

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.MultipartPathOverlay
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage

public interface MultipartPathOverlayDelegate {
  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/MultipartPathOverlay.html#setMap(com.naver.maps.map.NaverMap))
   */
  public fun setMap(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/MultipartPathOverlay.html#setGlobalZIndex(int))
   */
  public fun setGlobalZIndex(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/MultipartPathOverlay.html#setCoordParts(java.util.List))
   */
  public fun setCoordParts(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/MultipartPathOverlay.html#setColorParts(java.util.List))
   */
  public fun setColorParts(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/MultipartPathOverlay.html#setProgress(double))
   */
  public fun setProgress(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/MultipartPathOverlay.html#setWidth(int))
   */
  public fun setWidth(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/MultipartPathOverlay.html#setOutlineWidth(int))
   */
  public fun setOutlineWidth(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/MultipartPathOverlay.html#setPatternImage(com.naver.maps.map.overlay.OverlayImage))
   */
  public fun setPatternImage(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/MultipartPathOverlay.html#setPatternInterval(int))
   */
  public fun setPatternInterval(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/MultipartPathOverlay.html#setHideCollidedSymbols(boolean))
   */
  public fun setHideCollidedSymbols(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/MultipartPathOverlay.html#setHideCollidedMarkers(boolean))
   */
  public fun setHideCollidedMarkers(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/MultipartPathOverlay.html#setHideCollidedCaptions(boolean))
   */
  public fun setHideCollidedCaptions(instance: Any, arg0: Any) {
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
    public val NoOp: MultipartPathOverlayDelegate = object : MultipartPathOverlayDelegate {}
  }
}

public object RealMultipartPathOverlayDelegate : MultipartPathOverlayDelegate {
  override fun setMap(instance: Any, arg0: Any?) {
    require(instance is MultipartPathOverlay)
    require(arg0 is NaverMap?)
    instance.setMap(arg0)
  }

  override fun setGlobalZIndex(instance: Any, arg0: Any) {
    require(instance is MultipartPathOverlay)
    require(arg0 is Int)
    instance.setGlobalZIndex(arg0)
  }

  @Suppress("CANNOT_CHECK_FOR_ERASED")
  override fun setCoordParts(instance: Any, arg0: Any) {
    require(instance is MultipartPathOverlay)
    require(arg0 is List<List<LatLng>>)
    instance.setCoordParts(arg0)
  }

  @Suppress("CANNOT_CHECK_FOR_ERASED")
  override fun setColorParts(instance: Any, arg0: Any) {
    require(instance is MultipartPathOverlay)
    require(arg0 is List<MultipartPathOverlay.ColorPart>)
    instance.setColorParts(arg0)
  }

  override fun setProgress(instance: Any, arg0: Any) {
    require(instance is MultipartPathOverlay)
    require(arg0 is Double)
    instance.setProgress(arg0)
  }

  override fun setWidth(instance: Any, arg0: Any) {
    require(instance is MultipartPathOverlay)
    require(arg0 is Int)
    instance.setWidth(arg0)
  }

  override fun setOutlineWidth(instance: Any, arg0: Any) {
    require(instance is MultipartPathOverlay)
    require(arg0 is Int)
    instance.setOutlineWidth(arg0)
  }

  override fun setPatternImage(instance: Any, arg0: Any?) {
    require(instance is MultipartPathOverlay)
    require(arg0 is OverlayImage?)
    instance.setPatternImage(arg0)
  }

  override fun setPatternInterval(instance: Any, arg0: Any) {
    require(instance is MultipartPathOverlay)
    require(arg0 is Int)
    instance.setPatternInterval(arg0)
  }

  override fun setHideCollidedSymbols(instance: Any, arg0: Any) {
    require(instance is MultipartPathOverlay)
    require(arg0 is Boolean)
    instance.setHideCollidedSymbols(arg0)
  }

  override fun setHideCollidedMarkers(instance: Any, arg0: Any) {
    require(instance is MultipartPathOverlay)
    require(arg0 is Boolean)
    instance.setHideCollidedMarkers(arg0)
  }

  override fun setHideCollidedCaptions(instance: Any, arg0: Any) {
    require(instance is MultipartPathOverlay)
    require(arg0 is Boolean)
    instance.setHideCollidedCaptions(arg0)
  }

  override fun setOnClickListener(instance: Any, arg0: Any?) {
    require(instance is MultipartPathOverlay)
    require(arg0 is Overlay.OnClickListener?)
    instance.setOnClickListener(arg0)
  }

  override fun setTag(instance: Any, arg0: Any?) {
    require(instance is MultipartPathOverlay)
    instance.setTag(arg0)
  }

  override fun setVisible(instance: Any, arg0: Any) {
    require(instance is MultipartPathOverlay)
    require(arg0 is Boolean)
    instance.setVisible(arg0)
  }

  override fun setMinZoom(instance: Any, arg0: Any) {
    require(instance is MultipartPathOverlay)
    require(arg0 is Double)
    instance.setMinZoom(arg0)
  }

  override fun setMaxZoom(instance: Any, arg0: Any) {
    require(instance is MultipartPathOverlay)
    require(arg0 is Double)
    instance.setMaxZoom(arg0)
  }

  override fun setMinZoomInclusive(instance: Any, arg0: Any) {
    require(instance is MultipartPathOverlay)
    require(arg0 is Boolean)
    instance.setMinZoomInclusive(arg0)
  }

  override fun setMaxZoomInclusive(instance: Any, arg0: Any) {
    require(instance is MultipartPathOverlay)
    require(arg0 is Boolean)
    instance.setMaxZoomInclusive(arg0)
  }

  override fun setZIndex(instance: Any, arg0: Any) {
    require(instance is MultipartPathOverlay)
    require(arg0 is Int)
    instance.setZIndex(arg0)
  }
}
