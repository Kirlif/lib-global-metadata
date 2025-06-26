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

import com.reandroid.arsc.item.StringBlock;
import com.reandroid.arsc.item.StringReference;
import com.reandroid.unity.metadata.section.SectionStringData;
import com.reandroid.unity.metadata.spec.StringSpec;

public abstract class MDString extends SectionData implements StringReference {

    private final StringBytes stringBytes;

    public MDString() {
        super(1);
        this.stringBytes = new StringBytes(this);
        addChild(0, stringBytes);
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
        SectionStringData<?> parentSection = getParentSection();
        if (parentSection != null) {
            parentSection.onStringChanged(old, text);
        }
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
