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
package com.reandroid.unity.metadata.data;

import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.arsc.item.StringReference;
import com.reandroid.json.JSONException;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.MDInt;
import com.reandroid.unity.metadata.base.MDUInt;

public class StringLiteral extends SectionData implements StringReference {

    private final MDInt length;
    private final MDInt offset;

    private StringLiteralData stringLiteralData;

    public StringLiteral() {
        super(2);
        this.length = new MDUInt();
        this.offset = new MDUInt();

        addChild(0, length);
        addChild(1, offset);
    }

    @Override
    public String get() {
        return data().get();
    }
    @Override
    public void set(String value) {
        data().set(value);
    }
    public IntegerReference offset() {
        return offset;
    }
    public IntegerReference length() {
        return length;
    }

    public StringLiteralData data() {
        return stringLiteralData;
    }

    public void linkDataInternal(StringLiteralData data) {
        if (data != null && this.stringLiteralData != null && data != this.stringLiteralData) {
            throw new IllegalArgumentException("Data already linked: " + getIndex());
        }
        this.stringLiteralData = data;
    }
    public void onRemovedInternal() {
        StringLiteralData data = data();
        if (data != null) {
            data.removeSelf(this);
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("index", getIdx());
        jsonObject.put("value", get());
        return jsonObject;
    }
    @Override
    public void fromJson(JSONObject json) {
        if (json.has("index") && getIdx() != json.getInt("index")) {
            throw new JSONException("Mismatching index: " + getIdx() + " at " + json);
        }
        set(json.getString("value"));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(':');
        builder.append(length().get());
        builder.append(':');
        builder.append(offset().get());
        builder.append(')');
        StringLiteralData data = data();
        if (data != null) {
            builder.append(" '");
            builder.append(get());
            builder.append("'");
        } else {
            builder.append(" <DATA NOT LINKED>");
        }
        return builder.toString();
    }

}
