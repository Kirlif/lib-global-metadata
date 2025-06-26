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
import com.reandroid.unity.metadata.base.MDBlockItem;
import com.reandroid.unity.metadata.spec.PrimitiveSpec;

import java.io.IOException;
import java.io.InputStream;

public class ValueChar extends MetadataValue {

    private final CharBlock charBlock;

    public ValueChar() {
        super(1, Il2CppTypeEnum.CHAR);
        this.charBlock = new CharBlock();

        addChild(START_INDEX + 0, charBlock);
    }

    public char get() {
        return charBlock.get();
    }
    public void set(char c) {
        charBlock.set(c);
    }
    @Override
    public PrimitiveSpec.CharSpec getSpec() {
        return new PrimitiveSpec.CharSpec(get());
    }
    @Override
    public Object getJsonValue() {
        return get();
    }
    @Override
    public String toString() {
        return "'" + get() + "'";
    }


    public static class CharBlock extends MDBlockItem {

        private char mChar;

        public CharBlock() {
            super(2);
        }

        public char get() {
            return mChar;
        }

        public void set(char c) {
            if (c != mChar) {
                mChar = c;
                fromChar(c, getBytesInternal());
            }
        }

        @Override
        public void onReadBytes(BlockReader reader) throws IOException {
            super.onReadBytes(reader);
            mChar = toChar(getBytesInternal());
        }

        @Override
        public int readBytes(InputStream inputStream) throws IOException {
            int length = super.readBytes(inputStream);
            mChar = toChar(getBytesInternal());
            return length;
        }

        @Override
        public String toString() {
            return Character.toString(get());
        }
    }

    public static char toChar(byte[] bytes) {
        return (char) ((bytes[0] & 0xFF) | (bytes[1] << 8));
    }
    public static void fromChar(char c, byte[] out) {
        out[0] = (byte) (c & 0xFF);
        out[1] = (byte) ((c >> 8) & 0xFF);
    }
}
