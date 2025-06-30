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

import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.unity.metadata.data.BlobValueData;
import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.unity.metadata.header.MetadataSectionHeader;
import com.reandroid.unity.metadata.index.TypeDefinitionIndex;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class BlobDataPool extends MetadataEntryList<BlobValueData> {

    private final BlobByteArray blobByteArray;
    private boolean mModified;

    public BlobDataPool(MetadataSectionHeader sectionHeader) {
        super(1, sectionHeader.getCreator());
        IntegerReference sizeReference = sectionHeader.getSizeReference();
        IntegerReference readOnlyReference = new IntegerReference() {
            @Override
            public int get() {
                return sizeReference.get();
            }
            @Override
            public void set(int i) {
                // only section should set
            }
            @Override
            public String toString() {
                return Integer.toString(get());
            }
        };
        this.blobByteArray = new BlobByteArray(readOnlyReference);
        addChild(0, blobByteArray);
        getAlignment().setAlignment(8);
    }


    public int poolSize() {
        return getBlobByteArray().size();
    }
    private BlobByteArray getBlobByteArray() {
        return blobByteArray;
    }

    public BlobValueData get(TypeDefinitionIndex typeIndex, int offset) {
        BlobValueData data = searchByIdx(offset);
        if (data == null) {
            return null;
        }
        if (data.typeIndex().get() == typeIndex.get()) {
            return data;
        }
        int size = size();
        int start = data.getIndex();
        for (int i = start + 1; i < size; i++) {
            data = get(i);
            if (data.getOffset() == offset) {
                if (data.typeIndex().get() == typeIndex.get()) {
                    return data;
                }
            } else {
                break;
            }
        }
        for (int i = start - 1; i >= 0; i--) {
            data = get(i);
            if (data.getOffset() == offset) {
                if (data.typeIndex().get() == typeIndex.get()) {
                    return data;
                }
            } else {
                break;
            }
        }
        return null;
    }
    public BlobValueData getOrInitialize(TypeDefinitionIndex typeIndex, int offset) {
        if (offset == SectionData.INVALID_IDX) {
            return null;
        }
        BlobValueData data = get(typeIndex, offset);
        if (data != null) {
            return data;
        }
        if (offset >= poolSize()) {
            return null;
        }
        BlobValueData nearest = searchByIdx(true, offset);
        int index;
        if (nearest != null) {
            index = nearest.getIndex() + 1;
        } else {
            index = 0;
            BlobValueData last = getLast();
            if (last != null && offset > last.getOffset()) {
                index = last.getIndex() + 1;
            }
        }
        data = new BlobValueData(typeIndex, offset);
        add(index, data);
        return data;
    }

    void onLinkCompleted() {
        List<BlobValueData> dataList = asList();
        int count = dataList.size();
        for (int i = 0; i < count; i++) {
            BlobValueData data = dataList.get(i);
            data.linkValue();
        }
    }
    public BlockReader getBlockReader() {
        return getBlobByteArray().getBlockReader();
    }

    @Override
    int updateOffsetIdxData() {
        if (isModified()) {
            updatePoolBytes();
        }
        return countEntryBytes();
    }
    public void updatePoolBytes() {
        List<BlobValueData> sortedList = asList();
        sortedList.sort(BlobValueData::compareBytes);
        getBlobByteArray().writeEntries(sortedList.iterator());
        this.mModified = false;
    }

    @Override
    int countEntryBytes() {
        return getBlobByteArray().size();
    }
    @Override
    int onWriteEntryBytes(OutputStream stream) throws IOException {
        return getBlobByteArray().writeBytes(stream);
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        MetadataSectionHeader sectionHeader = getSectionHeader();
        reader.seek(sectionHeader.getOffset());
        getBlobByteArray().onReadBytes(reader);
        markModified();
    }
    public void markModified() {
        mModified = true;
    }
    public boolean isModified() {
        return mModified;
    }
}
