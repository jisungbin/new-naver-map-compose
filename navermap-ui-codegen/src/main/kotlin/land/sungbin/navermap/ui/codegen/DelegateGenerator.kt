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

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.ARRAY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeSpec

internal fun ktDelegate(context: GeneratorContext): TypeSpec {
  val myClazz = ClassName(context.packageName, context.name(NameFlag.DELEGATE))

  val builder = TypeSpec.interfaceBuilder(myClazz.simpleName)
    .apply {
      context.methods.forEach { method ->
        // The default delegate has a `{}` default implementation to make it ready for testing.
        val methodFun = ktFun(method.name) {
          addKdoc("See [official·document](${method.javadocLink})")
          addParameter("instance", DELEGATED_OVERLAY)
          method.parameters.forEach { (name, type) ->
            addParameter(name, if (type.isNullable) ANY_NULLABLE else ANY)
          }
        }
        addFunction(methodFun)
      }
    }
  val companion = TypeSpec.companionObjectBuilder()
    .addProperty(
      ktProp(context.name(NameFlag.NOOP), myClazz) {
        initializer("object·:·%T·{}", myClazz)
      },
    )
    .build()

  return builder.addType(companion).build()
}

internal fun ktRealDelegate(context: GeneratorContext): TypeSpec {
  val delegatorClazz = ClassName(context.packageName, context.name(NameFlag.DELEGATE))

  return TypeSpec.objectBuilder(context.name(NameFlag.REAL_DELEGATE))
    .apply {
      addSuperinterface(delegatorClazz)
      context.methods.forEach { method ->
        val methodFun = ktFun(method.name) {
          if (method.parameters.any { (_, type) -> type is ParameterizedTypeName }) {
            addAnnotation(suppress("CANNOT_CHECK_FOR_ERASED"))
          }
          if (method.deprecated) {
            addAnnotation(suppress("DEPRECATION"))
          }
          addModifiers(KModifier.OVERRIDE)
          addParameter("instance", DELEGATED_OVERLAY)
          method.parameters.forEach { (name, type) ->
            addParameter(name, if (type.isNullable) ANY_NULLABLE else ANY)
          }
          addStatement("require(instance·is·%T)", context.clazz)
          method.parameters.forEach { (name, type) ->
            if (type != ANY_NULLABLE) addStatement("require(%L·is·%T)", name, type)
          }
          addStatement(
            "instance.%L(%L)",
            method.name,
            method.parameters.joinToString { (name, type) ->
              val isVararg = (
                (type is ClassName && type.simpleName.endsWith("Array")) ||
                  (type is ParameterizedTypeName && type.rawType == ARRAY)
                )
              (if (isVararg) "*" else "") + name
            },
          )
        }
        addFunction(methodFun)
      }
    }
    .build()
}
