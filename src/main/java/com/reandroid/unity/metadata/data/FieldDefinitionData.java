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

import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.VersionRange;
import com.reandroid.unity.metadata.base.MDInt;
import com.reandroid.unity.metadata.index.CodeStringIndex;
import com.reandroid.unity.metadata.index.TypeDefinitionIndex;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.FieldSpec;
import com.reandroid.unity.metadata.spec.Spec;
import com.reandroid.unity.metadata.spec.TypeSpec;

public class FieldDefinitionData extends SectionData implements TokenizedData {

    public final CodeStringIndex nameIndex;
    public final TypeDefinitionIndex typeIndex;
    public final MDInt customAttributeIndex;
    private final MetadataToken token;

    private FieldDefaultValueData defaultValueData;

    public FieldDefinitionData() {
        super(4);

        this.nameIndex = new CodeStringIndex();
        this.typeIndex = new TypeDefinitionIndex();
        this.customAttributeIndex = new MDInt(new VersionRange(null, 24.0));
        this.token = new MetadataToken(new VersionRange(19.0, null));

        addChild(0, nameIndex);
        addChild(1, typeIndex);
        addChild(2, customAttributeIndex);
        addChild(3, token);
    }

    public MetadataToken getToken() {
        return token;
    }

    public String getName() {
        return nameIndex.getString();
    }
    public String getTypeName() {
        return typeIndex.getName();
    }

    public TypeDefinitionData getType() {
        return typeIndex.getData();
    }

    @Override
    public void link() {
        super.link();
        if (defaultValueData != null) {
            defaultValueData.link();
        }
    }

    @Override
    public FieldSpec getSpec() {
        TypeSpec typeSpec = typeIndex.getSpec();
        Spec value = null;
        FieldDefaultValueData data = getDefaultValueData();
        if (data != null) {
            if (typeSpec == null) {
                typeSpec = data.typeIndex.getSpec();
            }
            value = data.dataIndex.getSpec();
        }
        return FieldSpec.of(nameIndex.getSpec(), typeSpec, value);
    }

    public FieldDefaultValueData getDefaultValueData() {
        return defaultValueData;
    }
    public void setDefaultValueData(FieldDefaultValueData defaultValueData) {
        this.defaultValueData = defaultValueData;
    }
    public boolean hasDefaultValue() {
        return getDefaultValueData() != null;
    }
    public Object defaultValue() {
        FieldDefaultValueData data = getDefaultValueData();
        if (data != null) {
            return data.value();
        }
        return null;
    }

    public String getModifiers() {
        TypeDefinitionData type = getType();
        if (type == null) {
            return "";
        }
        int access = type.flags().get() & FIELD_ATTRIBUTE_FIELD_ACCESS_MASK;
        switch (access)
        {
            case FIELD_ATTRIBUTE_PRIVATE:
                return "private ";
            case FIELD_ATTRIBUTE_PUBLIC:
                return "public ";
            case FIELD_ATTRIBUTE_FAMILY:
                return "protected ";
            case FIELD_ATTRIBUTE_ASSEMBLY:
            case FIELD_ATTRIBUTE_FAM_AND_ASSEM:
                return "internal ";
            case FIELD_ATTRIBUTE_FAM_OR_ASSEM:
                return "protected internal ";
        }
        return "";
    }
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", nameIndex.getJson());
        jsonObject.put("typeIndex", typeIndex.getJson());
        jsonObject.put("customAttributeIndex", customAttributeIndex.getJson());
        jsonObject.put("token", token.getJson());
        return jsonObject;
    }

    @Override
    public MetadataSectionType<FieldDefinitionData> getSectionType() {
        return MetadataSectionType.FIELDS;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("name=");
        builder.append(nameIndex);
        builder.append(", type=");
        builder.append(typeIndex);
        builder.append(", customAttribute=");
        builder.append(customAttributeIndex);
        builder.append(", token=");
        builder.append(token);
        if (hasDefaultValue()) {
            builder.append(", default=");
            builder.append(defaultValue());
        }
        return builder.toString();
    }

    public static final int FIELD_ATTRIBUTE_FIELD_ACCESS_MASK = 0x0007;
    public static final int FIELD_ATTRIBUTE_COMPILER_CONTROLLED = 0x0000;
    public static final int FIELD_ATTRIBUTE_PRIVATE = 0x0001;
    public static final int FIELD_ATTRIBUTE_FAM_AND_ASSEM = 0x0002;
    public static final int FIELD_ATTRIBUTE_ASSEMBLY = 0x0003;
    public static final int FIELD_ATTRIBUTE_FAMILY = 0x0004;
    public static final int FIELD_ATTRIBUTE_FAM_OR_ASSEM = 0x0005;
    public static final int FIELD_ATTRIBUTE_PUBLIC = 0x0006;

    public static final int FIELD_ATTRIBUTE_STATIC = 0x0010;
    public static final int FIELD_ATTRIBUTE_INIT_ONLY = 0x0020;
    public static final int FIELD_ATTRIBUTE_LITERAL = 0x0040;

}
