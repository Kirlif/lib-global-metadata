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
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.utils.StringsUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;

public class CodeStringData extends MDString implements OffsetIdxData {

    private int mOffset;

    public CodeStringData() {
        super();
        this.mOffset = SectionData.INVALID_IDX;
    }

    @Override
    public int getIdx() {
        return getOffset();
    }

    @Override
    public int getOffset() {
        return mOffset;
    }
    @Override
    public void setOffset(int offset) {
        this.mOffset = offset;
    }
    @Override
    public int getDataSize() {
        return countBytes();
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        setOffset(reader.getPosition());
        int length = findEndingZero(reader);
        setBytesLength(length);
        byte[] bytes = getBytes();
        reader.readFully(bytes);
        onBytesChanged();
    }
    private int findEndingZero(BlockReader reader) throws IOException {
        int position = reader.getPosition();
        int length = 1;
        while (reader.read() != 0) {
            length ++;
        }
        reader.seek(position);
        return length;
    }
    @Override
    protected String decodeString(byte[] bytes) {
        int length = bytes.length;
        if (length < 2) {
            return StringsUtil.EMPTY;
        }
        ByteBuffer buf = ByteBuffer.wrap(bytes, 0, length - 1);
        try {
            return StringBlock.UTF8_DECODER.decode(buf).toString();
        } catch (CharacterCodingException e) {
            return new String(bytes);
        }
    }
    @Override
    protected byte[] encodeString(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        int length = bytes.length;
        byte[] result = new byte[length + 1];
        System.arraycopy(bytes, 0, result, 0, length);
        return result;
    }

    @Override
    public Object getJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", getIdx());
        jsonObject.put("string", get());
        return jsonObject;
    }

    @Override
    public MetadataSectionType<CodeStringData> getSectionType() {
        return MetadataSectionType.CODE_STRING;
    }
    @Override
    public String toString() {
        return get();
    }
}
