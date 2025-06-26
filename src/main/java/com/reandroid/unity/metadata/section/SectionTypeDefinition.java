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
import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.unity.metadata.header.MetadataSectionHeader;
import com.reandroid.unity.metadata.data.TypeDefinitionData;
import com.reandroid.utils.collection.ArraySort;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SectionTypeDefinition extends MetadataSection<TypeDefinitionData> {

    private boolean mByValBuilt;
    private boolean mByValBuilding;

    public SectionTypeDefinition(MetadataSectionHeader sectionHeader) {
        super(sectionHeader);
    }


    @Override
    public TypeDefinitionData getByIdx(int idx) {
        if (idx == SectionData.INVALID_IDX) {
            return null;
        }
        MetadataEntryList<TypeDefinitionData> entryList = getEntryList();
        int size = entryList.size();
        if (size == 0) {
            return null;
        }
        if (idx >= size & mByValBuilt) {
            int lastIdx = size - 1;
            TypeDefinitionData last = entryList.get(lastIdx);
            for (int i = lastIdx; i < idx; i++) {
                entryList.add(last.createCopy());
            }
        }
        return entryList.get(idx);
    }
    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        super.onReadBytes(reader);
        buildByVal();
    }
    private void buildByVal() {
        MetadataEntryList<TypeDefinitionData> entryList = getEntryList();
        int entryListSize = entryList.size();
        if (entryListSize == 0) {
            return;
        }
        if (mByValBuilt || mByValBuilding) {
            return;
        }
        mByValBuilding = true;

        int lastEntryId = entryList.get(entryListSize - 1).getIdx();
        int[] byValIds = new int[entryListSize];
        Map<Integer, TypeDefinitionData> map = new HashMap<>(entryListSize);
        for (int i = 0; i < entryListSize; i++) {
            TypeDefinitionData data = entryList.get(i);
            int id = data.getByVal();
            Integer key = id;
            if (id != SectionData.INVALID_IDX && !map.containsKey(key)) {
                map.put(key, data);
            } else {
                id = SectionData.INVALID_IDX;
            }
            byValIds[i] = id;
        }
        ArraySort.sort(byValIds);
        int byValStart = 0;
        while (byValIds[byValStart] <= lastEntryId) {
            byValStart ++;
        }
        TypeDefinitionData last = entryList.getLast();
        for (int i = byValStart; i < entryListSize; i++) {
            int id = byValIds[i];
            TypeDefinitionData next = map.get(id);
            lastEntryId = last.getIdx() + 1;
            for (int j = lastEntryId; j < id; j++) {
                entryList.add(last.createCopy());
            }
            entryList.add(next.createCopy());
            last = entryList.getLast();
        }
        mByValBuilt = true;
        mByValBuilding = false;
    }
}

