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
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.unity.metadata.base.MDByteArray;
import com.reandroid.unity.metadata.data.BlobValueData;

import java.io.IOException;
import java.util.Iterator;

public class BlobByteArray extends MDByteArray {

    private BlockReader blockReader;
    private int position;
    private int growSize;

    public BlobByteArray(IntegerReference sizeReference) {
        super(sizeReference);
        this.growSize = 4096;
    }

    public void writeEntries(Iterator<BlobValueData> iterator) {
        reset();
        while (iterator.hasNext()) {
            BlobValueData valueData = iterator.next();
            byte[] bytes = valueData.getBytes();
            int offset = getOrCreateOffset(bytes);
            valueData.setOffset(offset);
        }
        trimToPosition();
    }
    private int getOrCreateOffset(byte[] bytes) {
        int offset = indexOf(bytes);
        if (offset == -1) {
            offset = position();
            writeBytes(offset, bytes);
        }
        return offset;
    }
    public int indexOf(byte[] bytes) {
        byte[] array = getBytesInternal();
        int length = position() - bytes.length;
        for (int i = 0; i < length; i++) {
            if (startsAt(i, array, bytes)) {
                return i;
            }
        }
        return -1;
    }
    public boolean startsAt(int index, byte[] array, byte[] bytes) {
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            if (bytes[i] != array[index + i]) {
                return false;
            }
        }
        return true;
    }
    public void reset() {
        position(0);
    }

    public int position() {
        return position;
    }
    public void position(int pos) {
        this.position = pos;
    }

    @Override
    public void setSize(int size) {
        super.setSize(size);
        if (size < position()) {
            position(size);
        }
    }

    public BlockReader getBlockReader() {
        BlockReader reader = this.blockReader;
        if (reader == null) {
            reader = new BlockReader(getBytesInternal(), 0, position());
            this.blockReader = reader;
        }
        return reader;
    }
    @Override
    protected void onBytesChanged() {
        super.onBytesChanged();
        this.blockReader = null;
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        super.onReadBytes(reader);
        int size = size();
        position(size);
        setGrowSize(size + (size / 2));
    }

    private void trimToPosition() {
        setSize(position());
    }
    private void writeBytes(int offset, byte[] bytes) {
        int length = bytes.length;
        ensureCapacity(length);
        putByteArray(offset, bytes);
        int pos = offset + length;
        if (pos > position()) {
            position(pos);
        }
    }
    private void ensureCapacity(int capacity) {
        if (capacity > capacity()) {
            int size = this.growSize;
            if (size < capacity) {
                size = capacity;
            }
            setSize(size);
        }
    }
    private int capacity() {
        return size() - position();
    }
    private void setGrowSize(int size) {
        if (size < 1000) {
            size = 1000;
        }
        this.growSize = size;
    }

    @Override
    public String toString() {
        return "size = " + size() + ", position = " + position();
    }
}
