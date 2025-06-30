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

import com.reandroid.arsc.item.LongReference;
import com.reandroid.unity.metadata.base.MetadataInteger;
import com.reandroid.unity.metadata.spec.PrimitiveSpec;

public class ValueU4 extends MetadataValue implements LongReference {

    private final MetadataInteger value;

    public ValueU4() {
        super(1, Il2CppTypeEnum.U4);
        this.value = new MetadataInteger(false);

        addChild(START_INDEX + 0, value);
    }

    @Override
    public int get() {
        return value.get();
    }

    @Override
    public long getLong() {
        return value.getLong();
    }

    @Override
    public void set(long l) {
        value.set(l);
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
        return Long.toString(getLong());
    }
}
