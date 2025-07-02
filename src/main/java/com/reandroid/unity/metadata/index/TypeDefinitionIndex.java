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
import com.reandroid.unity.metadata.spec.TypeSpec;


public class TypeDefinitionIndex extends DefinitionIndex<TypeDefinitionData> {

    public TypeDefinitionIndex(VersionRange versionRange) {
        super(MetadataSectionType.TYPE_DEFINITIONS, versionRange);
    }
    public TypeDefinitionIndex() {
        super(MetadataSectionType.TYPE_DEFINITIONS, null);
    }
    public TypeDefinitionIndex(IntegerReference reference) {
        super(MetadataSectionType.TYPE_DEFINITIONS, null, reference);
    }
    public String getName() {
        TypeDefinitionData data = getData();
        if (data != null) {
            return data.getName();
        }
        return null;
    }
    public String getNamespace() {
        TypeDefinitionData data = getData();
        if (data != null) {
            return data.getNamespace();
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
    public String getElementTypeName() {
        TypeDefinitionData data = getElementType();
        if (data != null) {
            return data.getTypeName();
        }
        return null;
    }
    public TypeDefinitionData getElementType() {
        TypeDefinitionData data = getData();
        if (data != null) {
            return data.elementTypeIndex().getData();
        }
        return null;
    }

    @Override
    public TypeSpec getSpec() {
        return (TypeSpec) super.getSpec();
    }

    public void linkTypes() {
        link();
        TypeDefinitionData data = getData();
        if (data != null) {
            data.parentIndex().link();
            data.elementTypeIndex().link();
            data.declaringTypeIndex().link();
            data.getFieldList().link();
        }
    }

    @Override
    public String toString() {
        return get() + "{" + getName() + "}";
    }
}
