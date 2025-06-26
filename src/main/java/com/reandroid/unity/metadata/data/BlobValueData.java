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

import com.reandroid.arsc.container.SingleBlockContainer;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.StringReference;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.index.TypeDefinitionIndex;
import com.reandroid.unity.metadata.section.BlobDataPool;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.section.SecFieldAndParameterDefaultValueData;
import com.reandroid.unity.metadata.spec.Spec;
import com.reandroid.unity.metadata.spec.SpecPair;
import com.reandroid.unity.metadata.spec.TypeSpec;
import com.reandroid.unity.metadata.value.Il2CppTypeEnum;
import com.reandroid.unity.metadata.value.MetadataValue;
import com.reandroid.unity.metadata.value.MetadataValueFactory;
import com.reandroid.unity.metadata.value.ValueUnknown;
import com.reandroid.utils.CompareUtil;

import java.io.IOException;

public class BlobValueData extends SectionData implements
        OffsetIdxData, Comparable<BlobValueData> {

    private final SingleBlockContainer<MetadataValue> valueContainer;
    private final TypeDefinitionIndex typeIndex;

    private int mOffset;

    public BlobValueData(TypeDefinitionIndex typeIndex, int offset) {
        super(1);
        this.valueContainer = new SingleBlockContainer<>();

        this.typeIndex = typeIndex;
        this.mOffset = offset;

        addChild(0, valueContainer);
    }

    public StringReference getValueAsString() {
        MetadataValue value = getValue();
        if (value instanceof StringReference) {
            return (StringReference) value;
        }
        return null;
    }
    public MetadataValue getValue() {
        return valueContainer.getItem();
    }
    public void setValue(MetadataValue value) {
        if (value != null) {
            value.clearEnumBlock();
        }
        valueContainer.setItem(value);
    }

    public void linkValue() {
        if (getValue() == null) {
            setValue(readValue());
        }
    }
    public void initUnknownValue(int nextOffset) {
        MetadataValue value = getValue();
        if (!isBlankUnknownValue(value)) {
            return;
        }
        if (value == null) {
            value = MetadataValueFactory.createUnknown();
            setValue(value);
        }
        readUnknown((ValueUnknown) value, nextOffset);
    }
    private void readUnknown(ValueUnknown value, int nextOffset) {
        int offset = getOffset();
        value.setSize(nextOffset - offset);
        BlockReader reader = getDataPool().getBlockReader();
        reader.seek(offset);
        try {
            value.readBytes(reader);
        } catch (IOException exception) {
            // unreachable
            throw new RuntimeException(exception);
        }
    }
    private boolean isBlankUnknownValue(MetadataValue value) {
        if (value == null) {
            return true;
        }
        if (!(value instanceof ValueUnknown)) {
            return false;
        }
        return ((ValueUnknown) value).size() == 0;
    }
    private MetadataValue readValue() {
        MetadataValue value = MetadataValueFactory.forType(getType());
        if (value == null) {
            return null;
        }
        value.setParent(this);
        value.clearEnumBlock();
        BlockReader reader = getDataPool().getBlockReader();
        reader.seek(getOffset());
        try {
            value.readBytes(reader);
            return value;
        } catch (IOException exception) {
            return MetadataValueFactory.createUnknown();
        }
    }
    @Override
    public int getIdx() {
        return getOffset();
    }
    @Override
    public int getOffset() {
        return mOffset;
    }
    @Override
    public void setOffset(int idx) {
        this.mOffset = idx;
    }
    @Override
    public int getDataSize() {
        MetadataValue value = getValue();
        if (value != null) {
            return value.countBytes();
        }
        return 0;
    }

    public BlobDataPool getDataPool() {
        return getParentSection().getDataPool();
    }

    @Override
    public SecFieldAndParameterDefaultValueData getParentSection() {
        return (SecFieldAndParameterDefaultValueData) super.getParentSection();
    }

    public Il2CppTypeEnum getType() {
        TypeDefinitionIndex typeIndex = this.typeIndex;
        return Il2CppTypeEnum.valueOfSystemType(typeIndex.getNamespace(), typeIndex.getName());
    }
    public String getTypeName() {
        return typeIndex.getTypeName();
    }

    @Override
    public SpecPair<TypeSpec, Spec> getSpec() {
        MetadataValue value = getValue();
        if (value == null) {
            return null;
        }
        return SpecPair.of(typeIndex.getSpec(), value.getSpec());
    }

    @Override
    public void link() {
        typeIndex.link();
        linkValue();
    }

    @Override
    public int computeUnitSize() {
        return 0;
    }

    @Override
    public int compareTo(BlobValueData byteValueData) {
        if (byteValueData == null) {
            return -1;
        }
        if (byteValueData == this) {
            return 0;
        }
        return CompareUtil.compare(getOffset(), byteValueData.getOffset());
    }

    @Override
    public JSONObject toJson() {
        MetadataValue value = getValue();
        if (value != null) {
            JSONObject json = value.getJson();
            if (value.getTypeEnum() == Il2CppTypeEnum.UNKNOWN &&
                    typeIndex != TypeDefinitionIndex.NO_TYPE) {
                json.put("value_type", typeIndex.getJson());
            }
            return json;
        }
        return super.toJson();
    }

    @Override
    public Object getJson() {
        MetadataValue value = getValue();
        if (value != null) {
            JSONObject json = value.getJson();
            if (value.getTypeEnum() == Il2CppTypeEnum.UNKNOWN && typeIndex != TypeDefinitionIndex.NO_TYPE) {
                json.put("value_type", typeIndex.getJson());
            }
            return json;
        }
        return toString();
    }

    @Override
    public MetadataSectionType<BlobValueData> getSectionType() {
        return MetadataSectionType.FIELD_AND_PARAMETER_DEFAULT_VALUE_DATA;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BlobValueData valueData = (BlobValueData) obj;
        return mOffset == valueData.mOffset &&
                getParentSection() == valueData.getParentSection();
    }
    @Override
    public int hashCode() {
        return mOffset;
    }

    @Override
    public String toString() {
        MetadataValue value = getValue();
        if (value != null) {
            return getOffset() + ": " + value.toString();
        }
        return getOffset() + ":" + getTypeName();
    }

}
