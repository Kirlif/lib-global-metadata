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

import com.reandroid.unity.metadata.base.VersionRange;
import com.reandroid.unity.metadata.base.MDUInt;
import com.reandroid.unity.metadata.spec.PrimitiveSpec;
import com.reandroid.utils.HexUtil;

public class MetadataToken extends MDUInt {

    public MetadataToken(VersionRange versionRange) {
        super(versionRange);
    }
    public MetadataToken() {
        this(null);
    }

    @Override
    public void setJson(Object obj) {
        set(((Number)obj).longValue());
    }
    public PrimitiveSpec.U4Spec getSpec() {
        return new PrimitiveSpec.U4Spec(getLong());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        MetadataToken token = (MetadataToken) obj;
        return this.getLong() == token.getLong();
    }
    @Override
    public int hashCode() {
        int i = get();
        int high = ((i >> 24) & 0xff) << 23;
        i  = i & 0x00ffffff;
        return high | i;
    }
    @Override
    public String toString() {
        return HexUtil.toHex8(getLong());
    }
}
