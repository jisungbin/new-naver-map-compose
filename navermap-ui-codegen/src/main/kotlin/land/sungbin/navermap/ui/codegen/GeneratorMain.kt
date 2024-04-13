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

import com.squareup.kotlinpoet.FileSpec
import land.sungbin.navermap.ui.codegen.parser.findAllOverlayClasses
import land.sungbin.navermap.ui.codegen.parser.findNaverMapProperties
import java.nio.file.Paths
import java.util.logging.Logger

private const val BASE_UI_PACKAGE = "land.sungbin.navermap.ui"
private const val BASE_MODIFIER_PACKAGE = "land.sungbin.navermap.ui.modifier"

private val rootPath by lazy { System.getProperty("user.dir") }
private val uiModulePath by lazy { "$rootPath/navermap-ui/src/main/kotlin/" }
private val modifierModulePath by lazy { "$rootPath/navermap-ui-modifier/src/main/kotlin/" }

internal val logger by lazy { Logger.getLogger("ModifierGen") }

fun main() {
  generateNaverMapClass()
  generateAllOverlayClasses()
}

private fun generateNaverMapClass() {
  val properties = findNaverMapProperties()
  val pkg = "$BASE_MODIFIER_PACKAGE.navermap"
  val context = GeneratorContext(
    packageName = pkg,
    clazz = NAVER_MAP,
    constructors = emptyList(),
    methods = properties,
  )

  val delegate = ktDelegate(context)
  val realDelegate = ktRealDelegate(context)
  val delegateFile = FileSpec.builder(context.packageName, delegate.name!!)
    .addTypes(delegate, realDelegate)
    .addAnnotation(suppress("UsePropertyAccessSyntax"))
    .build()

  val modifier = ktModifier(context)
  val combinedModifier = ktCombinedModifier(context)

  val modifierNodes = properties.map { method -> ktModifierNode(method, context) }
  val modifierExtensions = properties.map { method -> ktModifierExtension(method, context) }
  val contributionNodes = properties.map { method -> ktContributionNode(method, context) }
  val contributors = properties.map { method -> ktContributor(method, context) }

  val modifierFile = FileSpec.builder(pkg, modifier.name!!).addTypes(modifier, combinedModifier).build()
  val contributorNodeFiles = List(properties.size) { index ->
    val modifierNode = modifierNodes[index]
    val modifierExtension = modifierExtensions[index]
    val contributuionNode = contributionNodes[index]
    val contributor = contributors[index]

    FileSpec.builder(pkg, modifierExtension.name)
      .addTypes(modifierNode, contributuionNode, contributor)
      .addFunction(modifierExtension)
      .addAnnotation(suppress("RedundantVisibilityModifier"))
      .build()
  }

  val compositionLocal = ktCompositionLocal(context)
  val compositionLocalFile =
    FileSpec.builder("$BASE_MODIFIER_PACKAGE.delegator", compositionLocal.name)
      .addProperty(compositionLocal)
      .build()

  val modifierPath = Paths.get(modifierModulePath)

  println("delegate file saved at ${delegateFile.writeTo(modifierPath).toString().removePrefix(modifierModulePath)}")
  println("modifier file saved at ${modifierFile.writeTo(modifierPath).toString().removePrefix(modifierModulePath)}")
  contributorNodeFiles.forEach { file ->
    println("[${file.name}] modifier file saved at ${file.writeTo(modifierPath).toString().removePrefix(modifierModulePath)}")
  }
  println("composition local file saved at ${compositionLocalFile.writeTo(modifierPath).toString().removePrefix(modifierModulePath)}")
  println("\n")
}

private fun generateAllOverlayClasses() {
  findAllOverlayClasses().forEach { overlayClass ->
    val current = overlayClass.name.simpleName
    val pkg = packageName(current.lowercase())
    val context = GeneratorContext(pkg, overlayClass)

    val delegate = ktDelegate(context)
    val realDelegate = ktRealDelegate(context)
    val delegateFile = FileSpec.builder(pkg, delegate.name!!)
      .addTypes(delegate, realDelegate)
      .addAnnotation(suppress("UsePropertyAccessSyntax"))
      .build()

    val modifier = ktModifier(context)
    val combinedModifier = ktCombinedModifier(context)

    val modifierNodes = overlayClass.setters.map { method -> ktModifierNode(method, context) }
    val modifierExtensions = overlayClass.setters.map { method -> ktModifierExtension(method, context) }
    val contributionNodes = overlayClass.setters.map { method -> ktContributionNode(method, context) }
    val contributors = overlayClass.setters.map { method -> ktContributor(method, context) }

    val modifierFile = FileSpec.builder(pkg, modifier.name!!).addTypes(modifier, combinedModifier).build()
    val contributorNodeFiles = List(overlayClass.setters.size) { index ->
      val modifierNode = modifierNodes[index]
      val modifierExtension = modifierExtensions[index]
      val contributuionNode = contributionNodes[index]
      val contributor = contributors[index]

      FileSpec.builder(pkg, modifierExtension.name)
        .addTypes(modifierNode, contributuionNode, contributor)
        .addFunction(modifierExtension)
        .addAnnotation(suppress("RedundantVisibilityModifier"))
        .build()
    }

    val compositionLocal = ktCompositionLocal(context)
    val compositionLocalFile =
      FileSpec.builder("$BASE_MODIFIER_PACKAGE.delegator", compositionLocal.name)
        .addProperty(compositionLocal)
        .build()

    val composableContentFile = ktContentComposable(
      contentPkg = "$BASE_UI_PACKAGE.content",
      compositionLocalPkg = compositionLocalFile.packageName,
      delegatorPkg = modifierFile.packageName,
      context = context,
    )

    val uiPath = Paths.get(uiModulePath)
    val modifierPath = Paths.get(modifierModulePath)

    println("delegate file saved at ${delegateFile.writeTo(modifierPath).toString().removePrefix(modifierModulePath)}")
    println("modifier file saved at ${modifierFile.writeTo(modifierPath).toString().removePrefix(modifierModulePath)}")
    contributorNodeFiles.forEach { file ->
      println("[${file.name}] modifier file saved at ${file.writeTo(modifierPath).toString().removePrefix(modifierModulePath)}")
    }
    println("composition local file saved at ${compositionLocalFile.writeTo(modifierPath).toString().removePrefix(modifierModulePath)}")
    println("composable content file saved at ${composableContentFile.writeTo(uiPath).toString().removePrefix(uiModulePath)}")
    println("\n")
  }
}

private fun packageName(name: String) = "$BASE_MODIFIER_PACKAGE.$name"
