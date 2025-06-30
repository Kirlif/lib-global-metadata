/*
 *  Copyright (C) 2022 github.com/REAndroid
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reandroid.unity.metadata.util;

import com.reandroid.utils.CompareUtil;
import com.reandroid.utils.HexUtil;
import com.reandroid.utils.NumbersUtil;

public class CommonUtil {

    public static int compareBytes(byte[] bytes1, byte[] bytes2) {
        if (bytes1 == bytes2) {
            return 0;
        }
        int length1 = bytes1.length;
        int length2 = bytes2.length;
        int length = NumbersUtil.min(length1, length2);
        for (int i = 0; i < length; i++) {
            int c = CompareUtil.compare(bytes1[i] & 0xff, bytes2[i] & 0xff);
            if (c != 0) {
                return c;
            }
        }
        return CompareUtil.compare(length1, length2);
    }
    public static int hash(byte[] bytes) {
        if (bytes == null) {
            return 0;
        }
        int hash = 31;
        for (byte b : bytes) {
            hash = hash + (b & 0xff) * 31;
        }
        return hash;
    }
    public static String joinArray(byte[] array) {
        return joinArray(array, ", ", false);
    }
    public static String joinArray(byte[] array, boolean hex) {
        return joinArray(array, ", ", hex);
    }
    public static String joinArray(byte[] array, Object separator) {
        return joinArray(array, separator, false);
    }
    public static String joinArray(byte[] array, Object separator, boolean hex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }
        StringBuilder builder = new StringBuilder();
        int length = array.length;
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                builder.append(separator);
            }
            byte val = array[i];
            if (hex) {
                builder.append(HexUtil.toHex2(val));
            } else {
                builder.append(val);
            }
        }
        return builder.toString();
    }

    public static String joinArray(short[] array) {
        return joinArray(array, ", ", false);
    }
    public static String joinArray(short[] array, boolean hex) {
        return joinArray(array, ", ", hex);
    }
    public static String joinArray(short[] array, Object separator) {
        return joinArray(array, separator, false);
    }
    public static String joinArray(short[] array, Object separator, boolean hex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }
        StringBuilder builder = new StringBuilder();
        int length = array.length;
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                builder.append(separator);
            }
            short val = array[i];
            if (hex) {
                builder.append(HexUtil.toHex4(val));
            } else {
                builder.append(val);
            }
        }
        return builder.toString();
    }

    public static String joinArray(int[] array) {
        return joinArray(array, ", ", false);
    }
    public static String joinArray(int[] array, boolean hex) {
        return joinArray(array, ", ", hex);
    }
    public static String joinArray(int[] array, Object separator) {
        return joinArray(array, separator, false);
    }
    public static String joinArray(int[] array, Object separator, boolean hex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }
        StringBuilder builder = new StringBuilder();
        int length = array.length;
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                builder.append(separator);
            }
            int val = array[i];
            if (hex) {
                builder.append(HexUtil.toHex8(val));
            } else {
                builder.append(val);
            }
        }
        return builder.toString();
    }
}
