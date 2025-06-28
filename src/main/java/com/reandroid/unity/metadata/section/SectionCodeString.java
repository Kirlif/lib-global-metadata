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
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.data.CodeStringData;
import com.reandroid.unity.metadata.header.MetadataSectionHeader;
import com.reandroid.unity.metadata.util.JsonDataUtil;

import java.io.IOException;
import java.util.List;

public class SectionCodeString extends SectionStringData<CodeStringData> {

    public SectionCodeString(MetadataSectionHeader offsetSize) {
        super(offsetSize);
    }

    @Override
    public CodeStringData getByIdx(int offset) {
        CodeStringData data = searchByIdx(offset);
        if (data == null) {
            data = createOverlapping(offset);
        }
        return data;
    }

    @Override
    public void remove(CodeStringData item) {
        if (item != null && item.isOverlapping()) {
            onPreRemove(item);
        } else {
            super.remove(item);
        }
    }

    @Override
    public void onPreRemove(CodeStringData item) {
        if (item != null && item.isOverlapping()) {
            if (getPoolMap().removeAppendix(item) != null) {
                item.setParent(null);
            }
        }
        super.onPreRemove(item);
    }

    private CodeStringData createOverlapping(int offset) {
        CodeStringData data = null;
        CodeStringData nearest = searchByIdx(true, offset);
        if (nearest != null) {
            data = nearest.createOverlappingAt(offset);
            StringPoolMap<CodeStringData> poolMap = getPoolMap();
            String key = data.get();
            CodeStringData exist = poolMap.get(key);
            if (exist == null) {
                poolMap.putAppendix(key, data);
            } else {
                data = exist;
            }
        }
        return data;
    }

    public boolean optimize() {
        boolean optimized = clearDuplicates();
        List<CodeStringData> dataList = asList();
        dataList.sort(CodeStringData::compareReversed);
        int size = dataList.size();
        boolean overlapped = false;
        for (int i = 0; i < size; i++) {
            CodeStringData data = dataList.get(i);
            if (data.getDataSize() < 2) {
                continue;
            }
            CodeStringData next = null;
            for (int j = i + 1; j < size; j++) {
                CodeStringData test = dataList.get(j);
                if (test.endsWith(data)) {
                    next = test;
                } else {
                    break;
                }
            }
            if (next == null || data.equalsTo(next)) {
                continue;
            }
            CodeStringData replace = createOverlapping(next.getOffset() + next.getDataSize() - data.getDataSize());
            if (!overlapped && replace.isOverlapping()) {
                overlapped = true;
            }
        }
        if (overlapped) {
            optimized = true;
            clearDuplicates();
        }
        return optimized;
    }
    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        reader.seek(getOffset());
        MetadataEntryList<CodeStringData> stringList = this.getEntryList();
        int size = getSectionSize();
        BlockReader stringReader = reader.create(size);
        while (stringReader.isAvailable()) {
            stringList.createNext().onReadBytes(stringReader);
        }
        getEntriesAlignment().align(reader);
        stringReader.close();
        reader.offset(size);
        reBuildMap();
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = super.toJson();
        if (jsonObject != null) {
            StringPoolMap<CodeStringData> poolMap = getPoolMap();
            int size = poolMap.appendicesSize();
            if (size != 0) {
                jsonObject.put("overlapped_count", size);
                jsonObject.put("overlapped_strings",
                        JsonDataUtil.collectOptionalToJson(poolMap.appendices()));
            }
        }
        return jsonObject;
    }
}
