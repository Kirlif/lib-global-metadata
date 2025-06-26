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
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.unity.metadata.header.MetadataSectionHeader;
import com.reandroid.unity.metadata.data.BlobValueData;
import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.unity.metadata.index.TypeDefinitionIndex;
import com.reandroid.utils.CompareUtil;
import com.reandroid.utils.collection.ArrayCollection;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecFieldAndParameterDefaultValueData extends MetadataSection<BlobValueData> {

    private final BlobDataPool dataPool;
    private final Map<Integer, BlobValueData> dataMap;

    public SecFieldAndParameterDefaultValueData(MetadataSectionHeader offsetSize) {
        super(1, CREATOR, offsetSize);
        IntegerReference size = offsetSize.getSizeReference();
        this.dataPool = new BlobDataPool(new IntegerReference() {
            @Override
            public int get() {
                return size.get();
            }
            @Override
            public void set(int value) {
            }
            @Override
            public String toString() {
                return Integer.toString(get());
            }
        });
        this.dataMap = new HashMap<>();

        addChild(1, dataPool);

        getSectionAlignment().setAlignment(8);
    }

    @Override
    public BlobValueData getByIdx(int idx) {
        return searchByIdx(idx);
    }

    public BlobDataPool getDataPool() {
        return dataPool;
    }

    @Override
    public void notifyLinkCompleted() {
        if (getCount() != 0 || getDataPool().size() == 0) {
            return;
        }
        linkValues();
        List<BlobValueData> dataMapList = getDataMapList();
        MetadataEntryList<BlobValueData> list = getEntryList();
        for (BlobValueData data : dataMapList) {
            list.add(data);
        }
        this.dataMap.clear();
        getDataPool().clear();
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        reader.seek(getOffset());
        getDataPool().onReadBytes(reader);
    }
    private List<BlobValueData> getDataMapList() {
        List<BlobValueData> dataList = new ArrayCollection<>(dataMap.values());
        dataList.sort(CompareUtil.getComparableComparator());
        return dataList;
    }
    private void linkValues() {
        List<BlobValueData> dataList = getDataMapList();
        int blobSize = getDataPool().size();
        int count = dataList.size();
        int lastIndex = count - 1;
        for (int i = 0; i < count; i++) {
            BlobValueData data = dataList.get(i);
            int nextOffset;
            if (i == lastIndex) {
                nextOffset = blobSize;
            } else {
                nextOffset = dataList.get(i + 1).getIdx();
            }
            data.initUnknownValue(nextOffset);
            int gap = nextOffset - data.getIdx() - data.getDataSize();
            if (gap > 0) {
                int index = nextOffset - gap;
                BlobValueData holder = getData(TypeDefinitionIndex.NO_TYPE, index);
                holder.initUnknownValue(nextOffset);
            }
        }
    }

    public BlobValueData getData(TypeDefinitionIndex typeIndex, int offset) {
        if (offset == SectionData.INVALID_IDX) {
            return null;
        }
        MetadataEntryList<BlobValueData> dataList = getEntryList();
        if (!dataList.isEmpty()) {
            return dataList.searchByIdx(offset);
        }
        BlobDataPool dataPool = getDataPool();
        if (offset >= dataPool.size()) {
            return null;
        }
        BlobValueData data = this.dataMap.get(offset);
        if (data == null) {
            data = new BlobValueData(typeIndex, offset);
            data.setParent(dataList);
            data.setIndex(dataMap.size());
            this.dataMap.put(offset, data);
        }
        return data;
    }
    @Override
    int sizeOfEntry() {
        return 0;
    }

    private static final Creator<BlobValueData> CREATOR = () -> {
        throw new RuntimeException("Creator not supported");
    };
}
