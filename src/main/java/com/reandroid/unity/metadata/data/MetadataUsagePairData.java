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

import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.MDUInt;

public class MetadataUsagePairData extends SectionData {

    public final MDUInt destinationIndex;
    public final MDUInt encodedSourceIndex;

    public MetadataUsagePairData() {
        super(2);

        this.destinationIndex = new MDUInt();
        this.encodedSourceIndex = new MDUInt();

        addChild(0, destinationIndex);
        addChild(1, encodedSourceIndex);
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("destination", destinationIndex.getJson());
        jsonObject.put("encoded_source", encodedSourceIndex.getJson());
        return jsonObject;
    }

    @Override
    public String toString() {
        return "destinationIndex=" + destinationIndex
                + ", encodedSourceIndex=" + encodedSourceIndex;
    }
}
