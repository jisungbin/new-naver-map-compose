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
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.buildCodeBlock
import land.sungbin.navermap.ui.codegen.parser.OverlayClass

@Suppress("ClassName")
internal sealed interface NameFlag {
  sealed interface NoName

  data object NOOP : NameFlag, NoName
  data object COMBINED : NameFlag, NoName
  data object DELEGATE : NameFlag, NoName
  data object REAL_DELEGATE : NameFlag, NoName
  data object MODIFIER : NameFlag, NoName
  data object MODIFIER_NODE : NameFlag
  data object MODIFIER_EXTENSION : NameFlag
  data object CONTRIBUTOR : NameFlag
  data object CONTRIBUTION_NODE : NameFlag
  data object COMPOSITION_LOCAL : NameFlag, NoName
}

internal class GeneratorContext(
  val packageName: String,
  val overlayClass: ClassName,
  val overlayMethods: List<OverlayClass.Method>,
) {
  constructor(
    packageName: String,
    overlayResult: OverlayClass,
  ) : this(
    packageName = packageName,
    overlayClass = overlayResult.name,
    overlayMethods = overlayResult.setters,
  )

  fun name(flag: NameFlag.NoName): String {
    require(flag is NameFlag)
    return normalizeName("", flag)
  }

  fun normalizeName(name: String, flag: NameFlag) =
    when (flag) {
      NameFlag.NOOP -> "NoOp" // MarkerDelegate.Companion.[NoOp]
      NameFlag.DELEGATE -> "${overlayClass.simpleName}Delegate" // MarkerDelegate
      NameFlag.REAL_DELEGATE -> "Real${overlayClass.simpleName}Delegate" // RealMarkerDelegate
      NameFlag.COMBINED -> "Combined${overlayClass.simpleName}Modifier" // CombinedOverlayModifier
      NameFlag.MODIFIER -> "${overlayClass.simpleName}Modifier" // OverlayModifier
      NameFlag.MODIFIER_NODE -> "${overlayClass.simpleName}${name.normalizeUppercase()}ModifierNode" // MarkerLatLngModifierNode
      NameFlag.MODIFIER_EXTENSION -> name.normalizeLowercase() // MarkerModifier.[offset]
      NameFlag.CONTRIBUTOR -> "${overlayClass.simpleName}${name.normalizeUppercase()}Contributor" // MarkerLatLngContributor
      NameFlag.CONTRIBUTION_NODE -> "${overlayClass.simpleName}${name.normalizeUppercase()}ContributionNode" // MarkerLatLngContributionNode
      NameFlag.COMPOSITION_LOCAL -> "Local${overlayClass.simpleName}Delegator" // LocalMarkerDelegator
    }

  fun noopDelegator() = buildCodeBlock {
    val delegator = ClassName(packageName, name(NameFlag.DELEGATE))
    addStatement("%M", delegator.nestedClass("Companion").member(name(NameFlag.NOOP)))
  }
}

private fun String.normalizeUppercase() = removePrefix("set").replaceFirstChar(Char::uppercaseChar)
private fun String.normalizeLowercase() = removePrefix("set").replaceFirstChar(Char::lowercaseChar)
