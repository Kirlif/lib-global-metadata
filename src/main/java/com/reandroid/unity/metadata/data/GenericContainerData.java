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
import com.reandroid.unity.metadata.base.MDInt;
import com.reandroid.unity.metadata.index.DefinitionIndex;
import com.reandroid.unity.metadata.index.DefinitionIndexList;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.GenericContainerSpec;
import com.reandroid.utils.ObjectsUtil;

public class GenericContainerData extends SectionData {

    private final OwnerIndex ownerIndex;
    private final DefinitionIndexList<GenericParameterData> genericParameterList;

    public GenericContainerData() {
        super(5);

        MDInt is_method = new MDInt(); // should be placed after count

        this.ownerIndex = new OwnerIndex(is_method);
        MDInt count = new MDInt();
        MDInt start = new MDInt();

        this.genericParameterList = new DefinitionIndexList<>(
                MetadataSectionType.GENERIC_PARAMETERS, start,
                count);

        addChild(0, ownerIndex);
        addChild(1, count);
        addChild(2, is_method);
        addChild(3, start);

        //blank block
        addChild(4, genericParameterList);
    }

    public SectionData getOwnerData() {
        return ownerIndex.getData();
    }
    public boolean isMethod() {
        return ownerIndex.isMethod();
    }
    public void setIsMethod(boolean isMethod) {
        ownerIndex.setIsMethod(isMethod);
    }

    @Override
    public void link() {
        super.link();
    }

    @Override
    public GenericContainerSpec<?> getSpec() {
        return GenericContainerSpec.of(ownerIndex.getSpec(),
                ObjectsUtil.cast(genericParameterList.getSpec()));
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("index", getIndex());
        jsonObject.put("spec", getSpec().descriptor());

        jsonObject.put("ownerIndex", ownerIndex.getJson());
        jsonObject.put("parameters", genericParameterList.getJson());
        return jsonObject;
    }

    @Override
    public MetadataSectionType<GenericContainerData> getSectionType() {
        return MetadataSectionType.GENERIC_CONTAINERS;
    }
    @Override
    public String toString() {
        return "ownerIndex=" + ownerIndex
                + ", is_method=" + isMethod()
                + ", genericParameterList=" + genericParameterList;
    }

    static class OwnerIndex extends DefinitionIndex<SectionData> {

        private final IntegerReference is_method;

        public OwnerIndex(IntegerReference is_method) {
            super(null);
            this.is_method = is_method;
        }

        public boolean isMethod() {
            return is_method.get() == 1;
        }
        public void setIsMethod(boolean isMethod) {
            IntegerReference is_method = this.is_method;
            if (isMethod != (is_method.get() == 1)) {
                is_method.set(isMethod ? 1 : 0);
                setData(null);
            }
        }

        @Override
        public void setDataInternal(SectionData data) {
            if (data != null) {
                setIsMethod(data instanceof MethodDefinitionData);
            }
            super.setDataInternal(data);
        }

        @Override
        public MetadataSectionType<?> getSectionType() {
            if (isMethod()) {
                return MetadataSectionType.METHODS;
            }
            return MetadataSectionType.TYPE_DEFINITIONS;
        }
    }
}
