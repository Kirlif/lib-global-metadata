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
import com.reandroid.unity.metadata.index.FieldDefinitionIndex;
import com.reandroid.unity.metadata.index.TypeDefinitionIndex;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.FieldSpec;

public class FieldRefData extends SectionData {

    public final TypeDefinitionIndex typeIndex;
    public final FieldDefinitionIndex fieldIndex;

    public FieldRefData() {
        super(2);

        this.typeIndex = new TypeDefinitionIndex();
        this.fieldIndex = new FieldDefinitionIndex();

        addChild(0, typeIndex);
        addChild(1, fieldIndex);
    }

    @Override
    public FieldSpec getSpec() {
        FieldSpec spec = fieldIndex.getSpec();
        if (spec != null) {
            return spec.changeDeclaring(typeIndex.getSpec());
        }
        return null;
    }

    @Override
    public void link() {
        super.link();
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", typeIndex.getJson());
        jsonObject.put("field", fieldIndex.getJson());
        return jsonObject;
    }

    @Override
    public MetadataSectionType<FieldRefData> getSectionType() {
        return MetadataSectionType.FIELD_REFS;
    }
    @Override
    public String toString() {
        return "typeIndex=" + typeIndex
                + ", fieldIndex=" + fieldIndex;
    }
}
