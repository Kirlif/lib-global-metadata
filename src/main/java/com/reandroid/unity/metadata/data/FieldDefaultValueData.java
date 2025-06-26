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

import com.reandroid.arsc.io.BlockReader;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.index.DefaultValueDataIndex;
import com.reandroid.unity.metadata.index.FieldDefinitionIndex;
import com.reandroid.unity.metadata.index.TypeDefinitionIndex;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.FieldSpec;
import com.reandroid.unity.metadata.spec.StringSpec;

import java.io.IOException;

public class FieldDefaultValueData extends SectionData {

    public final FieldDefinitionIndex fieldIndex;
    public final TypeDefinitionIndex typeIndex;
    public final DefaultValueDataIndex dataIndex;

    public FieldDefaultValueData() {
        super(3);

        this.fieldIndex = new FieldDefinitionIndex();
        this.typeIndex = new TypeDefinitionIndex();
        this.dataIndex = new DefaultValueDataIndex(typeIndex);

        addChild(0, fieldIndex);
        addChild(1, typeIndex);
        addChild(2, dataIndex);
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        super.onReadBytes(reader);
    }

    @Override
    public void link() {
        super.link();
        FieldDefinitionData data = fieldIndex.getData();
        if (data != null && data.getDefaultValueData() == null) {
            data.setDefaultValueData(this);
        }
    }

    @Override
    public FieldSpec getSpec() {
        return FieldSpec.of(StringSpec.of(fieldIndex.getName()),
                typeIndex.getSpec(), dataIndex.getSpec());
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("field", fieldIndex.getName());
        jsonObject.put("type", typeIndex.getSpec());
        jsonObject.put("data", dataIndex.getSpec());
        return jsonObject;
    }

    @Override
    public MetadataSectionType<FieldDefaultValueData> getSectionType() {
        return MetadataSectionType.FIELD_DEFAULT_VALUES;
    }
    @Override
    public String toString() {
        return "fieldIndex=" + fieldIndex
                + ", typeIndex=" + typeIndex
                + ", dataIndex=" + dataIndex;
    }
}
