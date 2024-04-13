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

package land.sungbin.navermap.ui.modifier.pathoverlay

import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import kotlin.Any
import kotlin.Boolean
import kotlin.Double
import kotlin.Int

public interface PathOverlayDelegate {
  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html#setMap(com.naver.maps.map.NaverMap))
   */
  public fun setMap(instance: Any, arg0: NaverMap?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html#setGlobalZIndex(int))
   */
  public fun setGlobalZIndex(instance: Any, arg0: Int) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html#setCoords(java.util.List))
   */
  public fun setCoords(instance: Any, arg0: List<LatLng>) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html#setProgress(double))
   */
  public fun setProgress(instance: Any, arg0: Double) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html#setWidth(int))
   */
  public fun setWidth(instance: Any, arg0: Int) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html#setOutlineWidth(int))
   */
  public fun setOutlineWidth(instance: Any, arg0: Int) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html#setColor(int))
   */
  public fun setColor(instance: Any, arg0: Int) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html#setOutlineColor(int))
   */
  public fun setOutlineColor(instance: Any, arg0: Int) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html#setPassedColor(int))
   */
  public fun setPassedColor(instance: Any, arg0: Int) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html#setPassedOutlineColor(int))
   */
  public fun setPassedOutlineColor(instance: Any, arg0: Int) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html#setPatternImage(com.naver.maps.map.overlay.OverlayImage))
   */
  public fun setPatternImage(instance: Any, arg0: OverlayImage?) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html#setPatternInterval(int))
   */
  public fun setPatternInterval(instance: Any, arg0: Int) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html#setHideCollidedSymbols(boolean))
   */
  public fun setHideCollidedSymbols(instance: Any, arg0: Boolean) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html#setHideCollidedMarkers(boolean))
   */
  public fun setHideCollidedMarkers(instance: Any, arg0: Boolean) {
  }

  /**
   * See
   * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html#setHideCollidedCaptions(boolean))
   */
  public fun setHideCollidedCaptions(instance: Any, arg0: Boolean) {
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
    public val NoOp: PathOverlayDelegate = object : PathOverlayDelegate {}
  }
}

public object RealPathOverlayDelegate : PathOverlayDelegate {
  override fun setMap(instance: Any, arg0: NaverMap?) {
    require(instance is PathOverlay)
    instance.setMap(arg0)
  }

  override fun setGlobalZIndex(instance: Any, arg0: Int) {
    require(instance is PathOverlay)
    instance.setGlobalZIndex(arg0)
  }

  override fun setCoords(instance: Any, arg0: List<LatLng>) {
    require(instance is PathOverlay)
    instance.setCoords(arg0)
  }

  override fun setProgress(instance: Any, arg0: Double) {
    require(instance is PathOverlay)
    instance.setProgress(arg0)
  }

  override fun setWidth(instance: Any, arg0: Int) {
    require(instance is PathOverlay)
    instance.setWidth(arg0)
  }

  override fun setOutlineWidth(instance: Any, arg0: Int) {
    require(instance is PathOverlay)
    instance.setOutlineWidth(arg0)
  }

  override fun setColor(instance: Any, arg0: Int) {
    require(instance is PathOverlay)
    instance.setColor(arg0)
  }

  override fun setOutlineColor(instance: Any, arg0: Int) {
    require(instance is PathOverlay)
    instance.setOutlineColor(arg0)
  }

  override fun setPassedColor(instance: Any, arg0: Int) {
    require(instance is PathOverlay)
    instance.setPassedColor(arg0)
  }

  override fun setPassedOutlineColor(instance: Any, arg0: Int) {
    require(instance is PathOverlay)
    instance.setPassedOutlineColor(arg0)
  }

  override fun setPatternImage(instance: Any, arg0: OverlayImage?) {
    require(instance is PathOverlay)
    instance.setPatternImage(arg0)
  }

  override fun setPatternInterval(instance: Any, arg0: Int) {
    require(instance is PathOverlay)
    instance.setPatternInterval(arg0)
  }

  override fun setHideCollidedSymbols(instance: Any, arg0: Boolean) {
    require(instance is PathOverlay)
    instance.setHideCollidedSymbols(arg0)
  }

  override fun setHideCollidedMarkers(instance: Any, arg0: Boolean) {
    require(instance is PathOverlay)
    instance.setHideCollidedMarkers(arg0)
  }

  override fun setHideCollidedCaptions(instance: Any, arg0: Boolean) {
    require(instance is PathOverlay)
    instance.setHideCollidedCaptions(arg0)
  }

  override fun setOnClickListener(instance: Any, arg0: Overlay.OnClickListener?) {
    require(instance is PathOverlay)
    instance.setOnClickListener(arg0)
  }

  override fun setTag(instance: Any, arg0: Any?) {
    require(instance is PathOverlay)
    instance.setTag(arg0)
  }

  override fun setVisible(instance: Any, arg0: Boolean) {
    require(instance is PathOverlay)
    instance.setVisible(arg0)
  }

  override fun setMinZoom(instance: Any, arg0: Double) {
    require(instance is PathOverlay)
    instance.setMinZoom(arg0)
  }

  override fun setMaxZoom(instance: Any, arg0: Double) {
    require(instance is PathOverlay)
    instance.setMaxZoom(arg0)
  }

  override fun setMinZoomInclusive(instance: Any, arg0: Boolean) {
    require(instance is PathOverlay)
    instance.setMinZoomInclusive(arg0)
  }

  override fun setMaxZoomInclusive(instance: Any, arg0: Boolean) {
    require(instance is PathOverlay)
    instance.setMaxZoomInclusive(arg0)
  }

  override fun setZIndex(instance: Any, arg0: Int) {
    require(instance is PathOverlay)
    instance.setZIndex(arg0)
  }
}
