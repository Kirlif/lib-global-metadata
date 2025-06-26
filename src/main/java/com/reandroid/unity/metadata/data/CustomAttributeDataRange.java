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
import com.reandroid.unity.metadata.section.SectionCustomAttributeData;

public class CustomAttributeDataRange extends SectionData implements TokenizedData {

    private final MetadataToken token;
    private final DefinitionIndex<CustomAttributeData> offset;

    public CustomAttributeDataRange() {
        super(2);
        this.token = new MetadataToken();
        this.offset = new DefinitionIndex<CustomAttributeData>(MetadataSectionType.ATTRIBUTE_DATA) {
            @Override
            public CustomAttributeData pullData(MetadataSection<CustomAttributeData> section, int idx) {
                return ((SectionCustomAttributeData) section).getByOffset(idx);
            }
            @Override
            public int idxOf(CustomAttributeData data) {
                return data.getOffset();
            }
        };

        addChild(0, token);
        addChild(1, offset);
    }



    public MetadataToken getToken() {
        return token;
    }
    public DefinitionIndex<CustomAttributeData> getOffset() {
        return offset;
    }

    public CustomAttributeData getAttributeData() {
        return getOffset().getData();
    }
    public void setAttributeData(CustomAttributeData data) {
        getOffset().setData(data);
    }

    @Override
    protected void onRefreshed() {
        super.onRefreshed();
        // FIXME: allow null data, uncomment the next line
        //setAttributeData(getAttributeData());
        // FIXME: remove the next lines
        CustomAttributeData data = getAttributeData();
        if (data != null) {
            setAttributeData(data);
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token.getJson());
        jsonObject.put("offset", offset.getJson());
        return jsonObject;
    }

    @Override
    public Object getJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token.getJson());
        jsonObject.put("offset", offset.getJson());
        return jsonObject;
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
