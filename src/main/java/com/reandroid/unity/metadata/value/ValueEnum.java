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
package com.reandroid.unity.metadata.value;

import com.reandroid.arsc.container.SingleBlockContainer;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.unity.metadata.base.LinkableItem;
import com.reandroid.unity.metadata.data.TypeDefinitionData;

import java.io.IOException;

public class ValueEnum extends MetadataValue implements LinkableItem, ValueParent {

    private final SingleBlockContainer<MetadataValue> valueContainer;

    public ValueEnum() {
        super(2, Il2CppTypeEnum.ENUM);
        this.valueContainer = new SingleBlockContainer<>();

        addChild(START_INDEX + 0, valueContainer);
    }

    public MetadataValue getValue() {
        return valueContainer.getItem();
    }
    public MetadataValue setValue(MetadataValue value) {
        valueContainer.setItem(value);
        if (value != null) {
            value.clearEnumBlock();
        }
        return value;
    }

    public String getEnumTypeName() {
        TypeDefinitionData data = getValueTypeDefinition();
        if (data != null) {
            return data.getName();
        }
        return null;
    }
    @Override
    public TypeDefinitionData getValueTypeDefinition() {
        return getTypeEnumBlock().getEnumTypeIndex().getEnumTypeData();
    }
    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        setValue(null);
        super.onReadBytes(reader);
        setValue(MetadataValueFactory.forType(
                getTypeEnumBlock().getEnumElementType()))
                .onReadBytes(reader);
    }

    @Override
    public void link() {
        LinkableItem.linkObject(getValue());
    }

    @Override
    public Object getJsonValue() {
        MetadataValue value = getValue();
        if (value != null) {
            return value.getJsonValue();
        }
        return super.getJsonValue();
    }

    @Override
    public String toString() {
        MetadataValue value = getValue();
        if (value != null) {
            return "ENUM:" + getEnumTypeName() + "(" + value + ")";
        }
        return super.toString() + ": " + valueContainer.toString();
    }
}
