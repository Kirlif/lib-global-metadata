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

import com.reandroid.arsc.container.FixedBlockContainer;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.MetadataInteger;
import com.reandroid.unity.metadata.index.TypeDefinitionIndex;
import com.reandroid.unity.metadata.base.MDUByte;
import com.reandroid.unity.metadata.data.TypeDefinitionData;

import java.io.IOException;

public class Il2CppTypeEnumBlock extends FixedBlockContainer {

    private final EnumTypeIndex enumTypeIndex;

    public Il2CppTypeEnumBlock(Il2CppTypeEnum typeEnum) {
        super(2);
        MDUByte typeByte = new MDUByte();
        this.enumTypeIndex = new EnumTypeIndex(typeByte);

        addChild(0, typeByte);
        addChild(1, enumTypeIndex);

        if (typeEnum != null) {
            typeByte.set(typeEnum.value());
        }
    }
    public Il2CppTypeEnumBlock() {
        this(null);
    }


    public Il2CppTypeEnum type() {
        if (isEnum()) {
            return getEnumElementType();
        }
        return getTypeEnum();
    }

    public Il2CppTypeEnum getTypeEnum() {
        return enumTypeIndex.getTypeEnum();
    }
    public void setTypeEnum(Il2CppTypeEnum typeEnum) {
        enumTypeIndex.setTypeEnum(typeEnum);
    }
    public Il2CppTypeEnum getEnumElementType() {
        return enumTypeIndex.getEnumElementType();
    }
    public boolean isEnum() {
        return enumTypeIndex.isEnum();
    }
    public TypeDefinitionData getTypeDefinition() {
        return enumTypeIndex.getTypeDefinition();
    }
    public TypeDefinitionData getElementDefinition() {
        return enumTypeIndex.getElementDefinition();
    }

    @Override
    public String toString() {
        return enumTypeIndex.toString();
    }

    static class EnumTypeIndex extends MetadataInteger {

        private final IntegerReference type;
        private final TypeDefinitionIndex enumTypeIndex;

        public EnumTypeIndex(IntegerReference type) {
            super(true);
            this.type = type;

            TypeDefinitionIndex typeIndex = new TypeDefinitionIndex(this);
            this.enumTypeIndex = typeIndex;

            typeIndex.setParent(this);
            typeIndex.setIndex(0);
        }

        public Il2CppTypeEnum getTypeEnum() {
            return Il2CppTypeEnum.valueOf(type.get());
        }
        public void setTypeEnum(Il2CppTypeEnum typeEnum) {
            type.set(typeEnum.value());
        }
        public Il2CppTypeEnum getEnumElementType() {
            if (isEnum()) {
                return Il2CppTypeEnum.valueOfSystemType(getElementName());
            }
            return null;
        }
        public String getTypeName() {
            TypeDefinitionData data = getTypeDefinition();
            if (data != null) {
                return data.getTypeName();
            }
            return null;
        }
        public String getElementName() {
            TypeDefinitionData data = getElementDefinition();
            if (data != null) {
                return data.getTypeName();
            }
            return null;
        }

        public TypeDefinitionData getTypeDefinition() {
            return enumTypeIndex.getData();
        }
        public TypeDefinitionData getElementDefinition() {
            return enumTypeIndex.getElementType();
        }

        public boolean isEnum() {
            return type.get() == Il2CppTypeEnum.ENUM.value();
        }
        @Override
        public boolean hasValidVersion() {
            return isEnum();
        }
        @Override
        public boolean isNull() {
            return super.isNull() || !isEnum();
        }

        @Override
        public void onReadBytes(BlockReader reader) throws IOException {
            if (isNull()) {
                return;
            }
            super.onReadBytes(reader);
            enumTypeIndex.linkTypes();
        }
        public Object toJson() {
            JSONObject jsonObject = new JSONObject();
            if (isNull()) {
                Il2CppTypeEnum typeEnum = getTypeEnum();
                Object obj;
                if (typeEnum == null) {
                    obj = JSONObject.NULL;
                } else {
                    obj = typeEnum.name();
                }
                jsonObject.put("enum_type", obj);
            } else {
                jsonObject.put("enum_type", getTypeName());
                jsonObject.put("enum_element", getElementName());
            }
            return jsonObject;
        }

        @Override
        public String toString() {
            if (isNull()) {
                return String.valueOf(getTypeEnum());
            }
            return "Enum: " + getTypeName() + "<" + getElementName() + ">";
        }
    }
}
