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

import com.reandroid.arsc.container.BlockList;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.BooleanReference;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.unity.metadata.base.JsonData;
import com.reandroid.unity.metadata.base.LinkableItem;
import com.reandroid.unity.metadata.base.MDBlockItem;
import com.reandroid.unity.metadata.base.MDCompressedSInt32;
import com.reandroid.unity.metadata.spec.SpecList;
import com.reandroid.utils.StringsUtil;
import com.reandroid.utils.collection.ComputeIterator;

import java.io.IOException;
import java.util.Iterator;

public class ValueSZArray extends MetadataValue implements LinkableItem {

    private final MDCompressedSInt32 arrayLength;
    private final DifferentArrayBoolean arrayElementsAreDifferent;
    private final Il2CppTypeEnumBlock arrayElementType;
    private final BlockList<MetadataValue> elementList;

    public ValueSZArray() {
        super(4, Il2CppTypeEnum.SZARRAY);
        this.arrayLength = new MDCompressedSInt32();
        this.arrayElementsAreDifferent = new DifferentArrayBoolean(arrayLength);
        this.arrayElementType = new Il2CppTypeEnumBlock();
        this.elementList = new SZArrayElementList(arrayLength, arrayElementsAreDifferent, arrayElementType);

        addChild(START_INDEX + 0, arrayLength);
        addChild(START_INDEX + 1, arrayElementType);
        addChild(START_INDEX + 2, arrayElementsAreDifferent);
        addChild(START_INDEX + 3, elementList);
    }

    public Iterator<MetadataValue> iterator() {
        return elementList.iterator();
    }
    @Override
    public void link() {
        LinkableItem.linkAll(elementList.iterator());
    }

    @Override
    public SpecList<?> getSpec() {
        return SpecList.collect(ComputeIterator.of(iterator(), MetadataValue::getSpec));
    }

    @Override
    public Object getJsonValue() {
        return JsonData.toJsonArray(elementList);
    }
    @Override
    public String toString() {
        return "ValueSZArray{" +
                "arrayLength=" + arrayLength +
                ", arrayElementsAreDifferent=" + arrayElementsAreDifferent +
                ", arrayElementType=" + arrayElementType +
                ", elementList=[" + StringsUtil.join(elementList.iterator(), ", ") + "]" +
                '}';
    }

    static class DifferentArrayBoolean extends MDBlockItem implements BooleanReference {

        private final IntegerReference countReference;

        DifferentArrayBoolean(IntegerReference countReference) {
            super(1);
            this.countReference = countReference;
        }

        @Override
        public boolean isNull() {
            return super.isNull() || countReference.get() < 0;
        }

        @Override
        public boolean get() {
            return getBytesInternal()[0] == 1;
        }

        @Override
        public void set(boolean value) {
            getBytesInternal()[0] = (byte) (value ? 1 : 0);
        }

        @Override
        public String toString() {
            return Boolean.toString(get());
        }
    }

    static class SZArrayElementList extends BlockList<MetadataValue> {

        private final IntegerReference countReference;
        private final BooleanReference arrayElementsAreDifferent;
        private final Il2CppTypeEnumBlock arrayElementType;

        SZArrayElementList(IntegerReference countReference, BooleanReference arrayElementsAreDifferent,
                           Il2CppTypeEnumBlock arrayElementType) {
            super();
            this.countReference = countReference;
            this.arrayElementsAreDifferent = arrayElementsAreDifferent;
            this.arrayElementType = arrayElementType;
        }

        @Override
        public boolean isNull() {
            return super.isNull() || countReference.get() < 0;
        }
        @Override
        protected void onReadBytes(BlockReader reader) throws IOException {
            if (isNull()) {
                return;
            }
            int count = countReference.get();
            boolean different = arrayElementsAreDifferent.get();
            for (int i = 0; i < count; i++) {
                MetadataValue value;
                if (different) {
                    value = MetadataValueFactory.read(reader);
                } else {
                    value = MetadataValueFactory.forType(arrayElementType.type());
                    value.clearEnumBlock();
                }
                add(value);
                value.readBytes(reader);
            }
        }
    }
}
