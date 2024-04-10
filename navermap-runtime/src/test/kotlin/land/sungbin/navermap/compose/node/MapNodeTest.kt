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

import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import assertk.assertFailure
import assertk.assertions.hasMessage
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import land.sungbin.navermap.runtime.DebugChanges
import land.sungbin.navermap.runtime.MapApplier
import land.sungbin.navermap.runtime.delegate.MapViewDelegator
import land.sungbin.navermap.runtime.delegate.NaverMapDelegator
import land.sungbin.navermap.runtime.delegate.OverlayDelegator
import land.sungbin.navermap.runtime.node.DelegatedNaverMap
import land.sungbin.navermap.runtime.node.LayoutNode
import land.sungbin.navermap.runtime.node.MapNodeLifecycleCallback
import land.sungbin.navermap.runtime.node.OverlayNode
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import kotlin.test.Test

class MapNodeTest : CompositionBaseTest() {
  @Test fun wellStructed() = compose {
    ComposeNode<LayoutNode, MapApplier>(
      factory = {
        LayoutNode(
          factory = {
            object : MapViewDelegator {
              override val instance = Any()
              override fun getMapAsync(block: (NaverMapDelegator) -> Unit) =
                block(NaverMapDelegator(Any()))
            }
          },
          onRelease = null,
        )
      },
      update = {},
    ) {
      ComposeNode<OverlayNode, MapApplier>(
        factory = {
          OverlayNode(
            factory = {
              object : OverlayDelegator {
                override val instance = Any()
                override fun setMap(map: DelegatedNaverMap?) = Unit
              }
            },
          )
        },
        update = {},
      )
    }
  }

  @Test fun wellStructedWhenNoNaverMap() = compose {
    ComposeNode<LayoutNode, MapApplier>(
      factory = {
        LayoutNode(
          factory = {
            object : MapViewDelegator {
              override val instance = Any()
              override fun getMapAsync(block: (NaverMapDelegator) -> Unit) = Unit
            }
          },
          onRelease = null,
        )
      },
      update = {},
    ) {
      ComposeNode<OverlayNode, MapApplier>(
        factory = {
          OverlayNode(
            factory = {
              object : OverlayDelegator {
                override val instance = Any()
                override fun setMap(map: DelegatedNaverMap?) = Unit
              }
            },
          )
        },
        update = {},
      )
    }
  }

  @Test fun mapNodeLifecycleCallbackWorks() = compositionTest {
    val attach = mockk<(node: String) -> Unit>(name = "attach", relaxed = true)
    val detach = mockk<(node: String) -> Unit>(name = "detach", relaxed = true)

    var mount by mutableStateOf(true)

    compose {
      if (mount) {
        ComposeNode<LayoutNode, MapApplier>(
          factory = {
            LayoutNode(
              factory = {
                object : MapViewDelegator {
                  override val instance = Any()
                  override fun getMapAsync(block: (NaverMapDelegator) -> Unit) =
                    block(NaverMapDelegator(Any()))
                }
              },
              lifecycle = object : MapNodeLifecycleCallback {
                override fun onAttached() = attach("layout")
                override fun onDetached() = detach("layout")
              },
              onRelease = null,
            )
          },
          update = {},
        ) {
          ComposeNode<OverlayNode, MapApplier>(
            factory = {
              OverlayNode(
                factory = {
                  object : OverlayDelegator {
                    override val instance = Any()
                    override fun setMap(map: DelegatedNaverMap?) = Unit
                  }
                },
                lifecycle = object : MapNodeLifecycleCallback {
                  override fun onAttached() = attach("overlay")
                  override fun onDetached() = detach("overlay")
                },
              )
            },
            update = {},
          )
        }
      }
    }

    verifyConsistent()
    verify(exactly = 0) { detach(any()) }

    mount = false
    expectChanges()

    verifySequence {
      attach("layout")
      attach("overlay")
      detach("overlay")
      detach("layout")
    }
  }

  @Test fun mapNodeLifecycleAttachCalledWhenNaverMapIsReady() = compositionTest {
    val naverMapAttacted = mockk<() -> Unit>(name = "attach", relaxed = true)

    compose {
      var getMapAsyncBlock by remember { mutableStateOf<((NaverMapDelegator) -> Unit)?>(null) }

      LaunchedEffect(getMapAsyncBlock) {
        val getMapAsync = getMapAsyncBlock
        if (getMapAsync != null) {
          getMapAsync(NaverMapDelegator(Any()))
        }
      }

      ComposeNode<LayoutNode, MapApplier>(
        factory = {
          LayoutNode(
            factory = {
              object : MapViewDelegator {
                override val instance = Any()
                override fun getMapAsync(block: (NaverMapDelegator) -> Unit) {
                  getMapAsyncBlock = block
                }
              }
            },
            onRelease = null,
          )
        },
        update = {},
      ) {
        ComposeNode<OverlayNode, MapApplier>(
          factory = {
            OverlayNode(
              factory = {
                object : OverlayDelegator {
                  override val instance = Any()
                  override fun setMap(map: DelegatedNaverMap?) = Unit
                }
              },
              lifecycle = object : MapNodeLifecycleCallback {
                override fun onAttached() = naverMapAttacted()
              },
            )
          },
          update = {},
        )
      }
    }

    verifyConsistent()
    verify(exactly = 0) { naverMapAttacted() }

    advance()

    verify(exactly = 1) { naverMapAttacted() }
  }

  @Test fun layoutNodeCanReleasing() = compositionTest {
    val releasing = mockk<() -> Unit>(name = "releasing", relaxed = true)

    var mount by mutableStateOf(true)

    compose {
      if (mount) {
        ComposeNode<LayoutNode, MapApplier>(
          factory = {
            LayoutNode(
              factory = {
                object : MapViewDelegator {
                  override val instance = Any()
                  override fun getMapAsync(block: (NaverMapDelegator) -> Unit) = Unit
                }
              },
              onRelease = releasing,
            )
          },
          update = {},
        )
      }
    }

    verifyConsistent()
    verify(exactly = 0) { releasing() }

    mount = false
    expectChanges()

    verifySequence { releasing() }
  }

  @Test fun overlayNodeWithoutRootCauseError() = compositionTest {
    val error = assertFailure {
      compose {
        ComposeNode<OverlayNode, MapApplier>(
          factory = {
            OverlayNode(
              factory = {
                object : OverlayDelegator {
                  override val instance = Any()
                  override fun setMap(map: DelegatedNaverMap?) = Unit
                }
              },
            )
          },
          update = {},
        )
      }
    }
    error.hasMessage("OverlayNode must be added as a child of LayoutNode.")
  }

  companion object {
    @JvmStatic @BeforeAll
    fun turnOnDebugChanges() {
      DebugChanges = true
    }

    @JvmStatic @AfterAll
    fun turnOffDebugChanges() {
      DebugChanges = false
    }
  }
}
