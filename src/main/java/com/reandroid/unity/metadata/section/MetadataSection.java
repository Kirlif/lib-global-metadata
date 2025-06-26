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
package com.reandroid.unity.metadata.section;

import com.reandroid.arsc.base.Creator;
import com.reandroid.arsc.container.FixedBlockContainer;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.json.JSONConvert;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.header.MetadataSectionHeader;
import com.reandroid.unity.metadata.base.LinkableItem;
import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.utils.ObjectsUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public class MetadataSection<T extends SectionData> extends FixedBlockContainer implements
        LinkableItem, Iterable<T>, JSONConvert<JSONObject> {

    private List<T> ITEMS;

    private final MetadataAlignment sectionAlignment;
    private final MetadataSectionHeader sectionHeader;
    private final MetadataEntryList<T> entryList;

    public MetadataSection(int childesCount, Creator<T> creator, MetadataSectionHeader sectionHeader) {
        super(childesCount + 2);
        this.sectionAlignment = new MetadataAlignment();
        this.entryList = new MetadataEntryList<>(creator);
        this.sectionHeader = sectionHeader;

        addChild(0, sectionAlignment);
        addChild(childesCount + 1, entryList);
    }
    public MetadataSection(int childesCount, MetadataSectionHeader sectionHeader) {
        this(childesCount, ObjectsUtil.cast(sectionHeader.getCreator()), sectionHeader);
    }
    public MetadataSection(Creator<T> creator, MetadataSectionHeader sectionHeader) {
        this(0, creator, sectionHeader);
    }
    public MetadataSection(MetadataSectionHeader sectionHeader) {
        this(0, ObjectsUtil.cast(sectionHeader.getCreator()), sectionHeader);
    }

    public MetadataSectionType<T> getSectionType() {
        return ObjectsUtil.cast(getSectionHeader().getSectionType());
    }
    public T getByIdx(int idx) {
        return get(idx);
    }
    public void getByIdx(Object[] array, int startIdx, int count) {
        for (int i = 0; i < count; i++) {
            array[i] = getByIdx(startIdx + i);
        }
    }
    public MetadataEntryList<T> getEntryList() {
        return entryList;
    }
    @Override
    public Iterator<T> iterator() {
        return entryList.iterator();
    }
    public T get(int i) {
        return entryList.get(i);
    }
    public T getOrCreateAt(int i) {
        return entryList.getOrCreateAt(i);
    }
    public T createNext() {
        return entryList.createNext();
    }
    public int getCount() {
        return entryList.size();
    }
    public boolean isEmpty() {
        return getCount() == 0;
    }
    public void clear() {
        entryList.clear();
    }
    public T searchByIdx(int i) {
        return entryList.searchByIdx(i);
    }
    public void onPreRemove(T item) {

    }

    public final MetadataSectionHeader getSectionHeader() {
        return sectionHeader;
    }
    public int getOffset() {
        return sectionHeader.getOffset();
    }
    public int setOffset(int offset) {
        offset += getSectionAlignment().align(offset);
        if (hasValidVersion()) {
            sectionHeader.setOffset(offset);
        }
        offset += getSectionSize();
        return offset;
    }
    public int getSectionSize() {
        return sectionHeader.getSize();
    }
    public void notifyLinkCompleted() {

    }

    @Override
    public boolean isNull() {
        return sectionHeader.isNull();
    }
    public boolean hasValidVersion() {
        return sectionHeader.hasValidVersion();
    }

    public MetadataAlignment getEntriesAlignment() {
        return entryList.getAlignment();
    }
    public MetadataAlignment getSectionAlignment() {
        return sectionAlignment;
    }

    @Override
    public int countBytes() {
        if (!hasValidVersion()) {
            return 0;
        }
        return super.countBytes();
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        if (!hasValidVersion()) {
            return;
        }
        if (getSectionHeader().getSize() == 0) {
            return;
        }
        reader.seek(getOffset());
        super.onReadBytes(reader);
    }

    @Override
    public int onWriteBytes(OutputStream stream) throws IOException {
        if (!hasValidVersion()) {
            return 0;
        }
        return super.onWriteBytes(stream);
    }
    public String getName() {
        return sectionHeader.getName();
    }
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", getCount());
        jsonObject.put(getSectionType().name(), getEntryList().toJson());
        return jsonObject;
    }

    @Override
    public void fromJson(JSONObject json) {
        entryList.fromJson(json.optJSONArray(getSectionType().name()));
    }

    int sizeOfEntry() {
        if (getVersion() == 0.0) {
            return 0;
        }
        T data = getEntryList().getLast();
        if (data == null) {
            data = getEntryList().getCreator().newInstance();
            data.setParent(this);
        }
        return data.computeUnitSize();
    }
    @Override
    public void link() {
        LinkableItem.linkAll(iterator());
    }

    public double getVersion() {
        MetadataSectionList sectionList = getParentInstance(MetadataSectionList.class);
        if (sectionList != null) {
            return sectionList.getVersion();
        }
        return 0.0;
    }

    // TODO: For debugging only, remove this latter
    private void touchDebug() {
        if (ITEMS == null) {
            ITEMS = new AbstractList<T>() {
                @Override
                public T get(int i) {
                    return MetadataSection.this.get(i);
                }

                @Override
                public int size() {
                    return MetadataSection.this.getCount();
                }
            };
        }
    }
    @Override
    public String toString() {
        touchDebug();
        return getSectionHeader().toString() + ", count=" + getCount();
    }
}
