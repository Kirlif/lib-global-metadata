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
package com.reandroid.unity.metadata.index;

import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.section.MetadataSectionList;
import com.reandroid.unity.metadata.base.VersionRange;
import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.unity.metadata.data.TypeDefinitionData;
import com.reandroid.unity.metadata.section.MetadataSection;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.TypeSpec;

public class CompressedTypeDefinitionIndex extends CompressedIntIndex<TypeDefinitionData> {

    public CompressedTypeDefinitionIndex(VersionRange versionRange) {
        super(versionRange);
    }
    public CompressedTypeDefinitionIndex() {
        this(null);
    }

    public String getName() {
        TypeDefinitionData data = getData();
        if (data != null) {
            return data.getName();
        }
        return null;
    }
    public String getTypeName() {
        TypeDefinitionData data = getData();
        if (data != null) {
            return data.getTypeName();
        }
        return null;
    }

    public TypeSpec getSpec() {
        TypeDefinitionData data = getData();
        if (data != null) {
            return data.getSpec();
        }
        return null;
    }

    @Override
    public void link() {
        TypeDefinitionData data = this.getData();
        if (data == null) {
            if (hasValidVersion()) {
                data = pullData(get());
                setData(data);
                if (data != null) {
                    data.link();
                }
            }
        }
    }
    @Override
    public TypeDefinitionData pullData(int idx) {
        if (idx != SectionData.INVALID_IDX) {
            MetadataSection<TypeDefinitionData> section = getSection();
            if (section != null) {
                return section.getByIdx(idx);
            }
        }
        return null;
    }
    @Override
    public MetadataSection<TypeDefinitionData> getSection() {
        MetadataSectionList sectionList = getParentInstance(MetadataSectionList.class);
        if (sectionList != null) {
            return sectionList.getSection(MetadataSectionType.TYPE_DEFINITIONS);
        }
        return null;
    }

    @Override
    public Object getJson() {
        TypeDefinitionData data = getData();
        if (data != null) {
            return data.getTypeName();
        }
        int idx = get();
        if (idx == SectionData.INVALID_IDX) {
            if (!hasValidVersion()) {
                return null;
            }
            return JSONObject.NULL;
        }
        return idx;
    }
    @Override
    public String toString() {
        return get() + "{" + getName() + "}";
    }
}
