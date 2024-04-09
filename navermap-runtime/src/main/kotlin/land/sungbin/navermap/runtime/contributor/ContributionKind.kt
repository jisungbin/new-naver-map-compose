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

@file:Suppress("NOTHING_TO_INLINE")

package land.sungbin.navermap.runtime.contributor

@JvmInline
public value class ContributionKind @PublishedApi internal constructor(public val mask: Int) {
  internal inline infix fun or(other: Int): Int = mask or other
  internal inline infix fun or(other: ContributionKind): ContributionKind = ContributionKind(mask or other.mask)
  public inline operator fun plus(other: ContributionKind): ContributionKind = ContributionKind(mask or other.mask)
}

internal inline infix fun Int.or(other: ContributionKind): Int = this or other.mask
internal inline operator fun Int.contains(value: ContributionKind): Boolean = this and value.mask != 0
internal inline operator fun ContributionKind.contains(value: ContributionKind): Boolean = this.mask and value.mask != 0

// NOTE: NEVER CHANGE THE VALUE OF THIS BITWISE OPERATION.
public object Contributors {
  internal inline val Any get() = ContributionKind(0b1 shl 0)
  public inline val MapView: ContributionKind get() = ContributionKind(0b1 shl 1)
  public inline val NaverMap: ContributionKind get() = ContributionKind(0b1 shl 2)
  public inline val Overlay: ContributionKind get() = ContributionKind(0b1 shl 3)
}
