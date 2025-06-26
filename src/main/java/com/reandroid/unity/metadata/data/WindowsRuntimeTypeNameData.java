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
import com.reandroid.unity.metadata.index.CodeStringIndex;
import com.reandroid.unity.metadata.index.TypeDefinitionIndex;
import com.reandroid.unity.metadata.spec.SpecList;

public class WindowsRuntimeTypeNameData extends SectionData {

    public final CodeStringIndex nameIndex;
    public final TypeDefinitionIndex typeIndex;

    public WindowsRuntimeTypeNameData() {
        super(2);
        this.nameIndex = new CodeStringIndex();
        this.typeIndex = new TypeDefinitionIndex();

        addChild(0, nameIndex);
        addChild(1, typeIndex);
    }

    public String getName() {
        return getNameIndex().getString();
    }
    public String getTypeName() {
        return getTypeIndex().getTypeName();
    }

    public CodeStringIndex getNameIndex() {
        return nameIndex;
    }
    public TypeDefinitionIndex getTypeIndex() {
        return typeIndex;
    }

    @Override
    public SpecList<?> getSpec() {
        return SpecList.checkNonNull(getNameIndex().getSpec(), getTypeIndex().getSpec());
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", getNameIndex().getJson());
        jsonObject.put("type", getTypeIndex().getJson());
        return jsonObject;
    }

    @Override
    public String toString() {
        return "nameIndex=" + nameIndex +
                ", typeIndex=" + typeIndex;
    }
}
