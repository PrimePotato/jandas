package com.lchclearnet.utils;

public class ArrayHelper {

    public static int firstIndexOf(String key, String[] array) {
        for (int i = 0; i < array.length; ++i) if (key.equals(array[i])) return i;
        return -1;
    }

    public static int lastIndexOf(String key, String[] array) {
        int lastIndex = -1;
        for (int i = 0; i < array.length; ++i) if (key.equals(array[i])) lastIndex = i;
        return lastIndex;
    }

    public static int firstIndexOf(int key, int[] array) {
        for (int i = 0; i < array.length; ++i) if (key == array[i]) return i;
        return -1;
    }

    public static int lastIndexOf(int key, int[] array) {
        int lastIndex = -1;
        for (int i = 0; i < array.length; ++i) if (key == array[i]) lastIndex = i;
        return lastIndex;
    }

    public static int firstIndexOf(long key, long[] array) {
        for (int i = 0; i < array.length; ++i) if (key == array[i]) return i;
        return -1;
    }

    public static int lastIndexOf(long key, long[] array) {
        int lastIndex = -1;
        for (int i = 0; i < array.length; ++i) if (key == array[i]) lastIndex = i;
        return lastIndex;
    }

    public static int firstIndexOf(float key, float[] array) {
        for (int i = 0; i < array.length; ++i) if (key == array[i]) return i;
        return -1;
    }

    public static int lastIndexOf(float key, float[] array) {
        int lastIndex = -1;
        for (int i = 0; i < array.length; ++i) if (key == array[i]) lastIndex = i;
        return lastIndex;
    }

    public static int firstIndexOf(double key, double[] array) {
        for (int i = 0; i < array.length; ++i) if (key == array[i]) return i;
        return -1;
    }

    public static int lastIndexOf(double key, double[] array) {
        int lastIndex = -1;
        for (int i = 0; i < array.length; ++i) if (key == array[i]) lastIndex = i;
        return lastIndex;
    }
}
