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

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import land.sungbin.navermap.ui.modifier.generator.parser.OverlayClass

// TODO: We need to create an instance of OverlayModifierNode and MapModifierNode
//  each time we want to use OverlayModifier. We need to think about how to minimize
//  node allocations.

internal fun ktModifierNode(method: OverlayClass.Method, context: GeneratorContext): TypeSpec {
  val modifierClazz = ClassName(context.packageName, context.name(NameFlag.MODIFIER))
  val delegatorClazz = ClassName(context.packageName, context.name(NameFlag.DELEGATE))

  return TypeSpec.classBuilder(context.normalizeName(method.name, NameFlag.MODIFIER_NODE))
    .apply {
      addAnnotation(IMMUTABLE)
      addModifiers(KModifier.PRIVATE, KModifier.DATA)
      addSuperinterface(modifierClazz)
      primaryConstructor(
        FunSpec.constructorBuilder()
          .apply {
            method.parameters.forEach { (name, type) -> addParameter(name, type) }
            addParameter(
              ParameterSpec.builder("delegator", delegatorClazz)
                .defaultValue(context.noopDelegator())
                .build(),
            )
          }
          .build(),
      )
      method.parameters.forEach { (name, type) ->
        addProperty(
          ktProp(name, type) {
            addModifiers(KModifier.PRIVATE)
            initializer(name)
          },
        )
      }
      addProperty(
        ktProp("delegator", delegatorClazz) {
          mutable()
          addModifiers(KModifier.OVERRIDE)
          initializer("delegator")
        },
      )
      addFunctions(
        ktFun("getContributionNode") {
          addModifiers(KModifier.OVERRIDE)
          returns(MAP_MODIFIER_CONTRIBUTION_NODE)
          addStatement(
            "return %T(%L)",
            ClassName(
              context.packageName,
              context.normalizeName(method.name, NameFlag.CONTRIBUTION_NODE),
            ),
            method.parameters.joinToString(postfix = ", delegator") { (name) -> name },
          )
        },
        ktFun("fold") {
          addModifiers(KModifier.OVERRIDE)
          val r = TypeVariableName("R", bounds = arrayOf(ANY))
          addTypeVariable(r)
          addParameter("initial", r)
          addParameter(
            "operation",
            LambdaTypeName.get(
              receiver = null,
              parameters = arrayOf(r, modifierClazz),
              returnType = r,
            ),
          )
          returns(r)
          addStatement("return operation(initial, this)")
        },
      )
    }
    .build()
}

internal fun ktModifierExtension(method: OverlayClass.Method, context: GeneratorContext): FunSpec {
  val modifierClazz = ClassName(context.packageName, context.name(NameFlag.MODIFIER))

  return ktFun(context.normalizeName(method.name, NameFlag.MODIFIER_EXTENSION)) {
    addAnnotation(STABLE)
    receiver(modifierClazz)
    method.parameters.forEach { (name, type) -> addParameter(name, type) }
    returns(modifierClazz)
    addStatement(
      "return this·then·%T(%L)",
      ClassName(
        context.packageName,
        context.normalizeName(method.name, NameFlag.MODIFIER_NODE),
      ),
      method.parameters.joinToString { (name) -> name },
    )
  }
}
