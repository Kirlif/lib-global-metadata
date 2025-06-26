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
import com.reandroid.utils.HexUtil;
import com.reandroid.utils.ObjectsUtil;
import com.reandroid.utils.StringsUtil;

import java.io.IOException;
import java.io.InputStream;

public class MetadataFileMagic extends MDBlockItem implements IntegerReference {

    private boolean disableValidation;

    public MetadataFileMagic() {
        super(4);
        set(MAGIC);
    }


    @Override
    public int get() {
        return getInteger(getBytesInternal(), 0);
    }
    @Override
    public void set(int value) {
        putInteger(getBytesInternal(), 0, value);
    }
    public void setDisableValidation(boolean disableValidation) {
        this.disableValidation = disableValidation;
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        super.onReadBytes(reader);
        validateValue();
    }
    @Override
    public int readBytes(InputStream inputStream) throws IOException {
        int read = super.readBytes(inputStream);
        validateValue();
        return read;
    }

    private void validateValue() throws IOException {
        if (!disableValidation) {
            int value = get();
            if (value != MAGIC) {
                throw new IOException("Invalid magic: " +
                        StringsUtil.toUpperCase(HexUtil.toHex8(value)) + ", expecting: "
                        + StringsUtil.toUpperCase(HexUtil.toHex8(MAGIC)));
            }
        }
    }

    @Override
    public String toString() {
        return StringsUtil.toUpperCase(HexUtil.toHex8(get()));
    }

    public static final int MAGIC = ObjectsUtil.of(0XFAB11BAF);
}
