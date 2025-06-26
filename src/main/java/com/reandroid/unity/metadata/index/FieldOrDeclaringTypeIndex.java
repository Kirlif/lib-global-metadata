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
package com.reandroid.unity.metadata.index;

import com.reandroid.arsc.container.FixedBlockContainer;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.section.MetadataSectionList;
import com.reandroid.unity.metadata.base.JsonData;
import com.reandroid.unity.metadata.base.LinkableItem;
import com.reandroid.unity.metadata.base.MDCompressedSInt32;
import com.reandroid.unity.metadata.base.MDCompressedUInt32;
import com.reandroid.unity.metadata.data.FieldDefinitionData;
import com.reandroid.unity.metadata.data.TypeDefinitionData;
import com.reandroid.unity.metadata.section.MetadataSection;
import com.reandroid.unity.metadata.section.MetadataSectionType;

public class FieldOrDeclaringTypeIndex extends FixedBlockContainer implements LinkableItem, JsonData {

    private final MDCompressedSInt32 fieldIndex;
    private final MDCompressedUInt32 declaringTypeIndex;

    private FieldDefinitionData mField;
    private TypeDefinitionData mDeclaringType;

    public FieldOrDeclaringTypeIndex() {
        super(2);
        this.fieldIndex = new MDCompressedSInt32();
        this.declaringTypeIndex = new FieldControlledUInt(fieldIndex);

        addChild(0, fieldIndex);
        addChild(1, declaringTypeIndex);
    }

    public String getDeclaringName() {
        TypeDefinitionData data = getDeclaringType();
        if (data != null) {
            return data.getName();
        }
        return null;
    }
    public FieldDefinitionData getField() {
        return mField;
    }
    public TypeDefinitionData getDeclaringType() {
        return mDeclaringType;
    }

    public boolean hasDeclaring() {
        return fieldIndex.get() < 0;
    }

    @Override
    public void link() {
        if (hasDeclaring()) {
            linkDeclaring();
        } else {
            linkField();
        }
    }
    private void linkField() {
        if (this.mField != null) {
            return;
        }
        MetadataSection<FieldDefinitionData> section = getFieldSection();
        if (section == null) {
            return;
        }
        int idx = fieldIndex.get();
        if (idx >= 0) {
            this.mField = section.getByIdx(idx);
            if (mField != null) {
                mField.link();
            }
        }
    }
    private void linkDeclaring() {
        if (this.mDeclaringType != null) {
            return;
        }
        MetadataSection<TypeDefinitionData> section = getTypeSection();
        if (section == null) {
            return;
        }
        int idx = declaringTypeIndex.get();
        if (idx >= 0) {
            this.mDeclaringType = section.getByIdx(idx);
            if (mDeclaringType != null) {
                mDeclaringType.link();
            }
        }
    }

    private MetadataSection<FieldDefinitionData> getFieldSection() {
        MetadataSectionList sectionList = getParentInstance(MetadataSectionList.class);
        if (sectionList != null) {
            return sectionList.getSection(MetadataSectionType.FIELDS);
        }
        return null;
    }
    private MetadataSection<TypeDefinitionData> getTypeSection() {
        MetadataSectionList sectionList = getParentInstance(MetadataSectionList.class);
        if (sectionList != null) {
            return sectionList.getSection(MetadataSectionType.TYPE_DEFINITIONS);
        }
        return null;
    }

    @Override
    public Object getJson() {
        JSONObject jsonObject = new JSONObject();
        FieldDefinitionData field = getField();
        if (field != null) {
            jsonObject.put("field", field.getName());
        } else {
            TypeDefinitionData declaringType = getDeclaringType();
            if (declaringType != null) {
                jsonObject.put("declaring", declaringType.getTypeName());
            }
        }
        return jsonObject;
    }

    @Override
    public void setJson(Object obj) {

    }
    @Override
    public String toString() {
        if (hasDeclaring()) {
            return String.valueOf(getDeclaringName());
        }
        return String.valueOf(getField());
    }

    static class FieldControlledUInt extends MDCompressedUInt32 {

        private final MDCompressedSInt32 fieldIndex;

        FieldControlledUInt(MDCompressedSInt32 fieldIndex) {
            super();
            this.fieldIndex = fieldIndex;
        }

        @Override
        public boolean isNull() {
            return super.isNull() || fieldIndex.get() >= 0;
        }
    }
}
