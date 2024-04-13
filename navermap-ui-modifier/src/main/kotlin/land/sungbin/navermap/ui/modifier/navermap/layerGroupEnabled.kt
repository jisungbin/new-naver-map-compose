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

@file:Suppress("RedundantVisibilityModifier")

package land.sungbin.navermap.ui.modifier.navermap

import androidx.compose.runtime.Stable
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.NaverMap
import land.sungbin.navermap.runtime.contributor.NaverMapContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.navermap.NaverMapDelegate.Companion.NoOp

@Stable
private data class NaverMapLayerGroupEnabledModifierNode(
  private val arg0: String,
  private val arg1: Boolean,
  override var delegator: NaverMapDelegate = NoOp,
) : NaverMapModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    NaverMapLayerGroupEnabledContributionNode(arg0, arg1, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, NaverMapModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class NaverMapLayerGroupEnabledContributionNode(
  public val arg0: String,
  public val arg1: Boolean,
  public val `delegate`: NaverMapDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = NaverMap

  override fun create(): Contributor = NaverMapLayerGroupEnabledContributor(arg0, arg1, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is NaverMapLayerGroupEnabledContributor)
    contributor.arg0 = arg0
    contributor.arg1 = arg1
    contributor.delegate = delegate
  }
}

private class NaverMapLayerGroupEnabledContributor(
  public var arg0: String,
  public var arg1: Boolean,
  public var `delegate`: NaverMapDelegate,
) : NaverMapContributor {
  override fun Any.contribute() {
    delegate.setLayerGroupEnabled(this, arg0, arg1)
  }
}

/**
 * See
 * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setLayerGroupEnabled(java.lang.String,boolean))
 */
@Stable
public fun NaverMapModifier.layerGroupEnabled(arg0: String, arg1: Boolean): NaverMapModifier =
  this then NaverMapLayerGroupEnabledModifierNode(arg0, arg1)
