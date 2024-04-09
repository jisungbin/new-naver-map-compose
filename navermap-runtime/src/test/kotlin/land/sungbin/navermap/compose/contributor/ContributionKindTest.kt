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

package land.sungbin.navermap.compose.contributor

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import land.sungbin.navermap.runtime.contributor.Contributors
import land.sungbin.navermap.runtime.contributor.contains
import land.sungbin.navermap.runtime.contributor.or
import kotlin.test.Test

class ContributionKindTest {
  @Test fun orBitwises() {
    val intOrContributionKind = Contributors.MapView.mask or Contributors.NaverMap
    val contributionKindOrInt = Contributors.NaverMap or Contributors.MapView.mask
    val contributionKindOrContributionKind = Contributors.NaverMap or Contributors.MapView
    val contributionKindPlusContributionKind = Contributors.MapView + Contributors.NaverMap

    assertThat(0b110).all {
      isEqualTo(intOrContributionKind)
      isEqualTo(contributionKindOrInt)
      isEqualTo(contributionKindOrContributionKind.mask)
      isEqualTo(contributionKindPlusContributionKind.mask)
    }
  }

  @Test fun andBitwise() {
    val anyOrMapViewOrNaverMap = Contributors.Any or Contributors.MapView or Contributors.MapView
    assertThat(Contributors.MapView in anyOrMapViewOrNaverMap).isTrue()
    assertThat(Contributors.Overlay in anyOrMapViewOrNaverMap).isFalse()
  }
}
