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

import com.reandroid.arsc.base.Creator;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.TypeSpec;
import com.reandroid.utils.ObjectsUtil;

public class TypeEntryData extends DefinitionEntryData<TypeDefinitionData> {

    public TypeEntryData() {
        super(MetadataSectionType.TYPE_DEFINITIONS);
    }

    public String getName() {
        TypeDefinitionData data = getData();
        if (data != null) {
            return data.getName();
        }
        return null;
    }
    public String getTypeName() {
        TypeDefinitionData data = getData();
        if (data != null) {
            return data.getTypeName();
        }
        return null;
    }

    @Override
    public TypeSpec getSpec() {
        return (TypeSpec) super.getSpec();
    }

    @Override
    public MetadataSectionType<TypeEntryData> getSectionType() {
        return ObjectsUtil.cast(super.getSectionType());
    }

    public static final Creator<TypeEntryData> CREATOR = TypeEntryData::new;
}
