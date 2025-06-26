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
package com.reandroid.unity.metadata.base;

import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.utils.HexUtil;

import java.io.IOException;
import java.io.InputStream;

public class MDByteArray extends MDBlockItem implements JsonData {

    private IntegerReference sizeReference;

    public MDByteArray(IntegerReference sizeReference) {
        super(sizeReference.get());
        this.sizeReference = sizeReference;
    }
    public MDByteArray(int bytesLength) {
        super(bytesLength);
    }

    public final void putByteArray(int offset, byte[] bytes) {
        byte[] bytesInternal = getBytesInternal();
        int available = bytesInternal.length - offset;
        if (available <= 0) {
            return;
        }
        int length = bytes.length;
        if (length > available) {
            length = available;
        }
        System.arraycopy(bytes, 0, bytesInternal, offset, length);
    }
    public byte[] getByteArray(int offset, int length) {
        byte[] bytes = getBytesInternal();
        byte[] result = new byte[length];
        if (length != 0) {
            System.arraycopy(bytes, offset, result, 0, result.length);
        }
        return result;
    }
    public IntegerReference getSizeReference() {
        return sizeReference;
    }
    public int size() {
        return countBytes();
    }
    public void setSize(int size) {
        setBytesLength(size, true);
        IntegerReference reference = getSizeReference();
        if (reference != null) {
            reference.set(size);
        }
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        IntegerReference reference = getSizeReference();
        if (reference != null) {
            setSize(reference.get());
        }
        super.onReadBytes(reader);
    }
    @Override
    public int readBytes(InputStream inputStream) throws IOException {
        IntegerReference reference = getSizeReference();
        if (reference != null) {
            setSize(reference.get());
        }
        return super.readBytes(inputStream);
    }

    @Override
    public Object getJson() {
        return HexUtil.toHexString(getBytes());
    }

    @Override
    public void setJson(Object obj) {
        throw new RuntimeException("Method not implemented");
    }
    @Override
    public String toString() {
        int size = size();
        if (size < 200) {
            return HexUtil.toHexString(getBytesInternal());
        } else {
            return "size=" + size;
        }
    }
}
