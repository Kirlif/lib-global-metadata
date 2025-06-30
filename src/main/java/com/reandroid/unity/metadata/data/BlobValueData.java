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

import com.reandroid.arsc.base.Block;
import com.reandroid.arsc.base.Creator;
import com.reandroid.arsc.container.SingleBlockContainer;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.index.TypeDefinitionIndex;
import com.reandroid.unity.metadata.section.BlobDataPool;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.section.SecFieldAndParameterDefaultValueData;
import com.reandroid.unity.metadata.spec.Spec;
import com.reandroid.unity.metadata.spec.SpecPair;
import com.reandroid.unity.metadata.spec.TypeSpec;
import com.reandroid.unity.metadata.util.CommonUtil;
import com.reandroid.unity.metadata.value.*;
import com.reandroid.utils.CompareUtil;
import com.reandroid.utils.HexUtil;
import com.reandroid.utils.ObjectsUtil;

import java.io.IOException;

public class BlobValueData extends SectionData implements
        OffsetIdxData, ValueParent {

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

    @Override
    public Key getKey() {
        return checkKey(new Key(getBytes()));
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
            typeIndex().linkTypes();
            setValue(readValue());
        }
    }
    private MetadataValue readValue() {
        MetadataValue value = MetadataValueFactory.forType(getType());
        if (value == null) {
            return readUnknown();
        }
        value.setParent(this);
        value.clearEnumBlock();
        BlockReader reader = getDataPool().getBlockReader();
        reader.seek(getOffset());
        try {
            value.readBytes(reader);
            return value;
        } catch (IOException exception) {
            return readUnknown();
        }
    }
    private MetadataValue readUnknown() {
        BlobDataPool dataPool = getDataPool();
        int nextOffset;
        BlobValueData nextData = dataPool.get(getIndex() + 1);
        if (nextData != null) {
            nextOffset = nextData.getOffset();
        } else {
            nextOffset = dataPool.poolSize();
        }
        ValueUnknown unknown = MetadataValueFactory.createUnknown();
        unknown.clearEnumBlock();
        unknown.setParent(this);
        unknown.setSize(nextOffset - this.getOffset());
        try {
            BlockReader reader = getDataPool().getBlockReader();
            reader.seek(getOffset());
            unknown.readBytes(reader);
        } catch (IOException exception) {
            // unreachable
            throw new RuntimeException(exception);
        }
        return unknown;
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
        return getParentSection().getEntryList();
    }

    @Override
    public SecFieldAndParameterDefaultValueData getParentSection() {
        return (SecFieldAndParameterDefaultValueData) super.getParentSection();
    }

    public TypeDefinitionIndex typeIndex() {
        return typeIndex;
    }

    @Override
    public TypeDefinitionData getValueTypeDefinition() {
        return typeIndex().getData();
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
        typeIndex().linkTypes();
    }

    @Override
    public int computeUnitSize() {
        return 0;
    }

    public int compareBytes(BlobValueData byteValueData) {
        if (byteValueData == null) {
            return -1;
        }
        if (byteValueData == this) {
            return 0;
        }
        return CommonUtil.compareBytes(getKey().getBytes(),
                byteValueData.getKey().getBytes());
    }
    public int compareOffset(BlobValueData byteValueData) {
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
            json.put("offset", getOffset());
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
            json.put("offset", getOffset());
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
        return ObjectsUtil.equals(getKey(), valueData.getKey());
    }
    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    @Override
    public String toString() {
        MetadataValue value = getValue();
        if (value != null) {
            return getOffset() + ": " + value.toString();
        }
        return getOffset() + ":" + getTypeName();
    }

    public static class Key implements Comparable<Key> {

        private final byte[] bytes;

        public Key(byte[] bytes) {
            this.bytes = bytes;
        }

        public final byte[] getBytes() {
            return bytes;
        }

        @Override
        public int compareTo(Key key) {
            if (key == this) {
                return 0;
            }
            return CommonUtil.compareBytes(getBytes(), key.getBytes());
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Key key = (Key) obj;
            return Block.areEqual(bytes, key.bytes);
        }

        @Override
        public int hashCode() {
            return CommonUtil.hash(bytes);
        }

        @Override
        public String toString() {
            return HexUtil.toHexString(getBytes());
        }

    }

    public static final Creator<BlobValueData> CREATOR = () -> {
        throw new RuntimeException("Creator not supported");
    };
}
