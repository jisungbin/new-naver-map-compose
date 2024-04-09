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

package land.sungbin.navermap.compose.modifier

import assertk.assertThat
import assertk.assertions.isInstanceOf
import assertk.assertions.isSameInstanceAs
import land.sungbin.navermap.compose.assertion.containsInstanceExactly
import land.sungbin.navermap.compose.assertion.containsInstanceExactlyInAnyOrder
import land.sungbin.navermap.runtime.modifier.CombinedMapModifier
import land.sungbin.navermap.runtime.modifier.MapModifier
import land.sungbin.navermap.runtime.modifier.MapModifierNode
import kotlin.test.Test

class MapModifierTest {
  @Test fun mapModifierSingleCombined() {
    val modifier = NothingMapModifierNode()
    val then = MapModifier then modifier
    assertThat(then).isSameInstanceAs(modifier)
  }

  @Test fun combinedModifierLoopAllNodes() {
    val modifier1 = NothingMapModifierNode()
    val modifier2 = UnitMapModifierNode()
    val modifier3 = IntMapModifierNode()
    val modifier4 = AnyMapModifierNode()

    val modifiers = MapModifier
      .then(modifier1)
      .then(modifier2)
      .then(modifier3)
      .then(modifier4)

    val nodes = mutableListOf<MapModifierNode<*>>()
    modifiers.all(nodes::add)

    assertThat(nodes).containsInstanceExactlyInAnyOrder(modifier1, modifier2, modifier3, modifier4)
  }

  @Test fun combinedCombinedModifierLoopAllNodes() {
    val modifier1 = NothingMapModifierNode()
    val modifier2 = UnitMapModifierNode()
    val modifier3 = IntMapModifierNode()
    val modifier4 = AnyMapModifierNode()
    val modifier5 = object : TestBaseMapModifierNode<Float>() {}

    val then1 = MapModifier.then(modifier1).then(modifier2)
    val then2 = MapModifier.then(modifier3).then(modifier4).then(modifier5)
    val then3 = MapModifier.then(then1).then(then2)

    assertThat(then1).isInstanceOf<CombinedMapModifier>()
    assertThat(then2).isInstanceOf<CombinedMapModifier>()
    assertThat(then3).isInstanceOf<CombinedMapModifier>()

    val nodes = mutableListOf<MapModifierNode<*>>()
    then3.all(nodes::add)

    assertThat(nodes).containsInstanceExactlyInAnyOrder(modifier1, modifier2, modifier3, modifier4, modifier5)
  }

  @Test fun combinedModifierFoldIn() {
    val modifier1 = NothingMapModifierNode()
    val modifier2 = UnitMapModifierNode()
    val modifier3 = IntMapModifierNode()
    val modifier4 = AnyMapModifierNode()

    val modifiers = MapModifier
      .then(modifier1)
      .then(modifier2)
      .then(modifier3)
      .then(modifier4)

    val nodes = modifiers.foldIn(mutableListOf<MapModifierNode<*>>()) { acc, element ->
      acc.apply { add(element) }
    }

    assertThat(nodes).containsInstanceExactly(modifier1, modifier2, modifier3, modifier4)
  }

  @Test fun combinedModifierFoldOut() {
    val modifier1 = NothingMapModifierNode()
    val modifier2 = UnitMapModifierNode()
    val modifier3 = IntMapModifierNode()
    val modifier4 = AnyMapModifierNode()

    val modifiers = MapModifier
      .then(modifier1)
      .then(modifier2)
      .then(modifier3)
      .then(modifier4)

    val nodes = modifiers.foldOut(mutableListOf<MapModifierNode<*>>()) { element, acc ->
      acc.apply { add(element) }
    }

    assertThat(nodes).containsInstanceExactly(modifier4, modifier3, modifier2, modifier1)
  }
}

class NothingMapModifierNode : TestBaseMapModifierNode<Nothing>()
class UnitMapModifierNode : TestBaseMapModifierNode<Unit>()
class IntMapModifierNode : TestBaseMapModifierNode<Int>()
class AnyMapModifierNode : TestBaseMapModifierNode<Any>()

abstract class TestBaseMapModifierNode<T> : MapModifierNode<T> {
  override fun hashCode(): Int = System.identityHashCode(this)
  override fun equals(other: Any?) = this === other
}
