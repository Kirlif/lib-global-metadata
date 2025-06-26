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
package com.reandroid.unity.metadata.value;

import com.reandroid.arsc.base.BlockCounter;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.arsc.item.NumberIntegerReference;
import com.reandroid.json.JSONArray;
import com.reandroid.unity.metadata.base.MDByteArray;
import com.reandroid.unity.metadata.spec.BlobSpec;
import com.reandroid.utils.HexUtil;

import java.io.IOException;
import java.io.OutputStream;

public class ValueUnknown extends MetadataValue {

    private final MDByteArray byteArray;

    public ValueUnknown() {
        super(1, Il2CppTypeEnum.UNKNOWN);
        this.byteArray = new MDByteArray(new NumberIntegerReference());

        addChild(0, null);
        addChild(1, byteArray);
    }

    @Override
    public byte[] getBytes() {
        return getByteArray().getBytes();
    }
    public int size() {
        return getByteArray().size();
    }
    public void setSize(int size) {
        getByteArray().setSize(size);
    }
    public IntegerReference getSizeReference() {
        return getByteArray().getSizeReference();
    }
    public MDByteArray getByteArray() {
        return byteArray;
    }

    @Override
    public int countBytes() {
        if (isNull()) {
            return 0;
        }
        return size();
    }

    @Override
    public void onCountUpTo(BlockCounter counter) {
        getByteArray().onCountUpTo(counter);
    }
    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        getByteArray().onReadBytes(reader);
    }
    @Override
    public int onWriteBytes(OutputStream stream) throws IOException {
        return getByteArray().writeBytes(stream);
    }

    @Override
    public BlobSpec getSpec() {
        return BlobSpec.of(getBytes());
    }

    @Override
    public Object getJsonValue() {
        byte[] bytes = getBytes();
        int length = bytes.length;
        JSONArray jsonArray = new JSONArray(length);
        for (int i = 0; i < length; i++) {
            jsonArray.put(bytes[i] & 0xff);
        }
        return jsonArray;
    }

    @Override
    public String toString() {
        MDByteArray byteArray = getByteArray();
        return byteArray.size() + " [" + HexUtil.toHexString(byteArray.getBytes()) + "]";
    }
}
