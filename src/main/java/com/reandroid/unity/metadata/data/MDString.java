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

import com.reandroid.arsc.base.Block;
import com.reandroid.arsc.item.StringBlock;
import com.reandroid.arsc.item.StringReference;
import com.reandroid.unity.metadata.section.SectionStringData;
import com.reandroid.unity.metadata.spec.StringSpec;
import com.reandroid.utils.CompareUtil;
import com.reandroid.utils.NumbersUtil;
import com.reandroid.utils.ObjectsUtil;

public abstract class MDString extends SectionData
        implements StringReference, Comparable<MDString> {

    private final StringBytes stringBytes;

    public MDString() {
        super(1);
        this.stringBytes = new StringBytes(this);
        addChild(0, stringBytes);
    }

    @Override
    public String getKey() {
        return get();
    }
    @Override
    public StringSpec getSpec() {
        return StringSpec.of(get());
    }
    @Override
    public String get() {
        return stringBytes.get();
    }
    @Override
    public void set(String text) {
        stringBytes.set(text);
    }
    @Override
    public byte[] getBytes() {
        return stringBytes.getBytesInternal();
    }
    @Override
    public int countBytes() {
        return stringBytes.countBytes();
    }
    protected void setBytesLength(int length) {
        stringBytes.changeBytesLength(length);
    }
    protected void onBytesChanged() {
        stringBytes.onBytesChanged();
    }

    protected abstract String decodeString(byte[] bytes);
    protected abstract byte[] encodeString(String text);

    protected void onStringChanged(String old, String text) {
        SectionStringData<MDString> parentSection = ObjectsUtil.cast(getParentSection());
        if (parentSection != null) {
            parentSection.onKeyChanged(old, text, this);
        }
    }
    public boolean isOverlapping() {
        return false;
    }
    public MDString base() {
        return null;
    }

    public SectionStringData<?> getParentSection() {
        return getParentInstance(SectionStringData.class);
    }

    @Override
    public Object getJson() {
        return get();
    }
    @Override
    public void setJson(Object obj) {
        set((String) obj);
    }

    @Override
    public int computeUnitSize() {
        return 0;
    }

    public boolean equalsTo(MDString data) {
        if (data == null) {
            return false;
        }
        return data == this || Block.areEqual(this.getBytes(), data.getBytes());
    }
    public boolean endsWith(MDString data) {
        if (data == this) {
            return true;
        }
        byte[] bytes1 = this.getBytes();
        byte[] bytes2 = data.getBytes();
        int length1 = bytes1.length;
        int length2 = bytes2.length;
        if (length1 < length2) {
            return false;
        }
        int length = NumbersUtil.min(length1, length2);
        int i = 1;
        while (i <= length) {
            if (bytes1[length1 - i] != bytes2[length2 - i]) {
                return false;
            }
            i ++;
        }
        return true;
    }
    public int compareReversed(MDString data) {
        if (data == this) {
            return 0;
        }
        byte[] bytes1 = this.getBytes();
        byte[] bytes2 = data.getBytes();
        int length1 = bytes1.length;
        int length2 = bytes2.length;
        int length = NumbersUtil.min(length1, length2);
        int i = 1;
        while (i <= length) {
            int c = CompareUtil.compare(bytes1[length1 - i] & 0xff, bytes2[length2 - i] & 0xff);
            if (c != 0) {
                return c;
            }
            i ++;
        }
        return CompareUtil.compare(length1, length2);
    }
    @Override
    public int compareTo(MDString mdString) {
        if (mdString == this) {
            return 0;
        }
        int i = CompareUtil.compare(this.isOverlapping(), mdString.isOverlapping()) * -1;
        if (i == 0) {
            if (isOverlapping()) {
                i = base().compareTo(mdString.base());
            } else {
                i = CompareUtil.compareUnsigned(getIndex(), mdString.getIndex());
            }
        }
        return i;
    }
    @Override
    public String toString() {
        return get();
    }

    static class StringBytes extends StringBlock {

        private final MDString mdString;

        StringBytes(MDString mdString) {
            this.mdString = mdString;
        }
        @Override
        protected String decodeString(byte[] bytes) {
            return mdString.decodeString(bytes);
        }
        @Override
        protected byte[] encodeString(String text) {
            return mdString.encodeString(text);
        }
        @Override
        protected void onStringChanged(String old, String text) {
            super.onStringChanged(old, text);
            mdString.onStringChanged(old, text);
        }
        @Override
        public byte[] getBytesInternal() {
            return super.getBytesInternal();
        }
        void changeBytesLength(int length) {
            setBytesLength(length, false);
        }
        @Override
        protected void onBytesChanged() {
            super.onBytesChanged();
        }
    }

}
