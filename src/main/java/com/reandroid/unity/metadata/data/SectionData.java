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

import com.reandroid.arsc.container.FixedBlockContainer;
import com.reandroid.json.JSONConvert;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.section.MetadataSectionList;
import com.reandroid.unity.metadata.base.JsonData;
import com.reandroid.unity.metadata.base.LinkableItem;
import com.reandroid.unity.metadata.section.MetadataEntryList;
import com.reandroid.unity.metadata.section.MetadataSection;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.Spec;
import com.reandroid.utils.ObjectsUtil;


public class SectionData extends FixedBlockContainer implements
        IdxData, LinkableItem, JSONConvert<JSONObject>, JsonData {

    public SectionData(int childesCount) {
        super(childesCount);
    }

    @Override
    public int getIdx() {
        return getIndex();
    }

    public Spec getSpec() {
        return null;
    }

    public double getGlobalMetadataVersion() {
        MetadataSection<?> section = getParentSection();
        if (section != null) {
            return section.getVersion();
        }
        return 0;
    }
    public MetadataSection<?> getParentSection() {
        return getParentInstance(MetadataSection.class);
    }
    public MetadataEntryList<?> getParentEntryList() {
        return getParentInstance(MetadataEntryList.class);
    }

    public<T extends SectionData> MetadataSection<T> getSection(MetadataSectionType<T> sectionType) {
        MetadataSectionList sectionList = getParentInstance(MetadataSectionList.class);
        if (sectionList != null) {
            return sectionList.getSection(sectionType);
        }
        return ObjectsUtil.getNull();
    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    @Override
    public void fromJson(JSONObject json) {

    }
    @Override
    public Object getJson() {
        return toJson();
    }
    @Override
    public void setJson(Object obj) {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public void link() {
        LinkableItem.linkBlockContainer(this);
    }
    public int computeUnitSize() {
        return countBytes();
    }
    public MetadataSectionType<?> getSectionType() {
        MetadataSection<?> section = getParentSection();
        if (section != null) {
            return section.getSectionType();
        }
        return null;
    }

    public static final int INVALID_IDX = ObjectsUtil.of(-1);
}
