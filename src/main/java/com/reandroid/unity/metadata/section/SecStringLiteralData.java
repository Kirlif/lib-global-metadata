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
import com.reandroid.unity.metadata.header.MetadataSectionHeader;
import com.reandroid.unity.metadata.data.StringLiteral;
import com.reandroid.unity.metadata.data.StringLiteralData;

import java.io.IOException;

public class SecStringLiteralData extends SectionStringData<StringLiteralData> {

    private final SectionStringLiteral sectionStringLiteral;

    public SecStringLiteralData(MetadataSectionHeader offsetSize, SectionStringLiteral sectionStringLiteral) {
        super(new StringCreator(sectionStringLiteral), offsetSize);
        this.sectionStringLiteral = sectionStringLiteral;
    }

    @Override
    public StringLiteralData getByIdx(int i) {
        return searchByIdx(i);
    }

    @Override
    public boolean clearDuplicates() {
        return false;
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        reader.seek(getOffset());
        int size = getSectionHeader().getSize();
        BlockReader sectionReader = reader.create(size);
        MetadataEntryList<StringLiteralData> entryList = getEntryList();
        entryList.setSize(sectionStringLiteral.getCount());
        entryList.readEntries(sectionReader);
        getEntriesAlignment().align(sectionReader);
        reader.seek(size);
        reBuildMap();
    }

    static class StringCreator implements Creator<StringLiteralData> {

        private final SectionStringLiteral sectionStringLiteral;

        StringCreator(SectionStringLiteral sectionStringLiteral) {
            this.sectionStringLiteral = sectionStringLiteral;
        }

        @Override
        public StringLiteralData newInstance() {
            throw new IllegalArgumentException("Must call newInstanceAt(int)");
        }
        @Override
        public StringLiteralData newInstanceAt(int index) {
            StringLiteral stringLiteral = sectionStringLiteral.getOrCreateAt(index);
            return new StringLiteralData(stringLiteral);
        }
    }
}
