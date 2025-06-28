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

import com.reandroid.arsc.base.BlockRefresh;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.unity.metadata.section.MetadataSectionList;
import com.reandroid.unity.metadata.base.VersionRange;
import com.reandroid.unity.metadata.base.LinkableItem;
import com.reandroid.unity.metadata.base.MDInt;
import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.unity.metadata.section.MetadataSection;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.Spec;
import com.reandroid.utils.ObjectsUtil;


public class DefinitionIndex<T extends SectionData> extends MDInt implements
        BlockRefresh, LinkableItem {

    private T mData;

    private final MetadataSectionType<T> sectionType;
    private final IntegerReference reference;

    public DefinitionIndex(MetadataSectionType<T> sectionType, VersionRange versionRange) {
        super(versionRange);
        this.sectionType = sectionType;
        this.reference = null;
        super.set(SectionData.INVALID_IDX);
    }
    public DefinitionIndex(MetadataSectionType<T> sectionType) {
        this(sectionType, null);
    }
    public DefinitionIndex(MetadataSectionType<T> sectionType, VersionRange versionRange, IntegerReference reference) {
        super(0, versionRange);
        this.sectionType = sectionType;
        this.reference = reference;
    }


    public Spec getSpec() {
        T data = getData();
        if (data != null) {
            return data.getSpec();
        }
        return null;
    }
    public T getData() {
        return mData;
    }
    public void setDataInternal(T data) {
        this.mData = data;
    }

    public void setData(T data) {
        if (data == null) {
            set(SectionData.INVALID_IDX);
        } else {
            set(idxOf(data));
        }
        setDataInternal(data);
    }
    public int idxOf(T data) {
        return data.getIdx();
    }

    public T pullData(int idx) {
        if (idx != SectionData.INVALID_IDX) {
            MetadataSection<T> section = getSection();
            if (section != null) {
                return pullData(section, idx);
            }
        }
        return null;
    }
    public T pullData(MetadataSection<T> section, int idx) {
        return section.getByIdx(idx);
    }
    @Override
    public void link() {
        int idx = get();
        T data = this.mData;
        if (needLinking(idx, data)) {
            data = pullData(idx);
            this.mData = data;
            if (data != null) {
                onLinked(data);
            } else {
                onLinkFailed(idx);
            }
        }
    }

    protected void onLinked(T data) {
        if (this.getParent(data.getClass()) != data) {
            data.onIndexLinked(this);
        }
    }
    protected void onLinkFailed(int idx) {
    }
    protected boolean needLinking(int idx, T data) {
        return idx != SectionData.INVALID_IDX && data == null && hasValidVersion();
    }

    @Override
    public final void refresh() {
        if (enableUpdate()) {
            update();
        }
    }
    public boolean enableUpdate() {
        return false;
    }

    private void update() {
        T data = this.getData();
        if (data != null) {
            T replace = ObjectsUtil.cast(data.getReplacement());
            if (replace != data) {
                setDataInternal(replace);
                data = replace;
            }
        }
        int idx;
        if (data == null) {
            idx = SectionData.INVALID_IDX;
        } else {
            idx = idxOf(data);
        }
        set(idx);
    }

    public MetadataSection<T> getSection() {
        MetadataSectionList sectionList = getSectionList();
        if (sectionList != null) {
            return sectionList.getSection(getSectionType());
        }
        return null;
    }

    public MetadataSectionType<? extends T> getSectionType() {
        return sectionType;
    }

    public MetadataSectionList getSectionList() {
        return getParentInstance(MetadataSectionList.class);
    }

    @Override
    public Object getJson() {
        if (isNull()) {
            return null;
        }
        Spec spec = getSpec();
        if (spec != null) {
            return spec.json();
        }
        T data = getData();
        if (data != null) {
            return data.getJson();
        }
        int idx = get();
        if (idx == SectionData.INVALID_IDX) {
            return null;
        }
        return idx;
    }

    @Override
    public int get() {
        if (reference != null) {
            return reference.get();
        }
        return super.get();
    }
    @Override
    public void set(int value) {
        if (reference != null) {
            reference.set(value);
        } else {
            super.set(value);
        }
    }

    @Override
    public String toString() {
        Spec spec = getSpec();
        if (spec != null) {
            return spec.toString();
        }
        return Integer.toString(get());
    }
}
