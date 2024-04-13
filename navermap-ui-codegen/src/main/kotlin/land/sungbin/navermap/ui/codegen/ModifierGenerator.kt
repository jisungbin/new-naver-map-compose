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

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName

internal fun ktModifier(context: GeneratorContext): TypeSpec {
  val myClazz = ClassName(context.packageName, context.name(NameFlag.MODIFIER))
  val combinedClazz = ClassName(context.packageName, context.name(NameFlag.COMBINED))
  val delegatorClazz = ClassName(context.packageName, context.name(NameFlag.DELEGATE))

  val builder = TypeSpec.interfaceBuilder(myClazz.simpleName).apply {
    addProperty(
      ktProp("delegator", delegatorClazz) {
        mutable()
        addModifiers(KModifier.ABSTRACT)
      },
    )
    addFunctions(
      ktFun("getContributionNode") {
        addModifiers(KModifier.ABSTRACT)
        returns(MAP_MODIFIER_CONTRIBUTION_NODE_NULLABLE)
      },
      ktFun("fold") {
        addModifiers(KModifier.ABSTRACT)
        // default bounds is ANY_NULLABLE, but I don't want nullable type
        val r = TypeVariableName("R", bounds = arrayOf(ANY))
        addTypeVariable(r)
        addParameter("initial", r)
        addParameter(
          "operation",
          LambdaTypeName.get(
            receiver = null,
            parameters = arrayOf(r, myClazz),
            returnType = r,
          ),
        )
        returns(r)
      },
      ktFun("then") {
        addModifiers(KModifier.INFIX)
        addParameter("other", myClazz)
        returns(myClazz)
        addStatement(
          "return if (other·===·%T) this else %T(this, other)",
          myClazz,
          combinedClazz,
        )
      },
    )
  }

  val companionBuilder = TypeSpec.companionObjectBuilder().apply {
    addSuperinterface(myClazz)
    addProperty(
      ktProp("delegator", delegatorClazz) {
        mutable()
        addModifiers(KModifier.OVERRIDE)
        initializer(context.noopDelegator())
      },
    )
    addFunctions(
      ktFun("getContributionNode") {
        addModifiers(KModifier.OVERRIDE)
        returns(MAP_MODIFIER_CONTRIBUTION_NODE_NULLABLE)
        addStatement("return null")
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
            parameters = arrayOf(r, myClazz),
            returnType = r,
          ),
        )
        returns(r)
        addStatement("return initial")
      },
      ktFun("then") {
        addModifiers(KModifier.OVERRIDE, KModifier.INFIX)
        addParameter("other", myClazz)
        returns(myClazz)
        addStatement("return other")
      },
      ktFun("hashCode") {
        addModifiers(KModifier.OVERRIDE)
        returns(INT)
        addStatement("return %M(this)", System::class.member("identityHashCode"))
      },
      ktFun("equals") {
        addModifiers(KModifier.OVERRIDE)
        addParameter("other", ANY_NULLABLE)
        returns(BOOLEAN)
        addStatement("return this·===·other")
      },
      ktFun("toString") {
        addModifiers(KModifier.OVERRIDE)
        returns(STRING)
        addStatement("return %S", myClazz.simpleName)
      },
    )
  }

  return builder.addType(companionBuilder.build()).build()
}

internal fun ktCombinedModifier(context: GeneratorContext): TypeSpec {
  val myClazz = ClassName(context.packageName, context.name(NameFlag.COMBINED))
  val originalClazz = ClassName(context.packageName, context.name(NameFlag.MODIFIER))
  val delegatorClazz = ClassName(context.packageName, context.name(NameFlag.DELEGATE))

  return TypeSpec.classBuilder(myClazz.simpleName)
    .apply {
      addAnnotation(STABLE)
      primaryConstructor(
        FunSpec.constructorBuilder()
          .addParameter("outer", originalClazz)
          .addParameter("inner", originalClazz)
          .build(),
      )
      addSuperinterface(originalClazz)
      addProperties(
        ktProp("outer", originalClazz) {
          addModifiers(KModifier.PRIVATE)
          initializer("outer")
        },
        ktProp("inner", originalClazz) {
          addModifiers(KModifier.PRIVATE)
          initializer("inner")
        },
        ktProp("delegator", delegatorClazz) {
          mutable()
          addModifiers(KModifier.OVERRIDE)
          initializer(context.noopDelegator())
        },
      )
      addFunctions(
        ktFun("getContributionNode") {
          addModifiers(KModifier.OVERRIDE)
          returns(MAP_MODIFIER_CONTRIBUTION_NODE_NULLABLE)
          addStatement("return null")
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
              parameters = arrayOf(r, originalClazz),
              returnType = r,
            ),
          )
          returns(r)
          addStatement("return inner.fold(outer.fold(initial, operation), operation)")
        },
        ktFun("equals") {
          addModifiers(KModifier.OVERRIDE)
          addParameter("other", ANY_NULLABLE)
          returns(BOOLEAN)
          addStatement(
            "return other·is·%T && outer·==·other.outer && inner·==·other.inner",
            myClazz,
          )
        },
        ktFun("hashCode") {
          addModifiers(KModifier.OVERRIDE)
          returns(INT)
          addStatement("return outer.hashCode() + 31 * inner.hashCode()")
        },
        ktFun("toString") {
          addModifiers(KModifier.OVERRIDE)
          returns(STRING)
          addStatement(
            "return %S + fold(%S) { acc, element -> if (acc.isEmpty()) element.toString() else %P } + %S",
            "[",
            "",
            "\$acc, \$element",
            "]",
          )
        },
      )
    }
    .build()
}
