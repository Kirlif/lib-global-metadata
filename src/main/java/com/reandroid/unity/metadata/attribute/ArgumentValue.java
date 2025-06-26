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
package com.reandroid.unity.metadata.attribute;

import com.reandroid.arsc.base.Creator;
import com.reandroid.arsc.container.SingleBlockContainer;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.JsonData;
import com.reandroid.unity.metadata.base.LinkableItem;
import com.reandroid.unity.metadata.spec.Spec;
import com.reandroid.unity.metadata.value.Il2CppTypeEnum;
import com.reandroid.unity.metadata.value.MetadataValue;
import com.reandroid.unity.metadata.value.MetadataValueFactory;

import java.io.IOException;

public class ArgumentValue extends SingleBlockContainer<MetadataValue>
        implements LinkableItem, JsonData {

    public ArgumentValue() {
        super();
    }

    public MetadataValue getValue() {
        return getItem();
    }
    public void setValue(MetadataValue value) {
        setItem(value);
    }
    public Il2CppTypeEnum getType() {
        MetadataValue value = getValue();
        if (value != null) {
            return value.getTypeEnum();
        }
        return null;
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        setItem(MetadataValueFactory.read(reader));
        super.onReadBytes(reader);
    }

    @Override
    public void link() {
        LinkableItem.linkObject(getValue());
    }

    public Spec getSpec() {
        MetadataValue value = getValue();
        if (value != null) {
            return value.getSpec();
        }
        return null;
    }
    @Override
    public Object getJson() {
        MetadataValue value = getValue();
        if (value != null) {
            return value.getJson();
        }
        return JSONObject.NULL;
    }

    @Override
    public void setJson(Object obj) {

    }
    @Override
    public String toString() {
        return String.valueOf(getValue());
    }

    public static final Creator<ArgumentValue> CREATOR = ArgumentValue::new;
}
