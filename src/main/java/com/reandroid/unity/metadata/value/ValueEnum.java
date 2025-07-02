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
import com.reandroid.unity.metadata.data.FieldDefinitionData;
import com.reandroid.unity.metadata.data.TypeDefinitionData;
import com.reandroid.utils.ObjectsStore;
import com.reandroid.utils.StringsUtil;
import com.reandroid.utils.collection.ArrayIterator;
import com.reandroid.utils.collection.ComputeIterator;

import java.io.IOException;
import java.util.Iterator;

public class ValueEnum extends MetadataValue implements LinkableItem, ValueParent {

    private final SingleBlockContainer<MetadataValue> valueContainer;

    public ValueEnum() {
        super(1, Il2CppTypeEnum.ENUM);
        this.valueContainer = new SingleBlockContainer<>();

        addChild(START_INDEX + 0, valueContainer);
    }

    public MetadataValue getEnumValue() {
        return valueContainer.getItem();
    }
    public MetadataValue setEnumValue(MetadataValue value) {
        valueContainer.setItem(value);
        if (value != null) {
            value.clearEnumBlock();
        }
        return value;
    }
    public Object enumValue() {
        MetadataValue value = getEnumValue();
        if (value != null) {
            return value.value();
        }
        return null;
    }

    public String getEnumTypeName() {
        TypeDefinitionData data = getEnumTypeDefinition();
        if (data != null) {
            return data.getTypeName();
        }
        return null;
    }
    public TypeDefinitionData getEnumTypeDefinition() {
        return getTypeEnumBlock().getTypeDefinition();
    }
    @Override
    public TypeDefinitionData getValueTypeDefinition() {
        return getTypeEnumBlock().getElementDefinition();
    }
    public FieldDefinitionData[] getEnumFields() {
        TypeDefinitionData typeData = getEnumTypeDefinition();
        if (typeData == null) {
            return null;
        }
        Object value = enumValue();
        if (!(value instanceof Integer)) {
            return null;
        }

        int intValue = (Integer) value;
        Iterator<FieldDefinitionData> iterator = typeData.getFieldList().iterator();
        Object list = null;
        while (iterator.hasNext()) {
            FieldDefinitionData data = iterator.next();
            Object obj = data.defaultValue();
            if (obj instanceof Integer) {
                int i = (Integer) obj;
                if (i == intValue) {
                    return new FieldDefinitionData[]{data};
                }
                if ((intValue & i) == i) {
                    list = ObjectsStore.add(list, data);
                }
            }
        }
        FieldDefinitionData[] results = new FieldDefinitionData[ObjectsStore.size(list)];
        ObjectsStore.collect(list, results);
        return results;
    }
    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        setEnumValue(null);
        super.onReadBytes(reader);
        setEnumValue(MetadataValueFactory.forType(getTypeEnumBlock().getEnumElementType()));
        getEnumValue().onReadBytes(reader);
    }

    @Override
    public void link() {
        LinkableItem.linkObject(getEnumValue());
    }

    @Override
    public Object getJsonValue() {
        MetadataValue value = getEnumValue();
        if (value != null) {
            return value.getJsonValue();
        }
        return super.getJsonValue();
    }

    @Override
    public String toString() {
        Iterator<String> iterator = ComputeIterator.of(ArrayIterator.of(getEnumFields()),
                FieldDefinitionData::getName);
        if (iterator.hasNext()) {
            return getEnumTypeName() + "[" + StringsUtil.join(iterator, ", ") + "]";
        }
        MetadataValue value = getEnumValue();
        if (value != null) {
            return "ENUM:" + getEnumTypeName() + "(" + value + ")";
        }
        return super.toString() + ": " + valueContainer.toString();
    }
}
