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
import com.reandroid.unity.metadata.index.TypeDefinitionIndex;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.FieldSpec;
import com.reandroid.unity.metadata.spec.Spec;

public class EventDefinitionData extends SectionData implements TokenizedData {

    public final CodeStringIndex nameIndex;
    public final TypeDefinitionIndex typeIndex;
    public final MDInt add;
    public final MDInt remove;
    public final MDInt raise;
    public final DefinitionIndex<CustomAttributeInfoData> customAttributeIndex;
    public final MetadataToken token;

    public EventDefinitionData() {
        super(7);

        this.nameIndex = new CodeStringIndex();
        this.typeIndex = new TypeDefinitionIndex();
        this.add = new MDInt();
        this.remove = new MDInt();
        this.raise = new MDInt();
        this.customAttributeIndex = new DefinitionIndex<>(MetadataSectionType.ATTRIBUTES_INFO,
                new VersionRange(null, 24.0));
        this.token = new MetadataToken(new VersionRange(19.0, null));

        addChild(0, nameIndex);
        addChild(1, typeIndex);
        addChild(2, add);
        addChild(3, remove);
        addChild(4, raise);
        addChild(5, customAttributeIndex);
        addChild(6, token);
    }

    public MetadataToken getToken() {
        return token;
    }

    @Override
    public Spec getSpec() {
        return FieldSpec.of(nameIndex.getSpec(), typeIndex.getSpec());
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", nameIndex.getJson());
        jsonObject.put("type", typeIndex.getJson());
        jsonObject.put("add", add.getJson());
        jsonObject.put("remove", remove.getJson());
        jsonObject.put("raise", raise.getJson());
        jsonObject.put("custom_attribute", customAttributeIndex.getJson());
        jsonObject.put("token", token.getJson());
        return jsonObject;
    }

    @Override
    public String toString() {
        return "nameIndex=" + nameIndex
                + ", typeIndex=" + typeIndex
                + ", add=" + add
                + ", remove=" + remove
                + ", raise=" + raise
                + ", customAttributeIndex=" + customAttributeIndex
                + ", token=" + token;
    }
}
