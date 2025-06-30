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
import com.reandroid.unity.metadata.header.MetadataSectionHeader;
import com.reandroid.unity.metadata.data.BlobValueData;
import com.reandroid.unity.metadata.index.TypeDefinitionIndex;

import java.io.IOException;

public class SecFieldAndParameterDefaultValueData extends MetadataSection<BlobValueData> {

    public SecFieldAndParameterDefaultValueData(MetadataSectionHeader sectionHeader) {
        super(new BlobDataPool(sectionHeader), sectionHeader);
    }

    @Override
    public BlobDataPool getEntryList() {
        return (BlobDataPool) super.getEntryList();
    }
    @Override
    public BlobValueData getByIdx(int idx) {
        return searchByIdx(idx);
    }

    @Override
    public boolean optimize() {
        BlobDataPool dataPool = getEntryList();
        int initialSize = dataPool.poolSize();
        dataPool.updatePoolBytes();
        return initialSize != dataPool.poolSize();
    }

    @Override
    public void notifyLinkCompleted() {
        getEntryList().onLinkCompleted();
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        reader.seek(getOffset());
        getEntryList().onReadBytes(reader);
        getSectionAlignment().align(reader);
    }
    public BlobValueData getData(TypeDefinitionIndex typeIndex, int offset) {
        return getEntryList().getOrInitialize(typeIndex, offset);
    }
    @Override
    int sizeOfEntry() {
        return 0;
    }
}
