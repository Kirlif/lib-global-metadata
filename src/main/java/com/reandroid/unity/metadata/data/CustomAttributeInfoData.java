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
import com.reandroid.unity.metadata.index.DefinitionIndexList;
import com.reandroid.unity.metadata.section.MetadataSectionType;

public class CustomAttributeInfoData extends SectionData implements TokenizedData {

    public final MetadataToken token;
    public final DefinitionIndexList<TypeEntryData> types;

    public CustomAttributeInfoData() {
        super(3);

        this.token = new MetadataToken(new VersionRange(24.1, null));
        this.types = new DefinitionIndexList<>(MetadataSectionType.ATTRIBUTE_TYPES);

        addChild(0, token);
        addChild(1, types);
    }

    public MetadataToken getToken() {
        return token;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token.getJson());
        jsonObject.put("types", types.getJson());
        return jsonObject;
    }

    @Override
    public MetadataSectionType<CustomAttributeInfoData> getSectionType() {
        return MetadataSectionType.ATTRIBUTES_INFO;
    }

    @Override
    public String toString() {
        return "token=" + token
                + ", types=" + types;
    }
}
