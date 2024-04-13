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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.asClassName
import land.sungbin.navermap.runtime.MapApplier
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.delegate.OverlayDelegator
import land.sungbin.navermap.runtime.modifier.MapModifier
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.runtime.node.DelegatedNaverMap
import land.sungbin.navermap.runtime.node.DelegatedOverlay
import land.sungbin.navermap.runtime.node.OverlayNode
import land.sungbin.navermap.ui.NaverMapContent

internal val ANY_NULLABLE = ANY.copy(nullable = true)
internal val LAMBDA_UNIT_NULLABLE =
  LambdaTypeName.get(
    receiver = null,
    parameters = emptyList(),
    returnType = UNIT,
  ).copy(nullable = true)

internal val MAP_MODIFIER = MapModifier::class.asClassName()
internal val NAVER_MAP_CONTENT = NaverMapContent::class.asClassName()

internal val MAP_MODIFIER_CONTRIBUTION_NODE = MapModifierContributionNode::class.asClassName()
internal val MAP_MODIFIER_CONTRIBUTION_NODE_NULLABLE = MAP_MODIFIER_CONTRIBUTION_NODE.copy(nullable = true)

internal val OVERLAY_DELEGATOR = OverlayDelegator::class.asClassName()
internal val OVERLAY_CONTRIBUTOR = OverlayContributor::class.asClassName()
internal val DELEGATED_OVERLAY = DelegatedOverlay::class.asClassName()
internal val DELEGATED_NAVER_MAP_NULLABLE = DelegatedNaverMap::class.asClassName().copy(nullable = true)

internal val CONTRIBUTOR = Contributor::class.asClassName()
internal val CONTRIBUTORS = Contributors::class.asClassName()
internal val CONTRIBUTORS_OVERLAY = CONTRIBUTORS.member("Overlay")
internal val CONTRIBUTION_KIND = ContributionKind::class.asClassName()

internal val COMPOSABLE = Composable::class.asClassName()
internal val STABLE = Stable::class.asClassName()
internal val COMPOSE_NODE = ClassName("androidx.compose.runtime", "ComposeNode")
internal val OVERLAY_NODE = OverlayNode::class.asClassName()
internal val MAP_APPLIER = MapApplier::class.asClassName()

internal val PROVIDABLE_COMPOSITION_LOCAL = ProvidableCompositionLocal::class.asClassName()
internal val COMPOSITION_LOCAL_OF = ClassName("androidx.compose.runtime", "staticCompositionLocalOf")
