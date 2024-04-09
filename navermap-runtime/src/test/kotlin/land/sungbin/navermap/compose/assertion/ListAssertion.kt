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

import assertk.Assert
import assertk.assertions.support.expectedListDiff
import org.jetbrains.annotations.VisibleForTesting

fun Assert<List<*>>.containsInstanceExactly(vararg elements: Any?) = given { actual ->
  if (actual.contentInstanceEquals(elements)) return
  expectedListDiff(elements.asList(), actual)
}

fun Assert<List<*>>.containsInstanceExactlyInAnyOrder(vararg elements: Any?) = given { actual ->
  if (actual.contentInstanceEquals(elements, inAnyOrder = true)) return
  expectedListDiff(elements.asList(), actual)
}

@VisibleForTesting
internal fun List<*>.contentInstanceEquals(other: Array<*>, inAnyOrder: Boolean = false): Boolean {
  if (size != other.size) return false
  for (i in indices) {
    val check = if (inAnyOrder) other.find { it === get(i) } != null else get(i) === other[i]
    if (!check) return false
  }
  return true
}
