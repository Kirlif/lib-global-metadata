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
package com.reandroid.unity.metadata.index;

import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.unity.metadata.base.VersionRange;
import com.reandroid.unity.metadata.data.TypeDefinitionData;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.SpecList;
import com.reandroid.unity.metadata.spec.TypeSpec;
import com.reandroid.utils.ObjectsUtil;


public class TypeDefinitionIndexList extends DefinitionIndexList<TypeDefinitionData> {

    public TypeDefinitionIndexList(VersionRange versionRange) {
        super(MetadataSectionType.TYPE_DEFINITIONS, versionRange);
    }
    public TypeDefinitionIndexList() {
        this(null);
    }
    public TypeDefinitionIndexList(IntegerReference start, IntegerReference count) {
        super(0, MetadataSectionType.TYPE_DEFINITIONS, null, start, count);
    }

    @Override
    public SpecList<TypeSpec> getSpec() {
        return ObjectsUtil.cast(super.getSpec());
    }

    @Override
    protected String simpleToString(TypeDefinitionData item) {
        if (item == null) {
            return "null";
        }
        return item.getName();
    }
}
