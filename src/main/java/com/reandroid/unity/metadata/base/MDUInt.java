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

public class MDUInt extends MDInt implements LongReference {

    public MDUInt(VersionRange versionRange) {
        super(versionRange);
    }
    public MDUInt() {
        this(null);
    }

    @Override
    public long getLong() {
        return get() & 0xffffffffL;
    }
    @Override
    public void set(long value) {
        set((int) value);
    }
    @Override
    public Object getJson() {
        if (!hasValidVersion()) {
            return null;
        }
        return getLong();
    }
    public void setJson(Object obj) {
        set(((Number)obj).longValue());
    }
    @Override
    public String toString() {
        return Long.toString(getLong());
    }
}
