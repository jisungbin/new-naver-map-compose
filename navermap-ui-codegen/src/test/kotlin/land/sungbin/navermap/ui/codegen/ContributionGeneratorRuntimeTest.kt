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

package land.sungbin.navermap.ui.codegen

import land.sungbin.navermap.ui.codegen.dummy.dummyOverlayResult
import kotlin.test.Test

class ContributionGeneratorRuntimeTest {
  @Test fun ktContributionNode() {
    val context = GeneratorContext(
      packageName = "land.sungbin.navermap.ui.modifier.generator",
      overlayResult = dummyOverlayResult,
    )
    dummyOverlayResult.setters.forEach {
      val node = ktContributionNode(it, context)
      println(node)
    }
  }

  @Test fun ktContributor() {
    val context = GeneratorContext(
      packageName = "land.sungbin.navermap.ui.modifier.generator",
      overlayResult = dummyOverlayResult,
    )
    dummyOverlayResult.setters.forEach {
      val extension = ktContributor(it, context)
      println(extension)
    }
  }

  @Test fun ktComposableContent() {
    val context = GeneratorContext(
      packageName = "land.sungbin.navermap.ui.modifier.generator",
      overlayResult = dummyOverlayResult,
    )
    dummyOverlayResult.constructors.forEach {
      val composable = ktComposableContent(context, "", "", it)
      println(composable)
    }
  }
}
