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
import com.reandroid.unity.metadata.base.MDInt;
import com.reandroid.unity.metadata.index.TypeDefinitionIndex;
import com.reandroid.unity.metadata.spec.Spec;

public class InterfaceOffsetPairData extends SectionData {

    public final TypeDefinitionIndex interfaceTypeIndex;
    public final MDInt offset;

    public InterfaceOffsetPairData() {
        super(2);
        this.interfaceTypeIndex = new TypeDefinitionIndex();
        this.offset = new MDInt();

        addChild(0, interfaceTypeIndex);
        addChild(1, offset);
    }

    @Override
    public Spec getSpec() {
        return interfaceTypeIndex.getSpec();
    }

    @Override
    public void link() {
        super.link();
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", interfaceTypeIndex.getJson());
        jsonObject.put("offset", offset.getJson());
        return jsonObject;
    }

    @Override
    public String toString() {
        return "interfaceTypeIndex=" + interfaceTypeIndex +
                ", offset=" + offset;
    }
}
