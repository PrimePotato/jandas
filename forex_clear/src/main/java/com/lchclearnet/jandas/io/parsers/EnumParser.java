package com.lchclearnet.jandas.io.parsers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EnumParser<T extends Enum> extends AbstractParser<T> {

    private final Class<T> enumClass;

    protected Map<String, T> enumMap = new HashMap<>();


    public EnumParser(Class<T> enumClass) {
        this(enumClass, (Iterable<String>) null);
    }

    public EnumParser(Class<T> enumClass, String missingValueStrings) {
        this(enumClass, Arrays.stream(missingValueStrings.split(",")).map(s -> s.trim()).collect(Collectors.toList()));
    }

    public EnumParser(Class<T> enumClass, String[] missingValueStrings) {
        this(enumClass, Arrays.asList(missingValueStrings));
    }

    public EnumParser(Class<T> enumClass, Iterable<String> missingValueStrings) {
        super(missingValueStrings);
        this.enumClass = enumClass;
        for (T value : enumClass.getEnumConstants()) {
            enumMap.put(value.name().toUpperCase(), value);
        }
    }

    @Override
    public boolean canParse(String value) {
        return true;
    }

    @Override
    public T parse(String s) {
        if (isMissing(s)) return null;
        return enumMap.get(s.toUpperCase());
    }


  @Override
  public Class<T> elementClass() {
      return enumClass;
  };
}

