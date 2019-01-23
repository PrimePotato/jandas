package com.lchclearnet.jandas.io.parsers;

import com.lchclearnet.utils.VolNode;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A Parser implementation for Currency objects.
 */
public class VolNodeParser extends EnumParser<VolNode> {

    public VolNodeParser() {
        this((Iterable<String>) null);
    }

    public VolNodeParser(String missingValueStrings) {
        this(Arrays.stream(missingValueStrings.split(",")).map(s -> s.trim()).collect(Collectors.toList()));
    }

    public VolNodeParser(String[] missingValueStrings) {
        this(Arrays.asList(missingValueStrings));
    }

    public VolNodeParser(Iterable<String> missingValueStrings) {
        super(VolNode.class, missingValueStrings);
        enumMap.clear();
        for (VolNode volNode : VolNode.class.getEnumConstants()) {
            for (String value : volNode.getValues()) {
                enumMap.put(value.toUpperCase(), volNode);
            }
        }
    }

    @Override
    public boolean canParse(String value) {
        return true;
    }

    @Override
    public VolNode parse(String s) {
        if (isMissing(s)) return null;

        return enumMap.get(s.toUpperCase());
    }
}
