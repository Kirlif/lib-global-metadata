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
import com.reandroid.unity.metadata.index.DefinitionIndex;
import com.reandroid.unity.metadata.section.MetadataSection;
import com.reandroid.unity.metadata.section.MetadataSectionType;

public class CustomAttributeDataRange extends SectionData implements TokenizedData {

    private final MetadataToken token;
    private final DefinitionIndex<CustomAttributeData> dataOffset;

    public CustomAttributeDataRange() {
        super(2);
        this.token = new MetadataToken();
        this.dataOffset = new DefinitionIndex<CustomAttributeData>(MetadataSectionType.ATTRIBUTE_DATA) {

            @Override
            public int idxOf(CustomAttributeData data) {
                if (data == null) {
                    MetadataSection<CustomAttributeData> section = getSection();
                    return section.getSectionSize() - section.getEntriesAlignment().size();
                }
                return super.idxOf(data);
            }
        };

        addChild(0, token);
        addChild(1, dataOffset);
    }



    public MetadataToken getToken() {
        return token;
    }
    public DefinitionIndex<CustomAttributeData> getDataOffset() {
        return dataOffset;
    }

    public CustomAttributeData getAttributeData() {
        return getDataOffset().getData();
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("offset", dataOffset.get());
        jsonObject.put("token", token.getJson());
        jsonObject.put("data", dataOffset.getJson());
        return jsonObject;
    }

    @Override
    public Object getJson() {
        return toJson();
    }

    @Override
    public MetadataSectionType<CustomAttributeDataRange> getSectionType() {
        return MetadataSectionType.ATTRIBUTE_DATA_RANGE;
    }
    @Override
    public String toString() {
        return getToken() + ":" + "{" + getAttributeData() + "}";
    }
}
