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
import com.reandroid.arsc.item.AlignItem;
import com.reandroid.utils.HexUtil;

import java.io.IOException;

public class MetadataAlignment extends AlignItem {

    public MetadataAlignment(int alignment) {
        super(alignment);
    }
    public MetadataAlignment() {
        super();
    }

    @Override
    public int align(int count) {
        if (hasValidVersion()) {
            return super.align(count);
        } else {
            setSize(0);
            return 0;
        }
    }
    @Override
    public int align(long count) {
        if (hasValidVersion()) {
            return super.align(count);
        } else {
            setSize(0);
            return 0;
        }
    }

    public void align(BlockReader reader) throws IOException {
        if (reader.isAvailable() && hasValidVersion()) {
            super.align(reader.getPosition());
            reader.readFully(getBytesInternal());
            if (!hasValidBytes()) {
                throw new IOException("Invalid alignment bytes, expecting "
                        + HexUtil.toHex2(getFill()) + ", but found ["
                        + HexUtil.toHexString(getBytesInternal()) + "], pos = "
                        + (reader.getPosition() - size()));
            }
        }
    }
    private boolean hasValidBytes() {
        byte fill = getFill();
        byte[] bytes = getBytesInternal();
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            if (bytes[i] != fill) {
                return false;
            }
        }
        return true;
    }
    private boolean hasValidVersion() {
        if (getAlignment() == 0) {
            return false;
        }
        MetadataSection<?> section = getParentSection();
        if (section != null) {
            return section.hasValidVersion();
        }
        return false;
    }
    private MetadataSection<?> getParentSection() {
        return getParentInstance(MetadataSection.class);
    }
}
