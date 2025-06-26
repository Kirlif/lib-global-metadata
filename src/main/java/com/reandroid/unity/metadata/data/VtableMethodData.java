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

import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.BitIntegerReference;
import com.reandroid.unity.metadata.base.MDUInt;
import com.reandroid.unity.metadata.index.DefinitionIndex;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.section.MetadataUsage;
import com.reandroid.unity.metadata.spec.Spec;

public class VtableMethodData extends SectionData {

    private final IntegerReference usage;
    private final DefinitionIndex<?> dataIndex;

    public VtableMethodData() {
        super(2);
        MDUInt encodedValue = new MDUInt();

        BitIntegerReference dataReference = new BitIntegerReference(encodedValue, 28, 0) {
            @Override
            public int getLength() {
                if (VtableMethodData.this.getGlobalMetadataVersion() >= 27.0) {
                    return 28;
                }
                return 29;
            }
        };

        IntegerReference usageReference = new BitIntegerReference(encodedValue, 31, 3);

        this.dataIndex = new DefinitionIndex<SectionData>(null, null, dataReference) {
            @Override
            public MetadataSectionType<?> getSectionType() {
                return MetadataUsage.getSectionTypeForUsage(usageReference.get());
            }
        };
        this.usage = usageReference;

        addChild(0, encodedValue);
        // blank block
        addChild(1, dataIndex);
    }

    public DefinitionIndex<?> getDataIndex() {
        return dataIndex;
    }
    public MetadataUsage getUsage() {
        return MetadataUsage.valueOf(usage().get());
    }
    public IntegerReference usage() {
        return usage;
    }

    @Override
    public Spec getSpec() {
        return getDataIndex().getSpec();
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("usage", usage().get());
        jsonObject.put("data", getDataIndex().getJson());
        return jsonObject;
    }

    @Override
    public MetadataSectionType<VtableMethodData> getSectionType() {
        return MetadataSectionType.VTABLE_METHODS;
    }
    @Override
    public String toString() {
        return getUsage() + "(" + usage() + ") {" + getDataIndex() + "}";
    }
}
