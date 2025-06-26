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

import com.reandroid.arsc.base.Block;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.utils.HexUtil;

public class MDShort extends VersionSpecificItem implements IntegerReference {

    public MDShort(VersionRange versionRange) {
        super(versionRange,2);
    }
    public MDShort() {
        this(null);
    }

    @Override
    public Object getJson() {
        return get();
    }
    @Override
    public void setJson(Object obj) {
        Number number = (Number) obj;
        set(number.intValue());
    }

    public short getShort() {
        return Block.getShort(getBytesInternal(), 0);
    }
    public void set(short value) {
        Block.putShort(getBytesInternal(), 0, value);
    }
    @Override
    public int get() {
        return getShort();
    }

    @Override
    public void set(int value) {
        if ((value & 0xffff) != value) {
            if (value > 0 || value < Short.MIN_VALUE) {
                throw new NumberFormatException("Short value out of range: "
                        + HexUtil.toHex(value, 4));
            }
        }
        set((short) value);
    }
    public String getHexString() {
        return HexUtil.toHex(get(), 4);
    }

    @Override
    public String toString() {
        return Integer.toString(get());
    }
}
