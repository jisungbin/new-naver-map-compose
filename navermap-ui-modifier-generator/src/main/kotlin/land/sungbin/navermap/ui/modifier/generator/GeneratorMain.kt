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

import com.squareup.kotlinpoet.FileSpec
import land.sungbin.navermap.ui.modifier.generator.parse.findAllOverlayClasses
import java.nio.file.Paths
import java.util.logging.Logger

private const val BASE_PACKAGE_NAME = "land.sungbin.navermap.ui.modifier"

private val rootPath by lazy { System.getProperty("user.dir") }
private val modifierModulePath by lazy { "$rootPath/navermap-ui-modifier/src/main/kotlin/" }

internal val logger by lazy { Logger.getLogger("ModifierGen") }

fun main() = findAllOverlayClasses().forEach { overlayClass ->
  val current = overlayClass.name.simpleName
  val pkg = packageName(current.lowercase())
  val context = GeneratorContext(pkg, overlayClass)

  val delegate = ktDelegate(context)
  val realDeleage = ktRealDelegate(context)
  val delegateFile = FileSpec.builder(pkg, delegate.name!!).addTypes(delegate, realDeleage).build()

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
      .build()
  }

  val path = Paths.get(modifierModulePath)
  println("delegate file saved at ${delegateFile.writeTo(path).toString().removePrefix(modifierModulePath)}")
  println("modifier file saved at ${modifierFile.writeTo(path).toString().removePrefix(modifierModulePath)}")
  contributorNodeFiles.forEach { file ->
    println("[${file.name}] modifier file saved at ${file.writeTo(path).toString().removePrefix(modifierModulePath)}")
  }
  println("\n")
}

private fun packageName(name: String) = "$BASE_PACKAGE_NAME.$name"
