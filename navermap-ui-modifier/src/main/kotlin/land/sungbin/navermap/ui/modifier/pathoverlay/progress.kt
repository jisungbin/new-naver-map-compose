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

package land.sungbin.navermap.ui.modifier.pathoverlay

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlin.Any
import kotlin.Double
import land.sungbin.navermap.runtime.contributor.ContributionKind
import land.sungbin.navermap.runtime.contributor.Contributor
import land.sungbin.navermap.runtime.contributor.Contributors.Overlay
import land.sungbin.navermap.runtime.contributor.OverlayContributor
import land.sungbin.navermap.runtime.modifier.MapModifierContributionNode
import land.sungbin.navermap.ui.modifier.pathoverlay.PathOverlayDelegate.Companion.NoOp

@Immutable
private data class PathOverlayProgressModifierNode(
  private val arg0: Double,
  override var delegator: PathOverlayDelegate = NoOp,
) : PathOverlayModifier {
  override fun getContributionNode(): MapModifierContributionNode =
    PathOverlayProgressContributionNode(arg0, delegator)

  override fun <R : Any> fold(initial: R, operation: (R, PathOverlayModifier) -> R): R =
    operation(initial, this)
}

@Stable
private data class PathOverlayProgressContributionNode(
  public val arg0: Double,
  public val `delegate`: PathOverlayDelegate = NoOp,
) : MapModifierContributionNode {
  override val kindSet: ContributionKind = Overlay

  override fun create(): Contributor = PathOverlayProgressContributor(arg0, delegate)

  override fun update(contributor: Contributor) {
    require(contributor is PathOverlayProgressContributor)
    contributor.arg0 = arg0
    contributor.delegate = delegate
  }
}

private class PathOverlayProgressContributor(
  public var arg0: Double,
  public var `delegate`: PathOverlayDelegate,
) : OverlayContributor {
  override fun Any.contribute() {
    delegate.setProgress(this, arg0)
  }
}

@Stable
public fun PathOverlayModifier.progress(arg0: Double): PathOverlayModifier =
  this then PathOverlayProgressModifierNode(arg0)
