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

import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.utils.NumbersUtil;

public class BitIntegerReference implements IntegerReference {

    private final IntegerReference reference;
    private final int start;
    private final int length;

    public BitIntegerReference(IntegerReference reference, int start, int length) {
        this.reference = reference;
        this.start = start;
        this.length = length;
    }
    @Override
    public int get() {
        return NumbersUtil.getUInt(reference.get(), getStart(), getLength());
    }

    @Override
    public void set(int value) {
        reference.set(NumbersUtil.setUInt(reference.get(), value, getStart(), getLength()));
    }

    public int getStart() {
        return start;
    }
    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return Integer.toString(get());
    }
}
