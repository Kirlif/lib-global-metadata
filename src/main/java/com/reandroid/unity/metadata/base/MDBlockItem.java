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

import com.reandroid.arsc.base.DirectStreamReader;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.BlockItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MDBlockItem extends BlockItem implements DirectStreamReader, JsonData {

    public MDBlockItem(int bytesLength) {
        super(bytesLength);
    }

    @Override
    public Object getJson() {
        return null;
    }
    @Override
    public void setJson(Object obj) {

    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        int length = countBytes();
        if (length == 0) {
            return;
        }
        if (reader.available() < length) {
            throw new IOException("Too few bytes to read " + length + " bytes, " + reader);
        }
        super.onReadBytes(reader);
    }

    @Override
    public int readBytes(InputStream inputStream) throws IOException {
        int length = countBytes();
        if (length == 0) {
            return 0;
        }
        int read = super.readBytes(inputStream);
        if (read < length) {
            throw new IOException("Too few bytes to read " + length + " bytes");
        }
        return read;
    }

    @Override
    protected int onWriteBytes(OutputStream stream) throws IOException {
        if (isNull()) {
            return 0;
        }
        return super.onWriteBytes(stream);
    }
}
