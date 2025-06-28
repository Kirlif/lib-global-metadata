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

import com.reandroid.json.JSONArray;
import com.reandroid.json.JSONConvert;
import com.reandroid.json.JSONObject;

import java.util.Iterator;

public class JsonDataUtil {

    public static JSONArray collectOptional(Iterator<?> iterator) {
        return collectOptional(null, iterator);
    }
    public static JSONArray collectOptional(JSONArray jsonArray, Iterator<?> iterator) {
        if (!iterator.hasNext()) {
            return jsonArray;
        }
        if (jsonArray == null) {
            jsonArray = new JSONArray();
        }
        while (iterator.hasNext()) {
            jsonArray.put(iterator.next());
        }
        return jsonArray;
    }
    public static<T extends JSONConvert<?>> JSONArray collectOptionalToJson(Iterator<T> iterator) {
        if (!iterator.hasNext()) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        while (iterator.hasNext()) {
            jsonArray.put(iterator.next().toJson());
        }
        return jsonArray;
    }
    public static JSONObject nonNullObject(Object obj) {
        if (obj == null) {
            return new JSONObject();
        }
        return (JSONObject) obj;
    }
    public static void putOptional(JSONObject json, String key, double value) {
        if (value != 0.0) {
            if (isNanOrInfinity(value)) {
                json.put(key, Double.doubleToLongBits(value));
            } else {
                json.put(key, value);
            }
        }
    }
    public static void putOptional(JSONObject json, String key, float value) {
        if (value != 0.0f) {
            if (isNanOrInfinity(value)) {
                json.put(key, Float.floatToIntBits(value));
            } else {
                json.put(key, value);
            }
        }
    }
    public static void putOptional(JSONObject json, String key, long value) {
        if (value != 0) {
            json.put(key, value);
        }
    }
    public static void putOptional(JSONObject json, String key, int value) {
        if (value != 0) {
            json.put(key, value);
        }
    }
    public static void putOptional(JSONObject json, String key, boolean value) {
        if (value) {
            json.put(key, true);
        }
    }
    public static void putOptional(JSONObject json, String key, Object value) {
        if (value instanceof Float) {
            putOptional(json, key, ((Float) value).floatValue());
        } else if (value instanceof Double) {
            putOptional(json, key, ((Double) value).doubleValue());
        } else if (!isOptional(value)) {
            json.put(key, value);
        }
    }
    private static boolean isOptional(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue() == 0;
        }
        if (value instanceof Boolean) {
            return !((Boolean) value);
        }
        if (value instanceof JSONArray) {
            return ((JSONArray) value).isEmpty();
        }
        if (value instanceof JSONObject) {
            return ((JSONObject) value).isEmpty();
        }
        return false;
    }
    private static boolean isNanOrInfinity(float value) {
        int bits = Float.floatToIntBits(value);
        if (bits == Float.floatToIntBits(Float.NaN)) {
            return true;
        }
        if (bits == Float.floatToIntBits(Float.NEGATIVE_INFINITY)) {
            return true;
        }
        return bits == Float.floatToIntBits(Float.POSITIVE_INFINITY);
    }
    private static boolean isNanOrInfinity(double value) {
        long bits = Double.doubleToLongBits(value);
        if (bits == Double.doubleToLongBits(Double.NaN)) {
            return true;
        }
        if (bits == Double.doubleToLongBits(Double.NEGATIVE_INFINITY)) {
            return true;
        }
        return bits == Double.doubleToLongBits(Double.POSITIVE_INFINITY);
    }
}
