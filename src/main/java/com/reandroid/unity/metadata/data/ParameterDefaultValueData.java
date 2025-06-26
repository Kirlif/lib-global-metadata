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
import com.reandroid.unity.metadata.index.DefaultValueDataIndex;
import com.reandroid.unity.metadata.index.DefinitionIndex;
import com.reandroid.unity.metadata.index.TypeDefinitionIndex;
import com.reandroid.unity.metadata.section.MetadataSectionType;

public class ParameterDefaultValueData extends SectionData {

    public final DefinitionIndex<ParameterDefinitionData> parameterIndex;
    public final TypeDefinitionIndex typeIndex;
    public final DefaultValueDataIndex dataIndex;

    public ParameterDefaultValueData() {
        super(3);

        this.parameterIndex = new DefinitionIndex<>(MetadataSectionType.PARAMETERS);
        this.typeIndex = new TypeDefinitionIndex();
        this.dataIndex = new DefaultValueDataIndex(typeIndex);

        addChild(0, parameterIndex);
        addChild(1, typeIndex);
        addChild(2, dataIndex);
    }

    @Override
    public void link() {
        super.link();
        ParameterDefinitionData data = parameterIndex.getData();
        if (data != null && data.getDefaultValueData() == null) {
            data.setDefaultValueData(this);
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("parameter", parameterIndex.getJson());
        jsonObject.put("type", typeIndex.getJson());
        jsonObject.put("data", dataIndex.getJson());
        return jsonObject;
    }

    @Override
    public MetadataSectionType<ParameterDefaultValueData> getSectionType() {
        return MetadataSectionType.PARAMETER_DEFAULT_VALUES;
    }
    @Override
    public String toString() {
        return "parameterIndex=" + parameterIndex
                + ", typeIndex=" + typeIndex
                + ", dataIndex=" + dataIndex;
    }
}
