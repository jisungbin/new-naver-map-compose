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

package land.sungbin.navermap.ui.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Stable
import com.naver.maps.geometry.LatLng
import land.sungbin.navermap.runtime.MapApplier
import land.sungbin.navermap.runtime.delegate.OverlayDelegator
import land.sungbin.navermap.runtime.modifier.MapModifier
import land.sungbin.navermap.runtime.node.OverlayNode
import land.sungbin.navermap.ui.NaverMapContent
import land.sungbin.navermap.ui.modifier.delegator.LocalPathOverlayDelegator
import land.sungbin.navermap.ui.modifier.pathoverlay.PathOverlayDelegate
import land.sungbin.navermap.ui.modifier.pathoverlay.PathOverlayModifier

/**
 * See
 * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html)
 */
@Composable
@Suppress("UnusedReceiverParameter")
public fun NaverMapContent.PathOverlay(modifier: PathOverlayModifier = PathOverlayModifier) {
  val delegator: PathOverlayDelegate = LocalPathOverlayDelegator.current

  ComposeNode<OverlayNode, MapApplier>(
    factory = {
      OverlayNode(
        modifier = modifier.toMapModifier(delegator),
        factory = {
          object : OverlayDelegator {
            override val instance: Any = com.naver.maps.map.overlay.PathOverlay()

            override fun setMap(map: Any?) {
              delegator.setMap(instance, map)
            }
          }
        },
      )
    },
    update = {
      update(modifier) {
        this.modifier = it.toMapModifier(delegator)
      }
    },
  )
}

/**
 * See
 * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PathOverlay.html)
 */
@Composable
@Suppress("UnusedReceiverParameter")
public fun NaverMapContent.PathOverlay(
  arg0: List<LatLng>,
  modifier: PathOverlayModifier =
    PathOverlayModifier,
) {
  val delegator: PathOverlayDelegate = LocalPathOverlayDelegator.current

  ComposeNode<OverlayNode, MapApplier>(
    factory = {
      OverlayNode(
        modifier = modifier.toMapModifier(delegator),
        factory = {
          object : OverlayDelegator {
            override val instance: Any = com.naver.maps.map.overlay.PathOverlay(
              arg0,
            )

            override fun setMap(map: Any?) {
              delegator.setMap(instance, map)
            }
          }
        },
      )
    },
    update = {
      update(modifier) {
        this.modifier = it.toMapModifier(delegator)
      }
    },
  )
}

@Stable
private fun PathOverlayModifier.toMapModifier(delegator: PathOverlayDelegate): MapModifier =
  fold<MapModifier>(MapModifier) { acc, element ->
    element.delegator = delegator
    val node = element.getContributionNode() ?: return@fold acc
    acc then node
  }
