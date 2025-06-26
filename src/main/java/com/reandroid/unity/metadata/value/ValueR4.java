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

import com.reandroid.unity.metadata.base.MDUInt;
import com.reandroid.unity.metadata.spec.PrimitiveSpec;

public class ValueR4 extends MetadataValue {

    private final MDUInt value;

    public ValueR4() {
        super(1, Il2CppTypeEnum.R4);
        this.value = new MDUInt();
        addChild(START_INDEX + 0, value);
    }
    public float get() {
        return Float.intBitsToFloat(value.get());
    }
    public void set(float value) {
        this.value.set(Float.floatToIntBits(value));
    }

    @Override
    public PrimitiveSpec.R4Spec getSpec() {
        return new PrimitiveSpec.R4Spec(get());
    }

    @Override
    public Object getJsonValue() {
        return value.get();
    }
    @Override
    public String toString() {
        return get() + "f";
    }
}
