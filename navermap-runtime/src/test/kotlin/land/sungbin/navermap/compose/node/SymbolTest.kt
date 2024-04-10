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

package land.sungbin.navermap.compose.node

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isSameInstanceAs
import land.sungbin.navermap.runtime.node.Symbol
import kotlin.test.Test

class SymbolTest {
  @Test fun boundOnlyOnce() {
    val target = Any()

    val symbol = Symbol<Any>()
    symbol.bound(target)
    symbol.bound(Any())

    assertThat(symbol.owner).isSameInstanceAs(target)
  }

  @Test fun twiceBoundWithForce() {
    val target = Any()
    val target2 = Any()

    val symbol = Symbol<Any>()
    symbol.bound(target)
    symbol.bound(target2, force = true)

    assertThat(symbol.owner).isSameInstanceAs(target2)
  }

  @Test fun unboundSymbolGetError() {
    val symbol = Symbol<Any>()
    assertFailure { symbol.owner }.hasMessage("Symbol is not bound")
  }
}
