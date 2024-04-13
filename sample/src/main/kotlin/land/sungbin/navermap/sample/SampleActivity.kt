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

package land.sungbin.navermap.sample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.delay
import land.sungbin.navermap.ui.NaverMap
import land.sungbin.navermap.ui.content.Marker
import land.sungbin.navermap.ui.modifier.marker.MarkerModifier
import land.sungbin.navermap.ui.modifier.marker.captionText
import land.sungbin.navermap.ui.modifier.marker.onClickListener

class SampleActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        var showMap by remember { mutableStateOf(false) }

        AnimatedVisibility(
          modifier = Modifier.fillMaxSize(0.5f),
          visible = showMap,
        ) {
          NaverMap(modifier = Modifier.fillMaxSize()) {
            var now by remember { mutableLongStateOf(System.currentTimeMillis()) }

            LaunchedEffect(Unit) {
              while (true) {
                now = System.currentTimeMillis()
                delay(1000)
              }
            }

            Marker(
              LatLng(37.5670135, 126.9783740),
              modifier = MarkerModifier
                .onClickListener {
                  Toast
                    .makeText(this@SampleActivity, "Marker Clicked at $now", Toast.LENGTH_SHORT)
                    .show()
                  true
                }
                .captionText("Current time is $now"),
            )
          }
        }
        Button(onClick = { showMap = !showMap }) {
          Text(text = if (showMap) "Hide Map" else "Show Map")
        }
      }
    }
  }
}
