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

import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.naver.maps.map.overlay.Overlay
import com.squareup.kotlinpoet.ARRAY
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BYTE
import com.squareup.kotlinpoet.CHAR
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.UNIT
import io.github.classgraph.ArrayTypeSignature
import io.github.classgraph.BaseTypeSignature
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassRefTypeSignature
import io.github.classgraph.MethodInfo
import io.github.classgraph.ScanResult
import io.github.classgraph.TypeSignature
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

private val NULLABLE = androidx.annotation.Nullable::class.java

private object ClassNameSimpleTypeAdapter : TypeAdapter<ClassName>() {
  override fun write(writer: JsonWriter, value: ClassName?) {
    writer.value(value?.canonicalName + if (value?.isNullable == true) "?" else "")
  }

  override fun read(reader: JsonReader): ClassName? {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull()
      return null
    }
    return ClassName.bestGuess(reader.nextString())
  }
}

@OptIn(ExperimentalStdlibApi::class)
private val GSON = GsonBuilder()
  .setPrettyPrinting()
  .registerTypeAdapter(typeOf<ClassName>().javaType, ClassNameSimpleTypeAdapter)
  .create()

fun main() {
  print(GSON.toJson(findOverlayBaseMethods()))
  print(GSON.toJson(findAllOverlayClasses()))
}

data class OverlayClass(
  val name: ClassName,
  val setters: List<Method>,
) {
  data class Method(
    val name: String,
    val parameters: List<Pair<String, ClassName>>,
    val javadocLink: String,
  )
}

fun findOverlayBaseMethods(): List<OverlayClass.Method> {
  val scanResult: ScanResult
  val overlay = ClassGraph()
    .enableMethodInfo()
    .enableAnnotationInfo()
    .acceptPackages(Overlay::class.java.packageName)
    .scan().also { scanResult = it }
    .getClassInfo(Overlay::class.java.name)

  val methods = overlay.methodInfo
    .filter { it.isSetter() }
    .map { method ->
      OverlayClass.Method(
        name = method.name,
        parameters = method.parameters().toList(),
        javadocLink = method.javadocLink(),
      )
    }

  scanResult.close()
  return methods
}

fun findAllOverlayClasses(): List<OverlayClass> {
  val scanResult: ScanResult
  val overlayClasses = ClassGraph()
    .enableMethodInfo()
    .enableAnnotationInfo()
    .acceptPackages(Overlay::class.java.packageName)
    .scan().also { scanResult = it }
    .getSubclasses(Overlay::class.java.name)
    .filterNot { it.isAbstract }

  val founds = overlayClasses.map { clazz ->
    val setters = clazz.methodInfo.filter { it.isSetter() }

    OverlayClass(
      name = ClassName(clazz.packageName, clazz.simpleName),
      setters = setters.map { method ->
        OverlayClass.Method(
          name = method.name,
          parameters = method.parameters().toList(),
          javadocLink = method.javadocLink(),
        )
      },
    )
  }

  scanResult.close()
  return founds
}

private fun MethodInfo.isSetter(): Boolean =
  name.startsWith("set") && (typeDescriptor.resultType as? BaseTypeSignature)?.type === Void.TYPE

private fun MethodInfo.parameters(): Map<String, ClassName> {
  // TODO: https://discuss.gradle.org/t/how-to-pass-parameters-option-to-javac-compiler-when-building-my-java-project-with-gradle/2106
  //  Add the `-parameters` argument to javac. I've tried everything, but strangely it doesn't
  //  work in my Android build environment. It works fine in pure Java projects!
  var argIndex = 0
  return parameterInfo.associate { param ->
    val nullable = param.hasAnnotation(NULLABLE)
    val type = when (val type = param.typeDescriptor) {
      is ArrayTypeSignature -> {
        type.arrayClassInfo.loadClass().primitiveClassNameOrNull()
          ?: ClassName.bestGuess(type.arrayClassInfo.name.removeSuffix("[]"))
      }
      is ClassRefTypeSignature -> ClassName.bestGuess(type.fqn())
      is BaseTypeSignature -> type.type.primitiveClassNameOrNull()!!
      else -> error("Unknown type: $type")
    }
    "arg${argIndex++}" to type.copy(nullable = nullable) as ClassName
  }
}

// https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Overlay.html#setMaxZoom(double)
// https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/Marker.html#setCaptionAligns(com.naver.maps.map.overlay.Align...)
// https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/InfoWindow.html#open(com.naver.maps.map.overlay.Marker,com.naver.maps.map.overlay.Align)
// https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/PolylineOverlay.html#setCoords(java.util.List)
private fun MethodInfo.javadocLink() = buildString {
  append("https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/")
  append("${classInfo.simpleName}.html")
  append("#$name")
  append(parameterInfo.joinToString(separator = ",", prefix = "(", postfix = ")") { param -> param.typeDescriptor.fqn() })
}

private fun TypeSignature.fqn() = when (this) {
  is ArrayTypeSignature -> arrayClassInfo.name.let { if (it.endsWith("[]")) it.removeSuffix("[]") + "..." else it }
  is ClassRefTypeSignature -> fullyQualifiedClassName.replace('$', '.')
  is BaseTypeSignature -> typeStr
  else -> error("Unknown type: $this")
}

private fun Class<*>.primitiveClassNameOrNull() = when {
  this === Void.TYPE -> UNIT
  this === Boolean::class.javaPrimitiveType -> BOOLEAN
  this === Byte::class.javaPrimitiveType -> BYTE
  this === Short::class.javaPrimitiveType -> SHORT
  this === Int::class.javaPrimitiveType -> INT
  this === Long::class.javaPrimitiveType -> LONG
  this === Char::class.javaPrimitiveType -> CHAR
  this === Float::class.javaPrimitiveType -> FLOAT
  this === Double::class.javaPrimitiveType -> DOUBLE
  isArray -> ARRAY
  else -> null
}
