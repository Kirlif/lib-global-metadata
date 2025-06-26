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
package com.reandroid.unity.metadata.header;

import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.unity.metadata.base.MDBlockItem;

import java.io.IOException;

public class MetadataFileVersion extends MDBlockItem implements IntegerReference {

    private int mCache;

    public MetadataFileVersion() {
        super(4);
    }

    public double version() {
        return get();
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        super.onReadBytes(reader);
        int version = get();
        if ((version & 0xff) != version) {
            throw new IOException("Invalid version: " + version);
        }
    }

    @Override
    public int get() {
        return mCache;
    }
    @Override
    public void set(int value) {
        if (value != mCache) {
            mCache = value;
            putInteger(getBytesInternal(), 0, value);
        }
    }
    @Override
    protected void onBytesChanged() {
        mCache = getInteger(getBytesInternal(), 0);
    }
    @Override
    public Object getJson() {
        return get();
    }
    @Override
    public void setJson(Object obj) {
        set((Integer) obj);
    }
    @Override
    public boolean isNull() {
        return false;
    }
}
