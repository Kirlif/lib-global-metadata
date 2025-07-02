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

import com.reandroid.unity.metadata.base.MDUByte;
import com.reandroid.unity.metadata.spec.PrimitiveSpec;

public class ValueBoolean extends MetadataValue {

    private final MDUByte byteBlock;

    public ValueBoolean() {
        super(1, Il2CppTypeEnum.BOOLEAN);
        this.byteBlock = new MDUByte();
        addChild(START_INDEX + 0, byteBlock);
    }
    public boolean get() {
        return byteBlock.get() != 0;
    }
    public void set(boolean value) {
        byteBlock.set(value ? 0xff : 0);
    }

    @Override
    public Boolean value() {
        return get();
    }

    @Override
    public PrimitiveSpec.BooleanSpec getSpec() {
        if (get()) {
            return PrimitiveSpec.BooleanSpec.TRUE;
        }
        return PrimitiveSpec.BooleanSpec.FALSE;
    }

    @Override
    public String toString() {
        return Boolean.toString(get());
    }
}
