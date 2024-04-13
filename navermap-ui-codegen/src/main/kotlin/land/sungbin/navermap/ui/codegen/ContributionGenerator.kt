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

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import land.sungbin.navermap.ui.codegen.parser.OverlayClass

internal fun ktContributionNode(method: OverlayClass.Method, context: GeneratorContext): TypeSpec {
  val delegatorClazz = ClassName(context.packageName, context.name(NameFlag.DELEGATE))
  val contributorClazz = ClassName(
    context.packageName,
    context.normalizeName(method.name, NameFlag.CONTRIBUTOR),
  )
  val clear = method.parameters.any { (_, type) -> type.isNullable }

  return TypeSpec.classBuilder(context.normalizeName(method.name, NameFlag.CONTRIBUTION_NODE))
    .apply {
      addAnnotation(STABLE)
      addModifiers(KModifier.PRIVATE, KModifier.DATA)
      addSuperinterface(MAP_MODIFIER_CONTRIBUTION_NODE)
      primaryConstructor(
        FunSpec.constructorBuilder()
          .apply {
            method.parameters.forEach { (name, type) -> addParameter(name, type) }
            addParameter(
              ParameterSpec.builder("delegate", delegatorClazz)
                .defaultValue(context.noopDelegator())
                .build(),
            )
          }
          .build(),
      )
      method.parameters.forEach { (name, type) ->
        addProperty(ktProp(name, type) { initializer(name) })
      }
      addProperties(
        ktProp("delegate", delegatorClazz) { initializer("delegate") },
        ktProp("kindSet", CONTRIBUTION_KIND) {
          addModifiers(KModifier.OVERRIDE)
          initializer("%M", CONTRIBUTORS_OVERLAY)
        },
      )
      addFunctions(
        ktFun("create") {
          addModifiers(KModifier.OVERRIDE)
          returns(CONTRIBUTOR)
          addStatement(
            "return %T(%L)",
            contributorClazz,
            method.parameters.joinToString(postfix = ", delegate") { (name) -> name },
          )
        },
        ktFun("update") {
          addModifiers(KModifier.OVERRIDE)
          addParameter("contributor", CONTRIBUTOR)
          addStatement("require(contributor·is·%T)", contributorClazz)
          method.parameters.forEach { (name) -> addStatement("contributor.%L = %L", name, name) }
          addStatement("contributor.delegate = delegate")
        },
      )
      if (clear) {
        addFunction(
          ktFun("onDetach") {
            addModifiers(KModifier.OVERRIDE)
            addParameter("instance", CONTRIBUTOR)
            addStatement("require(instance·is·%T)", contributorClazz)
            addStatement("instance.clear?.invoke()")
            addStatement("instance.clear = null")
          },
        )
      }
    }
    .build()
}

internal fun ktContributor(method: OverlayClass.Method, context: GeneratorContext): TypeSpec {
  val delegatorClazz = ClassName(context.packageName, context.name(NameFlag.DELEGATE))
  val clear = method.parameters.any { (_, type) -> type.isNullable }

  return TypeSpec.classBuilder(context.normalizeName(method.name, NameFlag.CONTRIBUTOR))
    .apply {
      addModifiers(KModifier.PRIVATE)
      addSuperinterface(OVERLAY_CONTRIBUTOR)
      primaryConstructor(
        FunSpec.constructorBuilder()
          .apply {
            method.parameters.forEach { (name, type) -> addParameter(name, type) }
            addParameter("delegate", delegatorClazz)
          }
          .build(),
      )
      method.parameters.forEach { (name, type) ->
        addProperty(
          ktProp(name, type) {
            mutable()
            initializer(name)
          },
        )
      }
      addProperty(
        ktProp("delegate", delegatorClazz) {
          mutable()
          initializer("delegate")
        },
      )
      if (clear) {
        addProperty(
          ktProp("clear", LAMBDA_UNIT_NULLABLE) {
            mutable()
            initializer("null")
          },
        )
      }
      addFunction(
        ktFun("contribute") {
          addModifiers(KModifier.OVERRIDE)
          receiver(DELEGATED_OVERLAY)
          addStatement(
            "delegate.%L(this, %L)",
            method.name,
            method.parameters.joinToString { (name) -> name },
          )
          if (clear) {
            addStatement(
              "clear = { delegate.%L(this, %L) }",
              method.name,
              method.parameters.joinToString { "null" },
            )
          }
        },
      )
    }
    .build()
}
