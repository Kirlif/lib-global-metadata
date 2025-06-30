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
import com.reandroid.arsc.container.BlockList;
import com.reandroid.arsc.container.FixedBlockContainer;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.json.JSONArray;
import com.reandroid.json.JSONConvert;
import com.reandroid.unity.metadata.data.OffsetIdxData;
import com.reandroid.unity.metadata.header.MetadataSectionHeader;
import com.reandroid.unity.metadata.base.IntegerSupplier;
import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.utils.ObjectsUtil;
import com.reandroid.utils.collection.ArrayCollection;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class MetadataEntryList<T extends SectionData> extends FixedBlockContainer
        implements JSONConvert<JSONArray> {

    private final BlockList<T> entryList;
    private final MetadataAlignment alignment;

    public MetadataEntryList(int childesCount, Creator<? extends T> creator) {
        super(childesCount + 2);
        this.entryList = new BlockList<T>(creator) {
            @Override
            public void onPreRemove(T item) {
                super.onPreRemove(item);
                MetadataEntryList.this.onPreRemove(item);
            }
        };
        this.alignment = new MetadataAlignment(0);

        addChild(childesCount, entryList);
        addChild(childesCount + 1, alignment);
    }

    public MetadataEntryList(Creator<? extends T> creator) {
        this(0, creator);
    }

    public T getOrCreateAt(int index) {
        ensureSize(index + 1);
        return get(index);
    }
    public void ensureSize(int size) {
        entryList.ensureSize(size);
    }

    public int size() {
        return entryList.size();
    }
    public void setSize(int size) {
        entryList.setSize(size);
    }
    public T get(int i) {
        return entryList.get(i);
    }
    public T getFirst() {
        return entryList.getFirst();
    }
    public T getLast() {
        return entryList.getLast();
    }
    public Iterator<T> iterator() {
        return entryList.iterator();
    }
    public Iterator<T> iteratorIf(Predicate<? super T> predicate) {
        return entryList.iterator(predicate);
    }
    public T remove(int index) {
        return entryList.remove(index);
    }
    public boolean remove(T item) {
        return entryList.remove(item);
    }
    public boolean removeIf(Predicate<? super T> filter) {
        return entryList.removeIf(filter);
    }
    public T createAt(int index) {
        return entryList.createAt(index);
    }
    public T createNext() {
        return entryList.createNext();
    }
    public Creator<? extends T> getCreator() {
        return entryList.getCreator();
    }
    public void add(T item) {
        entryList.add(item);
    }
    public void add(int i, T item) {
        entryList.add(i, item);
    }

    public boolean isEmpty() {
        return size() == 0;
    }
    public void clear() {
        entryList.clearChildes();
    }
    public List<T> asList() {
        return new ArrayCollection<>(entryList.getChildes());
    }
    public void onPreRemove(T item) {
        getParentSection().onPreRemove(item);
    }
    public MetadataAlignment getAlignment() {
        return alignment;
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        MetadataSectionHeader metadataSectionHeader = getSectionHeader();
        reader.seek(metadataSectionHeader.getOffset());
        int size = metadataSectionHeader.getSize();
        BlockReader sectionReader = reader.create(size);
        if (!readKnownSizeEntries(sectionReader)) {
            readVariableSizeEntries(sectionReader);
        }
        size = size - sectionReader.available();
        reader.offset(size);
        if (sectionReader.available() > 3) {
            throw new IOException("Unread items: " + reader + ", " + metadataSectionHeader.getSectionType());
        }
    }

    public void readEntries(BlockReader reader) throws IOException {
        entryList.readChildes(reader);
    }
    private boolean readKnownSizeEntries(BlockReader sectionReader) throws IOException {
        int entrySize = sizeOfEntry();
        if (entrySize == 0) {
            return false;
        }
        int available = sectionReader.available();
        int count = available / entrySize;
        if (count * entrySize != available) {
            // throw ?
            return false;
        }
        setSize(count);
        readEntries(sectionReader);
        return true;
    }
    private void readVariableSizeEntries(BlockReader sectionReader) throws IOException {
        int position = sectionReader.getPosition();
        while (sectionReader.isAvailable()) {
            T item = createNext();
            item.readBytes(sectionReader);
            int current = sectionReader.getPosition();
            if (position == current) {
                throw new IOException("StackOverflow: " + getParentSection());
            }
            position = current;
        }
    }

    @Override
    public int countBytes() {
        int count = countEntryBytes();
        count += getAlignment().size();
        return count;
    }
    int countEntryBytes() {
        int count = countBytesFast();
        if (count == 0) {
            count = entryList.countBytes();
        }
        return count;
    }

    private int countBytesFast() {
        int unit = sizeOfEntry();
        if (unit != 0) {
            return unit * size();
        }
        return 0;
    }
    private int sizeOfEntry() {
        MetadataSection<T> section = getParentSection();
        if (section != null) {
            return section.sizeOfEntry();
        }
        return 0;
    }
    public T searchByIdx(int idx) {
        return searchByIdx(false, idx);
    }
    public T searchByIdx(boolean nearest, int idx) {
        return search(nearest, idx, SectionData::getIdx);
    }
    public T search(int idx, IntegerSupplier<? super T> supplier) {
        return search(false, idx, supplier);
    }
    public T search(boolean nearest, int idx, IntegerSupplier<? super T> supplier) {
        if (idx == SectionData.INVALID_IDX) {
            return null;
        }
        return binarySearch(nearest, idx, supplier);
    }
    public T binarySearch(boolean nearest, int idx, IntegerSupplier<? super T> supplier) {
        // Assumed all entries are ordered in ascending offset
        int start = 0;
        int end = size() - 1;
        T nearestItem = null;
        while (end >= start) {
            int mid = start + ((end - start) / 2);
            T item = get(mid);
            int test = supplier.compute(item);
            if (test == idx) {
                return item;
            }
            if (test < idx) {
                if (nearest) {
                    nearestItem = item;
                }
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return nearestItem;
    }

    @Override
    protected void onRefreshed() {
        super.onRefreshed();
        MetadataSection<T> section = getParentSection();
        if (section != null) {
            MetadataAlignment alignment = getAlignment();
            alignment.clear();
            int size = updateOffsetIdxData();
            size += alignment.align(size);
            section.getSectionHeader().setSize(size);
        }
    }
    int updateOffsetIdxData() {
        BlockList<T> entryList = this.entryList;
        if (!(entryList.getFirst() instanceof OffsetIdxData)) {
            return countBytes();
        }
        int size = entryList.size();
        int offset = 0;
        for (int i = 0; i < size; i++) {
            OffsetIdxData data = (OffsetIdxData) entryList.get(i);
            data.setOffset(offset);
            offset += data.getDataSize();
        }
        return offset;
    }
    @Override
    public int onWriteBytes(OutputStream stream) throws IOException {
        int length = onWriteEntryBytes(stream);
        length += getAlignment().writeBytes(stream);
        return length;
    }
    int onWriteEntryBytes(OutputStream stream) throws IOException {
        return entryList.writeBytes(stream);
    }


    public MetadataSectionHeader getSectionHeader() {
        MetadataSection<T> section = getParentSection();
        if (section != null) {
            return section.getSectionHeader();
        }
        return null;
    }
    private MetadataSection<T> getParentSection() {
        return ObjectsUtil.cast(getParentInstance(MetadataSection.class));
    }
    @Override
    public JSONArray toJson() {
        int count = size();
        JSONArray jsonArray = new JSONArray(count);
        for (int i = 0; i < count; i++) {
            jsonArray.put(get(i).getJson());
        }
        return jsonArray;
    }

    @Override
    public void fromJson(JSONArray json) {

    }

    @Override
    public String toString() {
        return getSectionHeader().getSectionType() + " [" + size() + "]";
    }
}
