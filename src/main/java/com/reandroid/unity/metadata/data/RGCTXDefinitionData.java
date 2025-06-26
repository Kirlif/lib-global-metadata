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

import com.reandroid.arsc.item.LongReference;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.VersionRange;
import com.reandroid.unity.metadata.base.MDInt;
import com.reandroid.unity.metadata.base.MDULong;
import com.reandroid.unity.metadata.section.MetadataSectionType;

public class RGCTXDefinitionData extends SectionData {

    public final LongReference type;
    public final LongReference data;

    public RGCTXDefinitionData() {
        super(4);

        MDInt type_pre29 = new MDInt(new VersionRange(null, 27.1));
        MDULong type_post29 = new MDULong(new VersionRange(29.0, null));
        MDInt data_pre29 = new MDInt(new VersionRange(null, 27.1));
        MDULong data_post29 = new MDULong(new VersionRange(27.2, null));

        this.type = new IntegerOrLongItem(type_pre29, type_post29);
        this.data = new IntegerOrLongItem(data_pre29, data_post29);

        addChild(0, type_pre29);
        addChild(1, type_post29);
        addChild(2, data_pre29);
        addChild(3, data_post29);
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type.getLong());
        jsonObject.put("data", data.getLong());
        return jsonObject;
    }

    @Override
    public MetadataSectionType<RGCTXDefinitionData> getSectionType() {
        return MetadataSectionType.RGCTX_ENTRIES;
    }
    @Override
    public String toString() {
        return "type=" + type + ", data=" + data;
    }

    static class IntegerOrLongItem implements LongReference {

        private final MDInt intValue;
        private final MDULong longValue;

        public IntegerOrLongItem(MDInt intValue, MDULong longValue) {
            this.intValue = intValue;
            this.longValue = longValue;
        }

        @Override
        public int get() {
            if (intValue.hasValidVersion()) {
                return intValue.get();
            }
            return longValue.get();
        }
        @Override
        public void set(int value) {
            if (intValue.hasValidVersion()) {
                intValue.set(value);
            } else {
                longValue.set(value);
            }
        }
        @Override
        public long getLong() {
            if (intValue.hasValidVersion()) {
                return intValue.get();
            }
            return longValue.getLong();
        }
        @Override
        public void set(long value) {
            if (intValue.hasValidVersion()) {
                intValue.set((int) value);
            } else {
                longValue.set(value);
            }
        }

        @Override
        public String toString() {
            return Long.toString(get());
        }
    }
}
