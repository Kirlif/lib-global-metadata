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

import com.reandroid.arsc.item.LongReference;
import com.reandroid.utils.HexUtil;

public class MDLong extends VersionSpecificItem implements LongReference {

    public MDLong(VersionRange versionRange) {
        super(versionRange, 8);
    }
    public MDLong() {
        this(null);
    }

    @Override
    public int get() {
        return (int) getLong();
    }
    @Override
    public void set(int value) {
        set(value & 0xffffffffL);
    }
    @Override
    public long getLong() {
        return getLong(getBytesInternal(), 0);
    }
    @Override
    public void set(long value) {
        putLong(getBytesInternal(), 0, value);
    }

    @Override
    public Object getJson() {
        return getLong();
    }

    public String getHexString() {
        return HexUtil.toHex(get(), 4);
    }
    @Override
    public String toString() {
        return getLong() + "L";
    }
}
