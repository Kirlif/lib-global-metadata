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

import com.reandroid.arsc.base.Block;
import com.reandroid.arsc.base.Creator;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.header.MetadataSectionHeader;
import com.reandroid.unity.metadata.base.MDByteArray;
import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.utils.HexUtil;
import com.reandroid.utils.NumbersUtil;

public class SectionUnknown extends MetadataSection<SectionUnknown.Data> {

    public SectionUnknown(MetadataSectionHeader offsetSize) {
        super(newCreator(offsetSize.getSizeReference()), offsetSize);
    }

    @Override
    int sizeOfEntry() {
        return 0;
    }

    public static class Data extends SectionData {

        public final MDByteArray byteArray;

        public Data(IntegerReference sizeReference) {
            super(1);
            this.byteArray = new MDByteArray(sizeReference);
            addChild(0, byteArray);
        }

        @Override
        public JSONObject toJson() {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bytes", HexUtil.toHexString(byteArray.getBytes()));
            return jsonObject;
        }
        @Override
        public String toString() {
            byte[] bytes = byteArray.getBytes();
            int count = NumbersUtil.min(bytes.length / 4, 50);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < count; i++) {
                int val = Block.getInteger(bytes, i * 4);
                builder.append(HexUtil.toHex8(val)).append(", ");
            }

            return builder.toString();
        }
    }
    private static  Creator<Data> newCreator(IntegerReference sizeReference) {
        return () -> new Data(sizeReference);
    }

}
