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

import com.squareup.kotlinpoet.CodeBlock
import land.sungbin.navermap.ui.codegen.dummy.dummyOverlayResult
import kotlin.test.Test

class ContentComposableGeneratorRuntimeTime {
  @Test fun ktMapModifierMapper() {
    val context = GeneratorContext(
      packageName = "land.sungbin.navermap.ui.modifier.generator",
      overlayResult = dummyOverlayResult,
    )
    println(ktMapModifierMapper(context))
  }

  @Test fun ktComposeNodeUpdate() {
    val context = GeneratorContext(
      packageName = "land.sungbin.navermap.ui.modifier.generator",
      overlayResult = dummyOverlayResult,
    )
    println(ktComposeNodeUpdate(context))
  }

  @Test fun ktContentConstructor() {
    val context = GeneratorContext(
      packageName = "land.sungbin.navermap.ui.modifier.generator",
      overlayResult = dummyOverlayResult,
    )
    dummyOverlayResult.constructors.forEach {
      println(ktContentConstructor(context.overlayClass, it))
    }
  }

  @Test fun ktOverlayDelegatorObject() {
    val context = GeneratorContext(
      packageName = "land.sungbin.navermap.ui.modifier.generator",
      overlayResult = dummyOverlayResult,
    )
    dummyOverlayResult.constructors.forEach {
      println(ktOverlayDelegatorObject(context.overlayClass, it))
    }
  }

  @Test fun ktComposeNode() {
    val context = GeneratorContext(
      packageName = "land.sungbin.navermap.ui.modifier.generator",
      overlayResult = dummyOverlayResult,
    )
    println(ktComposeNode(context, CodeBlock.of("overlayDelegator"), CodeBlock.of("updateBlock")))
  }

  @Test fun ktDelegator() {
    val context = GeneratorContext(
      packageName = "land.sungbin.navermap.ui.modifier.generator",
      overlayResult = dummyOverlayResult,
    )
    println(ktDelegator("", "", context))
  }
}
