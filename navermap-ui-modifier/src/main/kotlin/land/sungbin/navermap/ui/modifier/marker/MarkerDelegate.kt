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

package land.sungbin.navermap.ui.modifier.marker

import android.graphics.PointF
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage

public interface MarkerDelegate {
  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setMap(com.naver.maps.map.NaverMap))
   */
  public fun setMap(instance: Any, arg0: Any?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setGlobalZIndex(int))
   */
  public fun setGlobalZIndex(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setPosition(com.naver.maps.geometry.LatLng))
   */
  public fun setPosition(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setIcon(com.naver.maps.map.overlay.OverlayImage))
   */
  public fun setIcon(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setIconTintColor(int))
   */
  public fun setIconTintColor(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setWidth(int))
   */
  public fun setWidth(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setHeight(int))
   */
  public fun setHeight(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setAnchor(android.graphics.PointF))
   */
  public fun setAnchor(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setCaptionText(java.lang.String))
   */
  public fun setCaptionText(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setCaptionTextSize(float))
   */
  public fun setCaptionTextSize(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setCaptionColor(int))
   */
  public fun setCaptionColor(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setCaptionHaloColor(int))
   */
  public fun setCaptionHaloColor(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setCaptionRequestedWidth(int))
   */
  public fun setCaptionRequestedWidth(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setCaptionMinZoom(double))
   */
  public fun setCaptionMinZoom(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setCaptionMaxZoom(double))
   */
  public fun setCaptionMaxZoom(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setSubCaptionText(java.lang.String))
   */
  public fun setSubCaptionText(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setSubCaptionTextSize(float))
   */
  public fun setSubCaptionTextSize(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setSubCaptionColor(int))
   */
  public fun setSubCaptionColor(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setSubCaptionHaloColor(int))
   */
  public fun setSubCaptionHaloColor(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setSubCaptionFontFamily(java.lang.String...))
   */
  public fun setSubCaptionFontFamily(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setSubCaptionRequestedWidth(int))
   */
  public fun setSubCaptionRequestedWidth(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setSubCaptionMinZoom(double))
   */
  public fun setSubCaptionMinZoom(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setSubCaptionMaxZoom(double))
   */
  public fun setSubCaptionMaxZoom(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setCaptionAlign(com.naver.maps.map.overlay.Align))
   */
  public fun setCaptionAlign(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setCaptionAligns(com.naver.maps.map.overlay.Align...))
   */
  public fun setCaptionAligns(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setCaptionOffset(int))
   */
  public fun setCaptionOffset(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setAlpha(float))
   */
  public fun setAlpha(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setAngle(float))
   */
  public fun setAngle(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setFlat(boolean))
   */
  public fun setFlat(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setHideCollidedSymbols(boolean))
   */
  public fun setHideCollidedSymbols(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setHideCollidedMarkers(boolean))
   */
  public fun setHideCollidedMarkers(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setHideCollidedCaptions(boolean))
   */
  public fun setHideCollidedCaptions(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setForceShowIcon(boolean))
   */
  public fun setForceShowIcon(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setForceShowCaption(boolean))
   */
  public fun setForceShowCaption(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setOccupySpaceOnCollision(boolean))
   */
  public fun setOccupySpaceOnCollision(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setIconPerspectiveEnabled(boolean))
   */
  public fun setIconPerspectiveEnabled(instance: Any, arg0: Any) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setCaptionPerspectiveEnabled(boolean))
   */
  public fun setCaptionPerspectiveEnabled(instance: Any, arg0: Any) {
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
    public val NoOp: MarkerDelegate = object : MarkerDelegate {}
  }
}

public object RealMarkerDelegate : MarkerDelegate {
  override fun setMap(instance: Any, arg0: Any?) {
    require(instance is Marker)
    require(arg0 is NaverMap?)
    instance.setMap(arg0)
  }

  override fun setGlobalZIndex(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Int)
    instance.setGlobalZIndex(arg0)
  }

  override fun setPosition(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is LatLng)
    instance.setPosition(arg0)
  }

  override fun setIcon(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is OverlayImage)
    instance.setIcon(arg0)
  }

  override fun setIconTintColor(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Int)
    instance.setIconTintColor(arg0)
  }

  override fun setWidth(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Int)
    instance.setWidth(arg0)
  }

  override fun setHeight(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Int)
    instance.setHeight(arg0)
  }

  override fun setAnchor(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is PointF)
    instance.setAnchor(arg0)
  }

  override fun setCaptionText(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is String)
    instance.setCaptionText(arg0)
  }

  override fun setCaptionTextSize(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Float)
    instance.setCaptionTextSize(arg0)
  }

  override fun setCaptionColor(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Int)
    instance.setCaptionColor(arg0)
  }

  override fun setCaptionHaloColor(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Int)
    instance.setCaptionHaloColor(arg0)
  }

  override fun setCaptionRequestedWidth(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Int)
    instance.setCaptionRequestedWidth(arg0)
  }

  override fun setCaptionMinZoom(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Double)
    instance.setCaptionMinZoom(arg0)
  }

  override fun setCaptionMaxZoom(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Double)
    instance.setCaptionMaxZoom(arg0)
  }

  override fun setSubCaptionText(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is String)
    instance.setSubCaptionText(arg0)
  }

  override fun setSubCaptionTextSize(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Float)
    instance.setSubCaptionTextSize(arg0)
  }

  override fun setSubCaptionColor(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Int)
    instance.setSubCaptionColor(arg0)
  }

  override fun setSubCaptionHaloColor(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Int)
    instance.setSubCaptionHaloColor(arg0)
  }

  @Suppress("CANNOT_CHECK_FOR_ERASED")
  override fun setSubCaptionFontFamily(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Array<String>)
    instance.setSubCaptionFontFamily(*arg0)
  }

  override fun setSubCaptionRequestedWidth(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Int)
    instance.setSubCaptionRequestedWidth(arg0)
  }

  override fun setSubCaptionMinZoom(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Double)
    instance.setSubCaptionMinZoom(arg0)
  }

  override fun setSubCaptionMaxZoom(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Double)
    instance.setSubCaptionMaxZoom(arg0)
  }

  @Suppress("DEPRECATION")
  override fun setCaptionAlign(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Align)
    instance.setCaptionAlign(arg0)
  }

  @Suppress("CANNOT_CHECK_FOR_ERASED")
  override fun setCaptionAligns(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Array<Align>)
    instance.setCaptionAligns(*arg0)
  }

  override fun setCaptionOffset(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Int)
    instance.setCaptionOffset(arg0)
  }

  override fun setAlpha(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Float)
    instance.setAlpha(arg0)
  }

  override fun setAngle(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Float)
    instance.setAngle(arg0)
  }

  override fun setFlat(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Boolean)
    instance.setFlat(arg0)
  }

  override fun setHideCollidedSymbols(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Boolean)
    instance.setHideCollidedSymbols(arg0)
  }

  override fun setHideCollidedMarkers(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Boolean)
    instance.setHideCollidedMarkers(arg0)
  }

  override fun setHideCollidedCaptions(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Boolean)
    instance.setHideCollidedCaptions(arg0)
  }

  override fun setForceShowIcon(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Boolean)
    instance.setForceShowIcon(arg0)
  }

  override fun setForceShowCaption(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Boolean)
    instance.setForceShowCaption(arg0)
  }

  override fun setOccupySpaceOnCollision(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Boolean)
    instance.setOccupySpaceOnCollision(arg0)
  }

  override fun setIconPerspectiveEnabled(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Boolean)
    instance.setIconPerspectiveEnabled(arg0)
  }

  override fun setCaptionPerspectiveEnabled(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Boolean)
    instance.setCaptionPerspectiveEnabled(arg0)
  }

  override fun setOnClickListener(instance: Any, arg0: Any?) {
    require(instance is Marker)
    require(arg0 is Overlay.OnClickListener?)
    instance.setOnClickListener(arg0)
  }

  override fun setTag(instance: Any, arg0: Any?) {
    require(instance is Marker)
    instance.setTag(arg0)
  }

  override fun setVisible(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Boolean)
    instance.setVisible(arg0)
  }

  override fun setMinZoom(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Double)
    instance.setMinZoom(arg0)
  }

  override fun setMaxZoom(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Double)
    instance.setMaxZoom(arg0)
  }

  override fun setMinZoomInclusive(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Boolean)
    instance.setMinZoomInclusive(arg0)
  }

  override fun setMaxZoomInclusive(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Boolean)
    instance.setMaxZoomInclusive(arg0)
  }

  override fun setZIndex(instance: Any, arg0: Any) {
    require(instance is Marker)
    require(arg0 is Int)
    instance.setZIndex(arg0)
  }
}
