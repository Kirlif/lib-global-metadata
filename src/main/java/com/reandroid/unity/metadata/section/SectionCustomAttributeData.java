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
import com.reandroid.unity.metadata.data.CustomAttributeData;

import java.io.IOException;

public class SectionCustomAttributeData extends MetadataSection<CustomAttributeData> {


    public SectionCustomAttributeData(MetadataSectionHeader sectionHeader) {
        super(sectionHeader);

        MetadataAlignment alignment = getEntriesAlignment();
        alignment.setAlignment(4);
        alignment.setFill((byte) 0xcc);
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        if (!hasValidVersion()) {
            return;
        }
        MetadataSectionHeader metadataSectionHeader = getSectionHeader();
        reader.seek(metadataSectionHeader.getOffset());
        int size = metadataSectionHeader.getSize();
        BlockReader sectionReader = reader.create(size);
        while (sectionReader.available() > 3) {
            CustomAttributeData data = createNext();
            data.readBytes(sectionReader);
        }
        getEntriesAlignment().align(sectionReader);
        reader.offset(size);
        sectionReader.close();
    }

    @Override
    public CustomAttributeData getByIdx(int idx) {
        return searchByIdx(idx);
    }

    @Override
    public boolean optimize() {
        return clearDuplicates();
    }

    public boolean clearDuplicates() {
        // TODO: confirm compressible
        return false;
    }
    @Override
    int sizeOfEntry() {
        return 0;
    }
}
