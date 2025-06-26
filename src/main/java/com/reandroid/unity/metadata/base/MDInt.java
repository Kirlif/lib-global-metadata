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

public class MDInt extends VersionSpecificItem implements IntegerReference {

    public MDInt(int bytesLength, VersionRange versionRange) {
        super(versionRange, bytesLength);
    }
    public MDInt(VersionRange versionRange) {
        super(versionRange, 4);
    }
    public MDInt() {
        this(null);
    }

    @Override
    public int get() {
        return Block.getInteger(getBytesInternal(), 0);
    }

    @Override
    public void set(int value) {
        Block.putInteger(getBytesInternal(), 0, value);
    }

    @Override
    public Object getJson() {
        if (isNull()) {
            return null;
        }
        return get();
    }
    @Override
    public void setJson(Object obj) {
        set((Integer) obj);
    }

    @Override
    public String toString() {
        if (isNull()) {
            return "NULL";
        }
        return Integer.toString(get());
    }
}
