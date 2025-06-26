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
package com.reandroid.unity.metadata.section;

import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.unity.metadata.base.MDByteArray;

public class BlobDataPool extends MDByteArray {

    private BlockReader blockReader;

    public BlobDataPool(IntegerReference sizeReference) {
        super(sizeReference);
    }

    @Override
    public void setSize(int size) {
        setBytesLength(size, true);
    }
    public void clear() {
        setSize(0);
    }

    @Override
    protected void onBytesChanged() {
        this.blockReader = null;
    }

    public BlockReader getBlockReader() {
        BlockReader reader = this.blockReader;
        if (reader == null) {
            reader = new BlockReader(getBytesInternal());
            this.blockReader = reader;
        }
        return reader;
    }
}
