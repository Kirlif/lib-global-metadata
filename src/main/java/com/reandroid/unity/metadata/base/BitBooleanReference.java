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
package com.reandroid.unity.metadata.base;

import com.reandroid.arsc.item.BooleanReference;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.utils.NumbersUtil;

public class BitBooleanReference implements BooleanReference {

    private final IntegerReference reference;
    private final int start;

    public BitBooleanReference(IntegerReference reference, int start) {
        this.reference = reference;
        this.start = start;
    }

    @Override
    public boolean get() {
        return NumbersUtil.getUInt(reference.get(), getStart(), 1) == 1;
    }
    @Override
    public void set(boolean value) {
        reference.set(NumbersUtil.setUInt(reference.get(), value ? 1 : 0, getStart(), 1));
    }

    public int getStart() {
        return start;
    }

    @Override
    public String toString() {
        return Boolean.toString(get());
    }
}
