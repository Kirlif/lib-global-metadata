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
import com.reandroid.unity.metadata.data.CodeStringData;
import com.reandroid.unity.metadata.header.MetadataSectionHeader;

import java.io.IOException;

public class SectionCodeString extends SectionStringData<CodeStringData> {

    public SectionCodeString(MetadataSectionHeader offsetSize) {
        super(offsetSize);
    }

    @Override
    public CodeStringData getByIdx(int offset) {
        return searchByIdx(offset);
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        reader.seek(getOffset());
        MetadataEntryList<CodeStringData> stringList = this.getEntryList();
        int size = getSectionSize();
        BlockReader stringReader = reader.create(size);
        while (stringReader.isAvailable()) {
            CodeStringData codeStringData = stringList.createNext();
            codeStringData.onReadBytes(stringReader);
        }
        getEntriesAlignment().align(reader);
        stringReader.close();
        reader.offset(size);
        buildMap();
    }
}
