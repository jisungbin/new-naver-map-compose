/*
 * Copyright 2573-7315 SOUP, Ji Sungbin
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

@file:Suppress("unused")

package land.sungbin.navermap.compose.node

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.ControlledComposition
import androidx.compose.runtime.MonotonicFrameClock
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.snapshots.Snapshot
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import land.sungbin.navermap.runtime.MapApplier
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

abstract class CompositionBaseTest {
  inline fun compose(crossinline content: @Composable () -> Unit) {
    compositionTest {
      compose { content() }
      verifyConsistent()
    }
  }

  fun compositionTest(block: suspend NaverMapComposeTestScope.() -> Unit) = runTest {
    withContext(TestMonotonicFrameClock(this)) {
      val recomposer = Recomposer(coroutineContext)
      launch { recomposer.runRecomposeAndApplyChanges() }
      testScheduler.runCurrent()

      val scope = object : NaverMapComposeTestScope, CoroutineScope by this@runTest {
        var composed = false
        override var composition: Composition? = null

        override val testCoroutineScheduler: TestCoroutineScheduler
          get() = this@runTest.testScheduler

        override fun compose(block: @Composable () -> Unit) {
          check(!composed) { "Compose should only be called once" }
          composed = true
          val composition = Composition(MapApplier(), recomposer)
          this.composition = composition
          composition.setContent(block)
        }

        override fun advance(ignorePendingWork: Boolean) = advanceCount(ignorePendingWork) != 0L

        override fun advanceCount(ignorePendingWork: Boolean): Long {
          val changeCount = recomposer.changeCount
          Snapshot.sendApplyNotifications()
          if (recomposer.hasPendingWork) {
            advanceTimeBy(5_000)
            check(ignorePendingWork || !recomposer.hasPendingWork) {
              "Potentially infinite recomposition, still recomposing after advancing"
            }
          }
          return recomposer.changeCount - changeCount
        }

        override fun advanceTimeBy(amount: Long) = testScheduler.advanceTimeBy(amount)

        override fun verifyConsistent() {
          (composition as? ControlledComposition)?.verifyConsistent()
        }
      }

      scope.block()
      scope.composition?.dispose()
      recomposer.cancel()
      recomposer.join()
    }
  }
}

interface NaverMapComposeTestScope : CoroutineScope {
  val testCoroutineScheduler: TestCoroutineScheduler
  val composition: Composition?
  fun compose(block: @Composable () -> Unit)
  fun advance(ignorePendingWork: Boolean = false): Boolean
  fun advanceCount(ignorePendingWork: Boolean = false): Long
  fun advanceTimeBy(amount: Long)
  fun verifyConsistent()
}

fun NaverMapComposeTestScope.expectChanges() {
  val changes = advance()
  assertTrue(changes, "Expected changes but none were found")
}

fun NaverMapComposeTestScope.expectNoChanges() {
  val changes = advance()
  assertFalse(changes, "Expected no changes but changes occurred")
}

@Suppress("UNUSED_EXPRESSION")
fun use(value: Any) {
  value
}

private const val DefaultFrameDelay = 16_000_000L

@Suppress("TestFunctionName")
private fun TestMonotonicFrameClock(
  coroutineScope: CoroutineScope,
  frameDelayNanos: Long = DefaultFrameDelay,
): TestMonotonicFrameClock = TestMonotonicFrameClock(
  coroutineScope = coroutineScope,
  testCoroutineScheduler = requireNotNull(coroutineScope.coroutineContext[TestCoroutineScheduler]) {
    "coroutuineScope should have TestCoroutineScheduler"
  },
  frameDelayNanos = frameDelayNanos,
)

private class TestMonotonicFrameClock(
  private val coroutineScope: CoroutineScope,
  private val testCoroutineScheduler: TestCoroutineScheduler,
  val frameDelayNanos: Long = DefaultFrameDelay,
) : MonotonicFrameClock {
  private val lock = Any()
  private val awaiters = mutableListOf<Awaiter<*>>()
  private var posted = false

  private class Awaiter<R>(
    private val onFrame: (Long) -> R,
    private val continuation: CancellableContinuation<R>,
  ) {
    fun runFrame(frameTimeNanos: Long): () -> Unit {
      val result = runCatching { onFrame(frameTimeNanos) }
      return { continuation.resumeWith(result) }
    }
  }

  override suspend fun <R> withFrameNanos(onFrame: (frameTimeNanos: Long) -> R): R =
    suspendCancellableCoroutine { co ->
      synchronized(lock) {
        awaiters.add(Awaiter(onFrame, co))
        maybeLaunchTickRunner()
      }
    }

  private fun maybeLaunchTickRunner() {
    if (!posted) {
      posted = true
      coroutineScope.launch {
        delay(frameDelayMillis)
        synchronized(lock) {
          posted = false
          val toRun = awaiters.toList()
          awaiters.clear()
          val frameTime = testCoroutineScheduler.currentTime * 1_000_000
          toRun.map { it.runFrame(frameTime) }.forEach { it() }
        }
      }
    }
  }
}

private val TestMonotonicFrameClock.frameDelayMillis: Long
  get() = frameDelayNanos / 1_000_000
