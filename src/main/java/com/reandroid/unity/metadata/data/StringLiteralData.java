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
package com.reandroid.unity.metadata.data;

import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.StringBlock;
import com.reandroid.unity.metadata.section.MetadataEntryList;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.utils.ObjectsUtil;
import com.reandroid.utils.StringsUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;

public class StringLiteralData extends MDString implements OffsetIdxData {

    private final StringLiteral stringLiteral;

    public StringLiteralData(StringLiteral stringLiteral) {
        super();
        this.stringLiteral = stringLiteral;
        stringLiteral.linkDataInternal(this);
    }

    @Override
    public int getIdx() {
        return getOffset();
    }
    @Override
    public int getOffset() {
        return stringLiteral().offset().get();
    }
    @Override
    public void setOffset(int offset) {
        stringLiteral().offset().set(offset);
    }
    @Override
    public int getDataSize() {
        return countBytes();
    }

    public StringLiteral stringLiteral() {
        return stringLiteral;
    }

    @Override
    protected void onStringChanged(String old, String text) {
        super.onStringChanged(old, text);
        stringLiteral().length().set(getDataSize());
    }
    public void removeSelf(StringLiteral request) {
        if (request == null) {
            return;
        }
        if (stringLiteral() != request) {
            throw new IllegalArgumentException("Invalid remove self request");
        }
        MetadataEntryList<StringLiteralData> entryList = getParentEntryList();
        if (entryList != null) {
            entryList.remove(this);
        }
    }

    @Override
    protected String decodeString(byte[] bytes) {
        int length = bytes.length;
        if (length == 0) {
            return StringsUtil.EMPTY;
        }
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        try {
            return StringBlock.UTF8_DECODER.decode(buf).toString();
        } catch (CharacterCodingException e) {
            return new String(bytes);
        }
    }
    @Override
    protected byte[] encodeString(String text) {
        return text.getBytes(StandardCharsets.UTF_8);
    }
    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        int position = reader.getPosition();
        StringLiteral stringLiteral = stringLiteral();
        reader.seek(stringLiteral.offset().get());
        setBytesLength(stringLiteral.length().get());
        reader.readFully(getBytes());
        if (position > reader.getPosition()) {
            reader.seek(position);
        }
        onBytesChanged();
    }

    @Override
    public MetadataSectionType<StringLiteralData> getSectionType() {
        return MetadataSectionType.STRING_LITERAL_DATA;
    }
    @Override
    public MetadataEntryList<StringLiteralData> getParentEntryList() {
        return ObjectsUtil.cast(super.getParentEntryList());
    }

    @Override
    public String toString() {
        return get();
    }
}
