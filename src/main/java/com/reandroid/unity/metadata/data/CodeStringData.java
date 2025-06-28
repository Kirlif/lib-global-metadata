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
import java.io.OutputStream;
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

    public CodeStringData createOverlappingAt(int offset) {
        int baseOffset = getOffset();
        if (offset == baseOffset) {
            return this;
        }
        int size = getDataSize();
        if (offset < baseOffset || offset >= baseOffset + getDataSize()) {
            throw new IndexOutOfBoundsException("Offset out of range: "
                    + offset + ", base = " + baseOffset + ", size = " + size);
        }
        return new Overlapping(this, offset - baseOffset);
    }
    @Override
    public boolean isOverlapping() {
        return false;
    }
    @Override
    public CodeStringData base() {
        return null;
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
        jsonObject.put("idx", getIdx());
        jsonObject.put("string", get());
        return jsonObject;
    }

    @Override
    public void fromJson(JSONObject json) {
        set(json.getString("string"));
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idx", getIdx());
        jsonObject.put("string", get());
        return jsonObject;
    }

    @Override
    public MetadataSectionType<CodeStringData> getSectionType() {
        return MetadataSectionType.CODE_STRING;
    }

    @Override
    public CodeStringData getReplacement() {
        return (CodeStringData) super.getReplacement();
    }

    @Override
    public String toString() {
        return get();
    }

    static class Overlapping extends CodeStringData {

        private final CodeStringData base;
        private final int relativeOffset;

        public Overlapping(CodeStringData base, int relativeOffset) {
            super();
            this.base = base;
            this.relativeOffset = relativeOffset;

            this.setParent(base.getParent());
            this.setIndex(base.getIndex());
        }

        public CodeStringData base() {
            return base.getReplacement();
        }
        @Override
        public String get() {
            byte[] bytes = base().getBytes();
            int length = bytes.length - relativeOffset - 1;
            return new String(bytes,
                    relativeOffset,
                    length,
                    StandardCharsets.UTF_8);
        }
        @Override
        public void set(String text) {
            if (!text.equals(get())) {
                throw new IllegalArgumentException(
                        "Can not set string on overlapped string");
            }
        }
        @Override
        public int getOffset() {
            return base().getOffset() + relativeOffset;
        }
        @Override
        public void setOffset(int offset) {
            if (offset != getOffset()) {
                throw new IllegalArgumentException(
                        "Can not set offset on overlapped string");
            }
        }
        @Override
        public int getDataSize() {
            return base().getDataSize() - relativeOffset;
        }
        @Override
        public CodeStringData createOverlappingAt(int offset) {
            return base().createOverlappingAt(offset + relativeOffset);
        }
        @Override
        public boolean isOverlapping() {
            return true;
        }
        @Override
        public boolean isRemoved() {
            return base().isRemoved();
        }

        @Override
        public void removeSelf() {
            super.removeSelf();
        }

        @Override
        public byte[] getBytes() {
            byte[] baseBytes = base().getBytes();
            int start = relativeOffset;
            int length = baseBytes.length - start;
            byte[] bytes = new byte[length];
            System.arraycopy(baseBytes, start, bytes, 0, length);
            return bytes;
        }

        @Override
        public void onReadBytes(BlockReader reader) throws IOException {
            throw new IOException("Overlapping string");
        }
        @Override
        public int onWriteBytes(OutputStream stream) throws IOException {
            throw new IOException("Overlapping string");
        }

        @Override
        public void fromJson(JSONObject json) {
        }
        @Override
        public JSONObject toJson() {
            JSONObject jsonObject = super.toJson();
            jsonObject.put("base", base().get());
            jsonObject.put("base_idx", base().getIdx());
            return jsonObject;
        }
    }
}
