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
import com.reandroid.unity.metadata.index.DefinitionIndexList;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.SpecList;
import com.reandroid.unity.metadata.spec.TypeSpec;
import com.reandroid.utils.ObjectsUtil;

public class TypeRangeData extends SectionData {

    public final DefinitionIndexList<TypeEntryData> types;

    public TypeRangeData() {
        super(1);
        this.types = new DefinitionIndexList<>(MetadataSectionType.UNRESOLVED_VIRTUAL_CALL_PARAMETER_TYPES);

        addChild(0, types);
    }

    @Override
    public SpecList<TypeSpec> getSpec() {
        return ObjectsUtil.cast(types.getSpec());
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("types", types.getJson());
        return jsonObject;
    }

    @Override
    public String toString() {
        return types.toString();
    }
}
