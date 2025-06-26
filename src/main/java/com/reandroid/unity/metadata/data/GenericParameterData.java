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
import com.reandroid.unity.metadata.base.MDShort;
import com.reandroid.unity.metadata.base.MDUShort;
import com.reandroid.unity.metadata.index.CodeStringIndex;
import com.reandroid.unity.metadata.index.DefinitionIndex;
import com.reandroid.unity.metadata.index.DefinitionIndexList;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.*;
import com.reandroid.utils.ObjectsUtil;

public class GenericParameterData extends SectionData {

    private final DefinitionIndex<GenericContainerData> containerIndex;
    private final CodeStringIndex nameIndex;

    // index of this in GenericContainerData#parameterList
    private final MDUShort num;
    private final MDUShort flags;

    private final DefinitionIndexList<TypeEntryData> constraintList;

    public GenericParameterData() {
        super(7);

        this.containerIndex = new DefinitionIndex<>(MetadataSectionType.GENERIC_CONTAINERS);
        this.nameIndex = new CodeStringIndex();
        MDShort start = new MDShort();
        MDShort count = new MDShort();
        this.num = new MDUShort();
        this.flags = new MDUShort();

        this.constraintList = new DefinitionIndexList<>(MetadataSectionType.GENERIC_PARAMETER_CONSTRAINTS,
                start, count);

        addChild(0, containerIndex);
        addChild(1, nameIndex);
        addChild(2, start);
        addChild(3, count);
        addChild(4, num);
        addChild(5, flags);

        // blank block
        addChild(6, constraintList);
    }

    public String getName() {
        return nameIndex.getString();
    }
    public GenericContainerData getContainer() {
        return containerIndex.getData();
    }

    @Override
    public GenericParameterSpec getSpec() {
        SpecList<TypeSpec> specList = ObjectsUtil.cast(constraintList.getSpec());
        return GenericParameterSpec.of(flags.get(), nameIndex.getSpec(), specList);
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("index", getIdx());
        jsonObject.put("name_index", nameIndex.get());
        jsonObject.put("container_index", containerIndex.get());
        jsonObject.put("container", containerIndex.getData().getOwnerData().getSpec());
        jsonObject.put("name", nameIndex.getJson());
        jsonObject.put("constraints", constraintList.getJson());
        jsonObject.put("num", num.getJson());
        if (flags.get() != 0) {
            jsonObject.put("flags", flags.getJson());
        }
        return jsonObject;
    }

    @Override
    public MetadataSectionType<GenericParameterData> getSectionType() {
        return MetadataSectionType.GENERIC_PARAMETERS;
    }
    @Override
    public String toString() {
        return "ownerIndex=" + containerIndex
                + ", nameIndex=" + nameIndex
                + ", constraintList=" + constraintList
                + ", num=" + num
                + ", flags=" + flags;
    }
}
