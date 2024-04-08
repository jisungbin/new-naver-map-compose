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

package land.sungbin.navermap.runtime.contributor

import androidx.compose.runtime.Stable

/**
 * TODO: Write a description of class [Contributor] here.
 *
 * `delegate: Owner`: 만약 null이 아닌 값이 반환되면 해당 값을 노드 생성 대신 사용합니다.
 * `Owner.contribute()`: 주어진 타입의 노드가 생성됐을 때 Owner와 함께 호출됩니다.
 */
@Stable
public sealed interface Contributor
