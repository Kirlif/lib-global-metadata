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

import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.unity.metadata.base.MDUShort;
import com.reandroid.unity.metadata.spec.PrimitiveSpec;

public class ValueU2 extends MetadataValue implements IntegerReference {

    private final MDUShort value;

    public ValueU2() {
        super(1, Il2CppTypeEnum.U2);
        this.value = new MDUShort();
        addChild(START_INDEX + 0, value);
    }

    @Override
    public int get() {
        return value.get();
    }

    @Override
    public void set(int value) {
        this.value.set(value);
    }

    @Override
    public Integer value() {
        return get();
    }

    @Override
    public PrimitiveSpec.U2Spec getSpec() {
        return new PrimitiveSpec.U2Spec(get());
    }

    @Override
    public String toString() {
        return value.toString();
    }
    
}
