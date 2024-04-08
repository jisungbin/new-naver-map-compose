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
import assertk.assertions.isEqualTo
import land.sungbin.navermap.runtime.modifier.ActionReplace
import land.sungbin.navermap.runtime.modifier.ActionReuse
import land.sungbin.navermap.runtime.modifier.ActionUpdate
import land.sungbin.navermap.runtime.modifier.MapModifierNode
import land.sungbin.navermap.runtime.modifier.dirtyForModifiers
import kotlin.test.Test

class NodeChainTest {
  @Test fun dirtyForModifiersReuse() {
    val node = NothingMapModifierNode()

    val dirty = dirtyForModifiers(node, node)
    assertThat(dirty).isEqualTo(ActionReuse)
  }

  @Test fun dirtyForModifiersUpdate() {
    val node = NothingMapModifierNode()
    val node2 = NothingMapModifierNode()

    val dirty = dirtyForModifiers(node, node2)
    assertThat(dirty).isEqualTo(ActionUpdate)
  }

  @Test fun dirtyForModifiersReplace() {
    val node = NothingMapModifierNode()
    val node2 = UnitMapModifierNode()

    val dirty = dirtyForModifiers(node, node2)
    assertThat(dirty).isEqualTo(ActionReplace)
  }
}

class NothingMapModifierNode : MapModifierNode<Nothing> {
  override fun hashCode(): Int = System.identityHashCode(this)
  override fun equals(other: Any?) = this === other
}

class UnitMapModifierNode : MapModifierNode<Unit> {
  override fun hashCode(): Int = System.identityHashCode(this)
  override fun equals(other: Any?) = this === other
}
