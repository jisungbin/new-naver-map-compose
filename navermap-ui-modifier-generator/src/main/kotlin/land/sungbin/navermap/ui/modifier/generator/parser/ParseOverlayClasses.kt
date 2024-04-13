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

package land.sungbin.navermap.ui.modifier.generator.parser

import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.naver.maps.map.overlay.Overlay
import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BYTE
import com.squareup.kotlinpoet.CHAR
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.asTypeName
import io.github.classgraph.ArrayTypeSignature
import io.github.classgraph.BaseTypeSignature
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassRefTypeSignature
import io.github.classgraph.MethodInfo
import io.github.classgraph.ScanResult
import io.github.classgraph.TypeSignature
import land.sungbin.navermap.ui.modifier.generator.logger

private val NULLABLE = androidx.annotation.Nullable::class.java

private object TypeNameStringAdapter : TypeAdapter<TypeName>() {
  fun ClassName.simpleName() = canonicalName + if (isNullable) "?" else ""

  fun ParameterizedTypeName.simpleName(): String {
    return rawType.canonicalName + typeArguments.joinToString(prefix = "<", postfix = ">") { type ->
      when (type) {
        is ClassName -> type.simpleName()
        is ParameterizedTypeName -> type.simpleName()
        else -> error("Unknown type: $type")
      }
    }
  }

  override fun write(writer: JsonWriter, value: TypeName?) {
    if (value != null) {
      val represent = when (value) {
        is ClassName -> value.simpleName()
        is ParameterizedTypeName -> value.simpleName()
        else -> error("Unknown type: $value")
      }
      writer.value(represent)
    } else {
      writer.nullValue()
    }
  }

  override fun read(reader: JsonReader): TypeName? {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull()
      return null
    }
    return ClassName.bestGuess(reader.nextString())
  }
}

private val GSON = GsonBuilder()
  .setPrettyPrinting()
  .registerTypeAdapter(TypeName::class.java, TypeNameStringAdapter)
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
    val parameters: List<Pair<String, TypeName>>,
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

private fun MethodInfo.parameters(): Map<String, TypeName> {
  // TODO: https://discuss.gradle.org/t/how-to-pass-parameters-option-to-javac-compiler-when-building-my-java-project-with-gradle/2106
  //  Add the `-parameters` argument to javac. I've tried everything, but strangely it doesn't
  //  work in my Android build environment. It works fine in pure Java projects!
  var argIndex = 0
  return parameterInfo.associate { param ->
    val nullable = param.hasAnnotation(NULLABLE)
    "arg${argIndex++}" to param.typeDescriptor.typed().copy(nullable = nullable)
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
  append(
    parameterInfo.joinToString(separator = ",", prefix = "(", postfix = ")") { param ->
      param.typeDescriptor.fqn(varargVisualizing = true)
    },
  )
}

private fun ClassName.useKoltinCollections() = if (this.canonicalName == "java.util.List") LIST else this

private fun TypeSignature.fqn(varargVisualizing: Boolean = false) = when (this) {
  is ArrayTypeSignature -> arrayClassInfo.name.let {
    if (it.endsWith("[]")) it.removeSuffix("[]") + (if (varargVisualizing) "..." else "")
    else it
  }
  is ClassRefTypeSignature -> fullyQualifiedClassName.replace('$', '.')
  is BaseTypeSignature -> typeStr
  else -> error("Unknown type: $this")
}

private fun String.typed(): ClassName =
  when {
    this == Void.TYPE.canonicalName -> UNIT
    this == Boolean::class.javaPrimitiveType!!.canonicalName -> BOOLEAN
    this == Byte::class.javaPrimitiveType!!.canonicalName -> BYTE
    this == Short::class.javaPrimitiveType!!.canonicalName -> SHORT
    this == Int::class.javaPrimitiveType!!.canonicalName -> INT
    this == Long::class.javaPrimitiveType!!.canonicalName -> LONG
    this == Char::class.javaPrimitiveType!!.canonicalName -> CHAR
    this == Float::class.javaPrimitiveType!!.canonicalName -> FLOAT
    this == Double::class.javaPrimitiveType!!.canonicalName -> DOUBLE
    else -> ClassName.bestGuess(this)
  }

private fun TypeSignature.typed(): TypeName = when (this) {
  is ArrayTypeSignature -> recursiveTyped().also {
    logger.warning(
      "An ArrayTypeSignature was found! You probably need to manually fix the type. " +
        "[${toStringWithSimpleNames()}]",
    )
  }
  is ClassRefTypeSignature -> (loadClass().asTypeName() as ClassName).useKotlinPrimitiveType()
  is BaseTypeSignature -> type.asTypeName()
  else -> error("Unknown type: $this")
}

// @Experimental
private fun ArrayTypeSignature.recursiveTyped(): TypeName {
  var current: TypeSignature = this
  val types = mutableListOf<ClassName>()
  while (current is ArrayTypeSignature) {
    types += current.arrayClassInfo.name.removeSuffix("[]").typed().useKoltinCollections()
    current = current.nestedType
  }
  types += current.typed() as ClassName
  return if (types.size > 1) types.dropLast(1).foldRight(types.last() as TypeName) { type, acc ->
    type.parameterizedBy(acc)
  } else types.first()
}

// Only java.util.List, java.lang.String, java.lang.String.
// We should also handle the java.util.List of ParameterizedTypeName, but due to its complexity, we do it manually.
private fun ClassName.useKotlinPrimitiveType(): ClassName =
  when (canonicalName) {
    "java.lang.String" -> STRING
    "java.lang.Object" -> ANY
    else -> this
  }
