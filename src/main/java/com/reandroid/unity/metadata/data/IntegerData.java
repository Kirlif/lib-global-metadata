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
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.MDInt;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.PrimitiveSpec;
import com.reandroid.unity.metadata.spec.Spec;
import com.reandroid.utils.ObjectsUtil;

public class IntegerData extends SectionData implements IntegerReference {

    private final MDInt value;

    public IntegerData() {
        super(1);
        this.value = new MDInt();
        addChild(0, value);
    }

    @Override
    public int get() {
        return value.get();
    }
    @Override
    public void set(int value) {
        this.value.set(value);
    }
    @Override
    public Spec getSpec() {
        return new PrimitiveSpec.I4Spec(get());
    }

    @Override
    public Object getJson() {
        return get();
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value", get());
        return jsonObject;
    }

    @Override
    public MetadataSectionType<IntegerData> getSectionType() {
        return ObjectsUtil.cast(super.getSectionType());
    }
    @Override
    public String toString() {
        return Integer.toString(get());
    }
}
