package com.jeeagile.core.util;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 数组工具类
 */
public class ArrayUtil {

    private ArrayUtil() {
    }

    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
    public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
    public static final Field[] EMPTY_FIELD_ARRAY = new Field[0];
    public static final Method[] EMPTY_METHOD_ARRAY = new Method[0];
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final Throwable[] EMPTY_THROWABLE_ARRAY = new Throwable[0];
    public static final Type[] EMPTY_TYPE_ARRAY = new Type[0];
    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(short[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(boolean[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空<br>
     * 此方法会匹配单一对象，如果此对象为{@code null}则返回true<br>
     * 如果此对象为非数组，理解为此对象为数组的第一个元素，则返回false<br>
     * 如果此对象为数组对象，数组长度大于0情况下返回false，否则返回true
     *
     * @param array 数组
     * @return 是否为空
     */
    public static <T> boolean isEmpty(T[] array) {
        return ArrayUtils.isEmpty(array);
    }

    /**
     * 数组是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(long[] array) {
        return !isEmpty(array);
    }

    /**
     * 数组是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(int[] array) {
        return !isEmpty(array);
    }

    /**
     * 数组是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(short[] array) {
        return !isEmpty(array);
    }

    /**
     * 数组是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(char[] array) {
        return !isEmpty(array);
    }

    /**
     * 数组是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(byte[] array) {
        return !isEmpty(array);
    }

    /**
     * 数组是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(double[] array) {
        return !isEmpty(array);
    }

    /**
     * 数组是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(float[] array) {
        return !isEmpty(array);
    }

    /**
     * 数组是否为非空
     *
     * @param array 数组
     * @return 是否为非空
     */
    public static boolean isNotEmpty(boolean[] array) {
        return !isEmpty(array);
    }

    /**
     * 数组是否为非空
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为非空
     */
    public static <T> boolean isNotEmpty(T[] array) {
        return !ArrayUtils.isEmpty(array);
    }

    /**
     * 对象是否为数组对象
     *
     * @param obj 对象
     * @return 是否为数组对象，如果为{@code null} 返回false
     */
    public static boolean isArray(Object obj) {
        if (null == obj) {
            return false;
        }
        return obj.getClass().isArray();
    }

    /**
     * 数组中是否包含元素
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @param value 被检查的元素
     * @return 是否包含
     */
    public static <T> boolean contains(T[] array, T value) {
        return ArrayUtils.contains(array, value);
    }

    /**
     * 生成一个新的重新设置大小的数组<br>
     * 调整大小后拷贝原数组到新数组下。扩大则占位前N个位置，其它位置补充0，缩小则截断
     *
     * @param bytes   原数组
     * @param newSize 新的数组大小
     * @return 调整后的新数组
     */
    public static byte[] resize(byte[] bytes, int newSize) {
        if (newSize < 0) {
            return bytes;
        }
        final byte[] newArray = new byte[newSize];
        if (newSize > 0 && isNotEmpty(bytes)) {
            System.arraycopy(bytes, 0, newArray, 0, Math.min(bytes.length, newSize));
        }
        return newArray;
    }
}
