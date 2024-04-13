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
private data class NaverMapContentPaddingModifierNode(
  private val arg0: Int,
  private val arg1: Int,
  private val arg2: Int,
  private val arg3: Int,
  private val arg4: Boolean,
  override var delegator: NaverMapDelegate = NoOp,
) : NaverMapModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    NaverMapContentPaddingContributionNode(arg0, arg1, arg2, arg3, arg4, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, NaverMapModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class NaverMapContentPaddingContributionNode(
  public val arg0: Int,
  public val arg1: Int,
  public val arg2: Int,
  public val arg3: Int,
  public val arg4: Boolean,
  public val `delegate`: NaverMapDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = NaverMap

  override fun create(): Contributor = NaverMapContentPaddingContributor(
    arg0,
    arg1,
    arg2,
    arg3,
    arg4,
    delegate,
  )

  override fun update(contributor: Contributor) {
    require(contributor is NaverMapContentPaddingContributor)
    contributor.arg0 = arg0
    contributor.arg1 = arg1
    contributor.arg2 = arg2
    contributor.arg3 = arg3
    contributor.arg4 = arg4
    contributor.delegate = delegate
  }
}

private class NaverMapContentPaddingContributor(
  public var arg0: Int,
  public var arg1: Int,
  public var arg2: Int,
  public var arg3: Int,
  public var arg4: Boolean,
  public var `delegate`: NaverMapDelegate,
) : NaverMapContributor {
  override fun Any.contribute() {
    delegate.setContentPadding(this, arg0, arg1, arg2, arg3, arg4)
  }
}

/**
 * See
 * [official document](https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/NaverMap.html#setContentPadding(int,int,int,int,boolean))
 */
@Stable
public fun NaverMapModifier.contentPadding(
  arg0: Int,
  arg1: Int,
  arg2: Int,
  arg3: Int,
  arg4: Boolean,
): NaverMapModifier = this then NaverMapContentPaddingModifierNode(arg0, arg1, arg2, arg3, arg4)
