package land.sungbin.navermap.compose.modifier

import kotlin.test.Test
import land.sungbin.navermap.runtime.modifier.MapModifierNodeChain

class NodeChainTest {
  @Test
  fun testForTest() {
    val chain = MapModifierNodeChain(emptyList())
    println(chain)
  }
}
