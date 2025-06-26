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
import com.reandroid.unity.metadata.data.BlobValueData;
import com.reandroid.unity.metadata.section.MetadataSection;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.section.SecFieldAndParameterDefaultValueData;
import com.reandroid.unity.metadata.spec.Spec;
import com.reandroid.unity.metadata.spec.SpecPair;
import com.reandroid.unity.metadata.spec.TypeSpec;
import com.reandroid.unity.metadata.value.MetadataValue;
import com.reandroid.utils.ObjectsUtil;

public class DefaultValueDataIndex extends DefinitionIndex<BlobValueData> {

    private final TypeDefinitionIndex typeIndex;

    public DefaultValueDataIndex(VersionRange versionRange, TypeDefinitionIndex typeIndex) {
        super(MetadataSectionType.FIELD_AND_PARAMETER_DEFAULT_VALUE_DATA, versionRange);
        this.typeIndex = typeIndex;
    }
    public DefaultValueDataIndex(TypeDefinitionIndex typeIndex) {
        this(null, typeIndex);
    }

    @Override
    public SpecPair<TypeSpec, Spec> getSpec() {
        return ObjectsUtil.cast(super.getSpec());
    }

    @Override
    public void link() {
        super.link();
        typeIndex.link();
    }

    @Override
    public Object getJson() {
        MetadataValue value = getValue();
        if (value != null) {
            return value.getJson();
        }
        return null;
    }

    public String getType() {
        return typeIndex.getName();
    }
    public String getTypeName() {
        return typeIndex.getTypeName();
    }

    public MetadataValue getValue() {
        BlobValueData data = getData();
        if (data != null) {
            return data.getValue();
        }
        return null;
    }

    @Override
    public int idxOf(BlobValueData data) {
        return data.getOffset();
    }

    @Override
    public boolean enableUpdate() {
        return true;
    }

    @Override
    public BlobValueData pullData(MetadataSection<BlobValueData> section, int idx) {
        SecFieldAndParameterDefaultValueData valueData = (SecFieldAndParameterDefaultValueData) getSection();
        return valueData.getData(typeIndex, idx);
    }

    @Override
    public String toString() {
        return getValue() + ": " + super.toString();
    }
}
