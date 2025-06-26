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
import com.reandroid.unity.metadata.index.DefinitionIndex;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.Spec;
import com.reandroid.utils.ObjectsUtil;

public class DefinitionEntryData<T extends SectionData> extends SectionData implements IntegerReference {

    private final DefinitionIndex<T> definitionIndex;

    public DefinitionEntryData(MetadataSectionType<T> sectionType) {
        super(1);
        this.definitionIndex = new DefinitionIndex<>(sectionType);

        addChild(0, definitionIndex);
    }

    public T getData() {
        return getDefinitionIndex().getData();
    }
    @Override
    public Spec getSpec() {
        return getDefinitionIndex().getSpec();
    }

    @Override
    public Object getJson() {
        return getDefinitionIndex().getJson();
    }


    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("item", definitionIndex.getJson());
        return jsonObject;
    }


    @Override
    public int get() {
        return getDefinitionIndex().get();
    }
    @Override
    public void set(int value) {
        getDefinitionIndex().set(value);
    }

    public DefinitionIndex<T> getDefinitionIndex() {
        return definitionIndex;
    }

    @Override
    public int computeUnitSize() {
        return 4;
    }

    @Override
    public MetadataSectionType<? extends DefinitionEntryData<T>> getSectionType() {
        return ObjectsUtil.cast(super.getSectionType());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getIdx());
        builder.append('{');
        int i = get();
        if (i == SectionData.INVALID_IDX) {
            builder.append("NULL");
        } else {
            Spec spec = getSpec();
            if (spec != null) {
                builder.append(spec);
            } else {
                builder.append(get());
            }
        }
        builder.append('}');
        return builder.toString();
    }
}
