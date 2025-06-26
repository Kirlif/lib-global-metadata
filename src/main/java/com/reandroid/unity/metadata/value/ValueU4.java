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
import com.reandroid.unity.metadata.base.MDCompressedUInt32;
import com.reandroid.unity.metadata.spec.PrimitiveSpec;

import java.io.IOException;

public class ValueU4 extends MetadataValue implements IntegerReference {

    private final MDCompressedUInt32 value;

    public ValueU4() {
        super(1, Il2CppTypeEnum.U4);
        this.value = new MDCompressedUInt32();

        addChild(START_INDEX + 0, value);
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        super.onReadBytes(reader);
    }

    @Override
    public int get() {
        return value.get();
    }

    @Override
    public PrimitiveSpec.U4Spec getSpec() {
        return new PrimitiveSpec.U4Spec(get());
    }

    @Override
    public void set(int value) {
        this.value.set(value);
    }

    @Override
    public String toString() {
        return Integer.toString(get());
    }
}
