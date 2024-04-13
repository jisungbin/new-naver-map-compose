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

package land.sungbin.navermap.ui.modifier.generator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec

internal inline fun ktFun(name: String, block: FunSpec.Builder.() -> Unit) =
  FunSpec.builder(name).apply(block).build()

internal inline fun ktProp(name: String, typeName: TypeName, block: PropertySpec.Builder.() -> Unit) =
  PropertySpec.builder(name, typeName).apply(block).build()

internal fun TypeSpec.Builder.addFunctions(vararg functions: FunSpec) =
  addFunctions(functions.asList())

internal fun TypeSpec.Builder.addProperties(vararg properties: PropertySpec) =
  addProperties(properties.asList())

internal fun FileSpec.Builder.addTypes(vararg types: TypeSpec) =
  addTypes(types.asList())

internal fun suppress(vararg warning: String) =
  AnnotationSpec.builder(Suppress::class)
    .addMember(warning.joinToString { "\"$it\"" })
    .build()

internal fun deprecated(
  message: String = "Deprecated from the original API.",
  replaceWith: String = "",
) = AnnotationSpec.builder(Deprecated::class)
  .addMember("%S", message)
  .addMember("%T(%S)", ReplaceWith::class, replaceWith)
  .build()
