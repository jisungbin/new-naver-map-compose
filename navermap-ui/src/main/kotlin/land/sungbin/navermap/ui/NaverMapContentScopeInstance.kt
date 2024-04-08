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

package land.sungbin.navermap.ui

import androidx.compose.runtime.Composable
import com.naver.maps.map.overlay.Overlay
import land.sungbin.navermap.runtime.modifier.MapModifier
import land.sungbin.navermap.token.OverlayFactory

internal data object NaverMapContentScopeInstance : NaverMapContentScope {
  @Composable
  override fun <O, T : OverlayFactory<O>> T.invoke(
    modifier: MapModifier,
    block: O.() -> Unit,
  ) where O : Overlay {
    TODO("Not yet implemented")
  }
}
