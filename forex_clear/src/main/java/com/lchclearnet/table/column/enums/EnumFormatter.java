package com.lchclearnet.table.column.enums;

public class EnumFormatter<T extends Enum> {

    private String missingString = "";

    public EnumFormatter() {
    }

    public EnumFormatter(String missingString) {

        this.missingString = missingString;
    }

    public String format(T value) {
        return value != null ? String.valueOf(value) : missingString;
    }

    @Override
    public String toString() {
        return String.format("EnumFormatter{ missingString='%s'}", missingString);
    }
}
