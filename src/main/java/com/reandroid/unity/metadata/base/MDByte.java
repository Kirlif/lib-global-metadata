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
import com.reandroid.utils.HexUtil;

public class MDByte extends MDBlockItem implements IntegerReference {

    public MDByte() {
        super(1);
    }

    public byte getByte() {
        return getBytesInternal()[0];
    }
    @Override
    public int get() {
        return getBytesInternal()[0];
    }

    @Override
    public void set(int value) {
        if ((value & 0xff) != value) {
            if (value > 0 || value < Byte.MIN_VALUE) {
                throw new NumberFormatException("Byte value out of range: "
                        + HexUtil.toHex(value, 2));
            }
        }
        set((byte) value);
    }
    public void set(byte value) {
        getBytesInternal()[0] = value;
    }

    public String getHexString() {
        return HexUtil.toHex(get() & 0xff, 2);
    }
    @Override
    public String toString() {
        return Integer.toString(get());
    }
}
