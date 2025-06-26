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
import com.reandroid.unity.metadata.base.MDUInt;
import com.reandroid.unity.metadata.index.CodeStringIndex;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.Spec;

public class PropertyDefinitionData extends SectionData implements TokenizedData {

    public final CodeStringIndex nameIndex;
    public final MDInt get;
    public final MDInt set;
    public final MDUInt attrs;
    public final MDInt customAttributeIndex;
    public final MetadataToken token;

    public PropertyDefinitionData() {
        super(6);

        this.nameIndex = new CodeStringIndex();
        this.get = new MDInt();
        this.set = new MDInt();
        this.attrs = new MDUInt();
        this.customAttributeIndex = new MDInt(new VersionRange(null, 24.0));
        this.token = new MetadataToken(new VersionRange(19.0, null));

        addChild(0, nameIndex);
        addChild(1, get);
        addChild(2, set);
        addChild(3, attrs);
        addChild(4, customAttributeIndex);
        addChild(5, token);
    }

    public MetadataToken getToken() {
        return token;
    }

    @Override
    public Spec getSpec() {
        return nameIndex.getSpec();
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("index", getIdx());
        jsonObject.put("name", nameIndex.getJson());
        jsonObject.put("get", get.getJson());
        jsonObject.put("set", set.getJson());
        jsonObject.put("attrs", attrs.getJson());
        jsonObject.put("custom_attribute", customAttributeIndex.getJson());
        jsonObject.put("token", token.getJson());
        return jsonObject;
    }

    @Override
    public MetadataSectionType<PropertyDefinitionData> getSectionType() {
        return MetadataSectionType.PROPERTIES;
    }
    @Override
    public String toString() {
        return "nameIndex=" + nameIndex
                + ", get=" + get
                + ", set=" + set
                + ", attrs=" + attrs
                + ", customAttributeIndex=" + customAttributeIndex
                + ", token=" + token;
    }
}
