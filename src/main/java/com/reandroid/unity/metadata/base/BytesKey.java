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
import com.reandroid.unity.metadata.util.CommonUtil;
import com.reandroid.utils.HexUtil;

public class BytesKey implements Comparable<BytesKey> {

    private final byte[] bytes;

    public BytesKey(byte[] bytes) {
        this.bytes = bytes;
    }

    public final byte[] getBytes() {
        return bytes;
    }

    @Override
    public int compareTo(BytesKey bytesKey) {
        if (bytesKey == this) {
            return 0;
        }
        return CommonUtil.compareBytes(getBytes(), bytesKey.getBytes());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BytesKey bytesKey = (BytesKey) obj;
        return Block.areEqual(bytes, bytesKey.bytes);
    }

    @Override
    public int hashCode() {
        return CommonUtil.hash(bytes);
    }

    @Override
    public String toString() {
        return HexUtil.toHexString(getBytes());
    }

}
