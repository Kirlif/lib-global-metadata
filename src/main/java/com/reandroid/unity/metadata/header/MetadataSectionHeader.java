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
package com.reandroid.unity.metadata.header;

import com.reandroid.arsc.base.Creator;
import com.reandroid.arsc.item.IndirectInteger;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.VersionRange;
import com.reandroid.unity.metadata.base.VersionSpecificItem;
import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.unity.metadata.section.MetadataSectionType;

public class MetadataSectionHeader extends VersionSpecificItem {

    private final IntegerReference mOffset;
    private final IntegerReference mSize;

    private final MetadataSectionType<?> sectionType;

    public MetadataSectionHeader(MetadataSectionType<?> sectionType, VersionRange versionRange) {
        super(versionRange, 8);
        this.mOffset = new IndirectInteger(this, 0);
        this.mSize = new IndirectInteger(this, 4);
        this.sectionType = sectionType;
    }
    public MetadataSectionHeader(MetadataSectionType<?> sectionType) {
        this(sectionType, null);
    }

    public MetadataSectionType<?> getSectionType() {
        return sectionType;
    }
    public Creator<? extends SectionData> getCreator() {
        return getSectionType().getCreator();
    }

    public String getName() {
        return getSectionType().name();
    }

    public int getOffset() {
        return getOffsetReference().get();
    }
    public void setOffset(int offset) {
        getOffsetReference().set(offset);
    }
    public int getSize() {
        return getSizeReference().get();
    }
    public void setSize(int size) {
        getSizeReference().set(size);
    }
    public IntegerReference getOffsetReference() {
        return mOffset;
    }
    public IntegerReference getSizeReference() {
        return mSize;
    }

    @Override
    public double getFileVersion() {
        return getParentInstance(MetadataFileHeader.class).getFileVersion();
    }

    @Override
    public Object getJson() {
        if (!hasValidVersion() && getSize() != 0) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("offset", getOffset());
        jsonObject.put("size", getSize());
        return jsonObject;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }


    @Override
    public String toString() {
        return getName() + "(" + getOffset() + ", " + getSize() + ")";
    }
}
