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

package land.sungbin.navermap.compose.assertion

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test

class AssertionTest {
  @Test fun contentSizeDifference() {
    val list = listOf(Unit, Unit)
    val list2 = arrayOf(Unit)
    assertThat(list.contentInstanceEquals(list2)).isFalse()
  }

  @Test fun contentInstanceSame() {
    val list = listOf(Unit, Unit, Unit)
    val list2 = arrayOf(Unit, Unit, Unit)
    assertThat(list.contentInstanceEquals(list2)).isTrue()
  }

  @Test fun contentInstanceNotSame() {
    val any = Any()
    val any2 = Any()
    val any3 = Any()

    val list = listOf(any, any2, any3)
    val list2 = arrayOf(any, any3, any2)

    assertThat(list.contentInstanceEquals(list2)).isFalse()
  }

  @Test fun contentInstanceSameInAnyOrder() {
    val any = Any()
    val any2 = Any()
    val any3 = Any()
    val any4 = Any()
    val any5 = Any()

    val list = listOf(any, any2, any3, any4, any5)
    val list2 = arrayOf(any5, any4, any, any3, any2)

    assertThat(list.contentInstanceEquals(list2, inAnyOrder = true)).isTrue()
  }

  @Test fun contentInstanceNotSameInAnyOrder() {
    val any = Any()
    val any2 = Any()
    val any3 = Any()
    val any4 = Any()
    val any5 = Any()

    val list = listOf(any, any2, any3, any4, any5)
    val list2 = arrayOf(any5, any, any3, any2)

    assertThat(list.contentInstanceEquals(list2, inAnyOrder = true)).isFalse()
  }
}
