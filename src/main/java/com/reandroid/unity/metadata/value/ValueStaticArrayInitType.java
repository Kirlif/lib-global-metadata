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

import com.reandroid.arsc.base.Block;
import com.reandroid.arsc.base.BlockCounter;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.json.JSONArray;
import com.reandroid.unity.metadata.base.MDByteArray;
import com.reandroid.unity.metadata.data.TypeDefinitionData;
import com.reandroid.unity.metadata.spec.BlobSpec;
import com.reandroid.unity.metadata.util.CommonUtil;
import com.reandroid.utils.HexUtil;

import java.io.IOException;
import java.io.OutputStream;

public class ValueStaticArrayInitType extends MetadataValue {

    private final MDByteArray byteArray;

    public ValueStaticArrayInitType() {
        super(1, Il2CppTypeEnum.StaticArrayInitType);
        IntegerReference sizeReference = new IntegerReference() {
            @Override
            public int get() {
                Integer size = Il2CppTypeEnum.getStaticArrayInitTypeSize(
                        ValueStaticArrayInitType.this.getTypeName());
                if (size != null) {
                    return size;
                }
                return 0;
            }
            @Override
            public void set(int i) {
            }
            @Override
            public String toString() {
                return Integer.toString(get());
            }
        };
        this.byteArray = new MDByteArray(sizeReference);

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

    public TypeDefinitionData getValueTypeDefinition() {
        ValueParent blobValueData = getParentInstance(ValueParent.class);
        if (blobValueData != null) {
            return blobValueData.getValueTypeDefinition();
        }
        return null;
    }
    public String getTypeName() {
        TypeDefinitionData data = getValueTypeDefinition();
        if (data != null) {
            return data.getTypeName();
        }
        return null;
    }
    public short[] shortArray() {
        byte[] bytes = getBytes();
        int length = bytes.length / 2;
        short[] result = new short[length];
        for (int i = 0; i < length; i++) {
            result[i] = Block.getShort(bytes, i * 2);
        }
        return result;
    }
    public int[] intArray() {
        byte[] bytes = getBytes();
        int length = bytes.length / 4;
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = Block.getInteger(bytes, i * 4);
        }
        return result;
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
        StringBuilder builder = new StringBuilder();
        int size = size();
        if ((size % 4) == 0) {
            int[] array = intArray();
            int length = array.length;
            builder.append("int ");
            builder.append(length);
            builder.append('[');
            builder.append(CommonUtil.joinArray(array));
            builder.append(']');
        } else if ((size % 2) == 0) {
            short[] array = shortArray();
            int length = array.length;
            builder.append("short ");
            builder.append(length);
            builder.append('[');
            builder.append(CommonUtil.joinArray(array));
            builder.append(']');
        } else {
            builder.append("byte ");
            builder.append(size);
            builder.append(" [");
            builder.append(HexUtil.toHexString(getBytes()));
            builder.append("]");
        }
        return builder.toString();
    }
}
