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

import com.reandroid.unity.metadata.base.VersionRange;
import com.reandroid.unity.metadata.data.FieldDefinitionData;
import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.FieldSpec;

public class FieldDefinitionIndex extends DefinitionIndex<FieldDefinitionData> {

    public FieldDefinitionIndex(VersionRange versionRange) {
        super(MetadataSectionType.FIELDS, versionRange);
        set(SectionData.INVALID_IDX);
    }
    public FieldDefinitionIndex() {
        super(MetadataSectionType.FIELDS);
    }

    @Override
    public FieldSpec getSpec() {
        return (FieldSpec) super.getSpec();
    }

    public String getName() {
        FieldDefinitionData data = getData();
        if (data != null) {
            return data.getName();
        }
        return null;
    }
    public String getTypeName() {
        FieldDefinitionData data = getData();
        if (data != null) {
            return data.getTypeName();
        }
        return null;
    }

    @Override
    public String toString() {
        return get() + "{" + getName() + "}";
    }

}
