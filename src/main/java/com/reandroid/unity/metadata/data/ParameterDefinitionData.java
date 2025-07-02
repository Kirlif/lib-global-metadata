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
import com.reandroid.unity.metadata.index.CodeStringIndex;
import com.reandroid.unity.metadata.index.DefinitionIndex;
import com.reandroid.unity.metadata.index.TypeDefinitionIndex;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.ParameterSpec;
import com.reandroid.unity.metadata.spec.Spec;

public class ParameterDefinitionData extends SectionData implements TokenizedData {

    public final CodeStringIndex nameIndex;
    public final MetadataToken token;
    public final DefinitionIndex<CustomAttributeInfoData> customAttributeIndex;
    public final TypeDefinitionIndex typeIndex;

    private ParameterDefaultValueData defaultValueData;

    public ParameterDefinitionData() {
        super(4);

        this.nameIndex = new CodeStringIndex();
        this.token = new MetadataToken();
        this.customAttributeIndex = new DefinitionIndex<>(MetadataSectionType.ATTRIBUTES_INFO,
                new VersionRange(null, 24.0));
        this.typeIndex = new TypeDefinitionIndex();

        addChild(0, nameIndex);
        addChild(1, token);
        addChild(2, customAttributeIndex);
        addChild(3, typeIndex);
    }

    public MetadataToken getToken() {
        return token;
    }
    @Override
    public ParameterSpec getSpec() {
        Spec value = null;
        ParameterDefaultValueData data = getDefaultValueData();
        if (data != null) {
            value = data.dataIndex.getSpec();
        }
        return ParameterSpec.of(nameIndex.getSpec(), typeIndex.getSpec(), value);
    }

    public String getName() {
        return nameIndex.getString();
    }

    public String getTypeName() {
        return typeIndex.getTypeName();
    }

    public TypeDefinitionData getType() {
        return typeIndex.getData();
    }

    public ParameterDefaultValueData getDefaultValueData() {
        return defaultValueData;
    }

    public void setDefaultValueData(ParameterDefaultValueData defaultValueData) {
        this.defaultValueData = defaultValueData;
    }

    @Override
    public void link() {
        super.link();
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", nameIndex.getJson());
        jsonObject.put("token", token.getJson());
        jsonObject.put("custom_attribute", customAttributeIndex.getJson());
        jsonObject.put("type", typeIndex.getJson());
        return jsonObject;
    }

    @Override
    public MetadataSectionType<ParameterDefinitionData> getSectionType() {
        return MetadataSectionType.PARAMETERS;
    }
    @Override
    public String toString() {
        return "nameIndex=" + nameIndex
                + ", token=" + token
                + ", customAttributeIndex=" + customAttributeIndex
                + ", typeIndex=" + typeIndex;
    }
}
