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
package com.reandroid.unity.metadata.data;

import com.reandroid.utils.ObjectsUtil;

public class DataKey {

    private final SectionData data;
    private int mHash;

    public DataKey(SectionData data) {
        this.data = data;
    }

    public SectionData data() {
        return data;
    }
    public Object getData() {
        return data();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DataKey)) {
            return false;
        }
        DataKey dataKey = (DataKey) obj;
        return ObjectsUtil.equals(getData(), dataKey.getData());
    }

    @Override
    public int hashCode() {
        int hash = this.mHash;
        if (hash == 0) {
            hash = computeHashCode();
            this.mHash = hash;
        }
        return hash;
    }
    public int computeHashCode() {
        return ObjectsUtil.hash(getData());
    }

    @Override
    public String toString() {
        return String.valueOf(getData());
    }
}
