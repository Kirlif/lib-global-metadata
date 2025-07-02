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

import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.arsc.item.StringReference;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.MDBlockItem;
import com.reandroid.unity.metadata.base.MetadataInteger;
import com.reandroid.unity.metadata.spec.StringSpec;
import com.reandroid.utils.ObjectsUtil;
import com.reandroid.utils.StringsUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ValueString extends MetadataValue implements StringReference {

    private final ValueStringBlock valueStringBlock;

    public ValueString() {
        super(2, Il2CppTypeEnum.STRING);
        MetadataInteger length = new MetadataInteger(true);
        this.valueStringBlock = new ValueStringBlock(length);

        addChild(START_INDEX + 0, length);
        addChild(START_INDEX + 1, valueStringBlock);

    }

    @Override
    public String get() {
        return valueStringBlock.get();
    }
    @Override
    public void set(String str) {
        valueStringBlock.set(str);
    }

    @Override
    public String value() {
        return get();
    }

    @Override
    public StringSpec getSpec() {
        return StringSpec.of(get());
    }

    @Override
    public Object getJsonValue() {
        String value = get();
        if (value == null) {
            return JSONObject.NULL;
        }
        return value;
    }

    @Override
    public String toString() {
        return "\""+ get() + "\"";
    }

    public static class ValueStringBlock extends MDBlockItem {

        private final IntegerReference lengthReference;
        private String mCache;

        public ValueStringBlock(IntegerReference lengthReference) {
            super(0);
            this.lengthReference = lengthReference;
            mCache = StringsUtil.EMPTY;
        }

        public String get() {
            return mCache;
        }
        public void set(String text) {
            if (ObjectsUtil.equals(mCache, text)) {
                return;
            }
            mCache = text;
            if (text == null) {
                lengthReference.set(-1);
                setBytesLength(0, false);
            } else {
                byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
                int length = textBytes.length;
                setBytesLength(length, false);
                lengthReference.set(length);
                byte[] bytes = getBytesInternal();
                if (length != 0) {
                    System.arraycopy(textBytes, 0, bytes, 0, length);
                }
            }
            MetadataValue parent = getParentInstance(MetadataValue.class);
            if (parent != null) {
                parent.onDataChanged();
            }
        }

        @Override
        public void onReadBytes(BlockReader reader) throws IOException {
            this.readBytes((InputStream) reader);
        }

        @Override
        public int readBytes(InputStream inputStream) throws IOException {
            int length = lengthReference.get();
            if (length < 0) {
                mCache = null;
                length = 0;
                setBytesLength(length, false);
            } else {
                setBytesLength(length, false);
                byte[] bytes = getBytesInternal();
                int verify = inputStream.read(bytes, 0, length);
                if (verify != length) {
                    throw new IOException("Failed to read full bytes");
                }
                mCache = new String(bytes, 0, length, StandardCharsets.UTF_8);
            }
            return length;
        }

        @Override
        public String toString() {
            return String.valueOf(get());
        }
    }
    
}
