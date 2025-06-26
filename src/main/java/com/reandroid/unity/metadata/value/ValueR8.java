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

import com.reandroid.unity.metadata.base.MDULong;
import com.reandroid.unity.metadata.spec.PrimitiveSpec;

public class ValueR8 extends MetadataValue {

    private final MDULong value;

    public ValueR8() {
        super(1, Il2CppTypeEnum.R8);
        this.value = new MDULong();
        addChild(START_INDEX + 0, value);
    }
    public double get() {
        return Double.longBitsToDouble(value.getLong());
    }
    public void set(double value) {
        this.value.set(Double.doubleToLongBits(value));
    }
    @Override
    public PrimitiveSpec.R8Spec getSpec() {
        return new PrimitiveSpec.R8Spec(get());
    }

    @Override
    public Object getJsonValue() {
        return value.getLong();
    }

    @Override
    public String toString() {
        return Double.toString(get());
    }
    
}
