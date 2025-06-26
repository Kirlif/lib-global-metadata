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
import com.reandroid.unity.metadata.index.DefinitionIndex;
import com.reandroid.unity.metadata.index.DefinitionIndexList;
import com.reandroid.unity.metadata.index.TypeDefinitionIndexList;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.StringSpec;

public class ImageDefinitionData extends SectionData implements TokenizedData {

    public final CodeStringIndex nameIndex;
    public final DefinitionIndex<AssemblyDefinitionData> assemblyIndex;
    public final TypeDefinitionIndexList typeList;
    public final TypeDefinitionIndexList exportTypeList;
    public final MDInt entryPointIndex;
    public final MetadataToken token;
    public final DefinitionIndexList<CustomAttributeData> customAttributeList;

    public ImageDefinitionData() {
        super(7);

        this.nameIndex = new CodeStringIndex();
        this.assemblyIndex = new DefinitionIndex<>(MetadataSectionType.ASSEMBLIES);
        this.typeList = new TypeDefinitionIndexList();
        this.exportTypeList = new TypeDefinitionIndexList(new VersionRange(24.0, null));
        this.entryPointIndex = new MDInt();
        this.token = new MetadataToken(new VersionRange(19.0, null));
        this.customAttributeList = new DefinitionIndexList<>(MetadataSectionType.ATTRIBUTE_DATA,
                new VersionRange(24.1, null));

        addChild(0, nameIndex);
        addChild(1, assemblyIndex);
        addChild(2, typeList);
        addChild(3, exportTypeList);
        addChild(4, entryPointIndex);
        addChild(5, token);
        addChild(6, customAttributeList);
    }

    public MetadataToken getToken() {
        return token;
    }
    public String getName() {
        return nameIndex.getString();
    }
    @Override
    public StringSpec getSpec() {
        return nameIndex.getSpec();
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", nameIndex.getJson());
        jsonObject.put("assembly_index", assemblyIndex.get());
        jsonObject.put("types", typeList.getJson());
        jsonObject.put("export_types", exportTypeList.getJson());
        jsonObject.put("entry_point", entryPointIndex.getJson());
        jsonObject.put("token", token.getJson());
        jsonObject.put("attributes", customAttributeList.getJson());
        return jsonObject;
    }

    @Override
    public String toString() {
        return "nameIndex=" + nameIndex
                + ", assemblyIndex=" + assemblyIndex
                + ", typeList=" + typeList
                + ", exportList=" + exportTypeList
                + ", entryPointIndex=" + entryPointIndex
                + ", token=" + token
                + ", customAttributeList=" + customAttributeList;
    }
}
