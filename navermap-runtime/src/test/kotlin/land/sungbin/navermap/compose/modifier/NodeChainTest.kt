package land.sungbin.navermap.compose.modifier

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import land.sungbin.navermap.runtime.modifier.ActionReplace
import land.sungbin.navermap.runtime.modifier.ActionReuse
import land.sungbin.navermap.runtime.modifier.ActionUpdate
import land.sungbin.navermap.runtime.modifier.MapModifierNode
import land.sungbin.navermap.runtime.modifier.dirtyForModifiers

class NodeChainTest {
  @Test
  fun dirtyForModifiersReuse() {
    val node = NothingMapModifierNode()

    val dirty = dirtyForModifiers(node, node)
    assertThat(dirty).isEqualTo(ActionReuse)
  }

  @Test
  fun dirtyForModifiersUpdate() {
    val node = NothingMapModifierNode()
    val node2 = NothingMapModifierNode()

    val dirty = dirtyForModifiers(node, node2)
    assertThat(dirty).isEqualTo(ActionUpdate)
  }

  @Test
  fun dirtyForModifiersReplace() {
    val node = NothingMapModifierNode()
    val node2 = UnitMapModifierNode()

    val dirty = dirtyForModifiers(node, node2)
    assertThat(dirty).isEqualTo(ActionReplace)
  }
}

class NothingMapModifierNode : MapModifierNode<Nothing> {
  override fun hashCode(): Int = System.identityHashCode(this)
  override fun equals(other: Any?) = this.hashCode() == other.hashCode()
}

class UnitMapModifierNode : MapModifierNode<Unit> {
  override fun hashCode(): Int = System.identityHashCode(this)
  override fun equals(other: Any?) = this.hashCode() == other.hashCode()
}
