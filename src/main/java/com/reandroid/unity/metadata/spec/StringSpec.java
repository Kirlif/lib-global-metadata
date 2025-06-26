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
package com.reandroid.unity.metadata.spec;

import com.reandroid.json.JSONObject;
import com.reandroid.utils.ObjectsUtil;
import com.reandroid.utils.StringsUtil;

public class StringSpec implements ValueSpec {

    public static final StringSpec NULL = new StringSpec(null);
    public static final StringSpec EMPTY = new StringSpec(StringsUtil.EMPTY);

    private final String value;

    private StringSpec(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
    @Override
    public String descriptor() {
        return "\"" + value + "\"";
    }
    public boolean isEmpty() {
        String value = getValue();
        return value == null || value.length() == 0;
    }
    @Override
    public Object json() {
        String value = getValue();
        if (value == null) {
            return JSONObject.NULL;
        }
        return value;
    }
    @Override
    public int hashCode() {
        return ObjectsUtil.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StringSpec)) {
            return false;
        }
        StringSpec spec = (StringSpec) obj;
        return ObjectsUtil.equals(value, spec.value);
    }

    @Override
    public String toString() {
        String value = getValue();
        if (value == null) {
            return "null";
        }
        return "\"" + value + "\"";
    }

    public static StringSpec of(String value) {
        if (value == null) {
            return NULL;
        }
        if (value.length() == 0) {
            return EMPTY;
        }
        return new StringSpec(value);
    }
}
