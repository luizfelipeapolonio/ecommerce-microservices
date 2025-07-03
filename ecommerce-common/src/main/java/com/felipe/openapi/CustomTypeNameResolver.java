package com.felipe.openapi;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.jackson.TypeNameResolver;
import io.swagger.v3.core.util.PrimitiveType;
import org.apache.commons.text.WordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomTypeNameResolver extends TypeNameResolver {
  @Override
  protected String nameForGenericType(JavaType type, Set<Options> options) {
    final String classBaseName = nameForClass(type, options);
    final int count = type.containedTypeCount();

    final List<String> genericTypeNames = new ArrayList<>();
    for (int i = 0; i < count; ++i) {
      final JavaType arg = type.containedType(i);
      final String argName = PrimitiveType.fromType(arg) != null ? nameForClass(arg, options) :
        nameForType(arg, options);
      genericTypeNames.add(WordUtils.capitalize(argName));
    }
    String genericType = genericTypeNames.stream().collect(Collectors.joining(", ", "<", ">"));
    return classBaseName + genericType;
  }
}
