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
import com.reandroid.unity.metadata.data.MethodDefinitionData;
import com.reandroid.unity.metadata.data.TypeDefinitionData;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.MethodSpec;
import com.reandroid.unity.metadata.spec.SpecList;
import com.reandroid.utils.ObjectsUtil;

public class MethodDefinitionIndexList extends DefinitionIndexList<MethodDefinitionData> {

    public MethodDefinitionIndexList(VersionRange versionRange,
                                     IntegerReference start, IntegerReference count) {
        super(0, MetadataSectionType.METHODS, versionRange, start, count);
    }
    public MethodDefinitionIndexList(IntegerReference start, IntegerReference count) {
        this(null, start, count);
    }

    @Override
    public SpecList<MethodSpec> getSpec() {
        return ObjectsUtil.cast(super.getSpec());
    }

    @Override
    protected void linkDataElements(Object[] dataList) {
        super.linkDataElements(dataList);
    }

    @Override
    protected void onLinked(int i, MethodDefinitionData data) {
        if (i == 1) {
            preLinkDeclaringTypes();
        } else if (i == 0) {
            super.onLinked(i, data);
        }
    }
    private void preLinkDeclaringTypes() {
        TypeDefinitionIndex first = get(0).declaringType;
        TypeDefinitionData firstData = first.getData();
        if (firstData == null) {
            return;
        }
        int idx = first.get();
        int size = size();
        for (int i = 1; i < size; i++) {
            TypeDefinitionIndex declaringType = get(i).declaringType;
            if (idx == declaringType.get() && declaringType.getData() == null) {
                declaringType.setDataInternal(firstData);
            }
        }
    }

    @Override
    protected String simpleToString(MethodDefinitionData item) {
        if (item == null) {
            return "null";
        }
        return item.getName();
    }
}
