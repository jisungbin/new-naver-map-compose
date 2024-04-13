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
@file:Suppress("UnstableApiUsage")

plugins {
  id("com.android.library")
  kotlin("android")
}

android {
  namespace = "land.sungbin.navermap.ui.codegen"
  compileSdk = 34

  defaultConfig {
    minSdk = 31
  }

  sourceSets {
    getByName("main").java.srcDir("src/main/kotlin")
    getByName("test").java.srcDir("src/main/kotlin")
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  testOptions.unitTests {
    all { test ->
      test.useJUnitPlatform()
    }
  }
}

dependencies {
  implementation(libs.navermap)
  implementation(libs.classgraph)
  implementation(libs.kotlinpoet)
  implementation(libs.compose.runtime)

  implementation(projects.navermapRuntime)
  implementation(projects.navermapUi)

  // noinspection UseTomlInstead (debug only)
  implementation("com.google.code.gson:gson:2.10.1")

  testImplementation(kotlin("test"))
}
