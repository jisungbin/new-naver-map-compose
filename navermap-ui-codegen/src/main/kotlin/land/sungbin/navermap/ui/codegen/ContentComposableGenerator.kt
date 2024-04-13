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

import androidx.annotation.VisibleForTesting
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.withIndent
import land.sungbin.navermap.ui.codegen.parser.NaverMapClass

internal fun ktContentComposable(
  contentPkg: String,
  compositionLocalPkg: String,
  delegatorPkg: String,
  context: GeneratorContext,
): FileSpec {
  val modifierMapper = ktMapModifierMapper(context)
  val composables = context.constructors.map { constructor ->
    ktComposableContent(context, compositionLocalPkg, delegatorPkg, constructor)
  }

  return FileSpec.builder(contentPkg, context.name(NameFlag.CONTENT_COMPOSABLE_FILE))
    .apply {
      composables.forEach(::addFunction)
      addFunction(modifierMapper)
    }
    .build()
}

@VisibleForTesting
internal fun ktComposableContent(
  context: GeneratorContext,
  compositionLocalPkg: String,
  delegatorPkg: String,
  constructor: NaverMapClass.Method,
): FunSpec {
  val modifierClazz = ClassName(context.packageName, context.name(NameFlag.MODIFIER))
  val delegator = ktDelegator(compositionLocalPkg, delegatorPkg, context)

  return ktFun(context.clazz.simpleName) {
    addAnnotation(COMPOSABLE)
    addAnnotation(suppress("UnusedReceiverParameter"))
    if (constructor.deprecated) addAnnotation(deprecated())
    addKdoc("See [official·document](${constructor.javadocLink})")
    receiver(NAVER_MAP_CONTENT)
    constructor.parameters.map { (name, type) -> addParameter(name, type) }
    addParameter(
      ParameterSpec.builder("modifier", modifierClazz)
        .defaultValue("%T", modifierClazz)
        .build(),
    )
    addCode(
      buildCodeBlock {
        add("%L", delegator)
        addStatement("")
        val overlayDelegator = ktOverlayDelegatorObject(context.clazz, constructor)
        val updateBlock = ktComposeNodeUpdate(context)
        add(ktComposeNode(context, factory = CodeBlock.of("%L", overlayDelegator), update = updateBlock))
      },
    )
  }
}

@VisibleForTesting
internal fun ktDelegator(
  compositionLocalPkg: String,
  delegatorPkg: String,
  context: GeneratorContext,
): PropertySpec {
  val type = ClassName(compositionLocalPkg, context.name(NameFlag.COMPOSITION_LOCAL))
  val delegator = ClassName(delegatorPkg, context.name(NameFlag.DELEGATE))
  return ktProp("delegator", delegator) { initializer("%T.current", type) }
}

@VisibleForTesting
internal fun ktComposeNode(
  context: GeneratorContext,
  factory: CodeBlock,
  update: CodeBlock,
) = buildCodeBlock {
  addStatement("%T<%T, %T>(", COMPOSE_NODE, OVERLAY_NODE, MAP_APPLIER)
  indent()
  addStatement("factory = {")
  withIndent {
    addStatement("%T(", OVERLAY_NODE)
    indent()
    addStatement(
      "modifier = modifier.%L(delegator),",
      context.name(NameFlag.MAP_MODIFIER_MAPPER),
    )
    addStatement("factory = { %L },", factory)
    unindent()
    addStatement(")")
  }
  addStatement("},")
  addStatement("update = {")
  withIndent { add(update) }
  addStatement("},")
  unindent()
  addStatement(")")
}

@VisibleForTesting
internal fun ktOverlayDelegatorObject(clazz: ClassName, constructor: NaverMapClass.Method): TypeSpec =
  TypeSpec.anonymousClassBuilder()
    .addSuperinterface(OVERLAY_DELEGATOR)
    .addProperty(
      ktProp("instance", DELEGATED_OVERLAY) {
        addModifiers(KModifier.OVERRIDE)
        initializer(ktContentConstructor(clazz, constructor))
      },
    )
    .addFunction(
      ktFun("setMap") {
        addModifiers(KModifier.OVERRIDE)
        addParameter("map", DELEGATED_NAVER_MAP_NULLABLE)
        addStatement("delegator.setMap(instance, map)")
      },
    )
    .build()

@VisibleForTesting
internal fun ktContentConstructor(clazz: ClassName, constructor: NaverMapClass.Method): CodeBlock =
  buildCodeBlock {
    addStatement("%L(", clazz.canonicalName)
    withIndent {
      val parameterSize = constructor.parameters.size
      repeat(parameterSize) { index ->
        val (name) = constructor.parameters[index]
        addStatement("%L%L", name, if (index < parameterSize - 1) ", " else "")
      }
    }
    addStatement(")")
  }

@VisibleForTesting
internal fun ktComposeNodeUpdate(context: GeneratorContext): CodeBlock =
  buildCodeBlock {
    addStatement("update(modifier) {")
    withIndent {
      addStatement(
        "this.modifier = it.%L(delegator)",
        context.name(NameFlag.MAP_MODIFIER_MAPPER),
      )
    }
    addStatement("}")
  }

@VisibleForTesting
internal fun ktMapModifierMapper(context: GeneratorContext): FunSpec =
  ktFun(context.name(NameFlag.MAP_MODIFIER_MAPPER)) {
    addAnnotation(STABLE)
    addModifiers(KModifier.PRIVATE)
    receiver(ClassName(context.packageName, context.name(NameFlag.MODIFIER)))
    addParameter("delegator", ClassName(context.packageName, context.name(NameFlag.DELEGATE)))
    returns(MAP_MODIFIER)
    addCode(
      "return %L",
      buildCodeBlock {
        beginControlFlow("fold<%T>(%T)·{ acc, element ->", MAP_MODIFIER, MAP_MODIFIER)
        addStatement("element.delegator = delegator")
        addStatement("val node = element.getContributionNode() ?: return@fold acc")
        addStatement("acc then node")
        endControlFlow()
      },
    )
  }
