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
import com.reandroid.unity.metadata.index.FieldDefinitionIndex;
import com.reandroid.unity.metadata.index.TypeDefinitionIndex;
import com.reandroid.unity.metadata.section.MetadataSectionType;

public class FieldMarshaledSizeData extends SectionData {

    public final FieldDefinitionIndex field;
    public final TypeDefinitionIndex type;
    public final MDInt size;

    public FieldMarshaledSizeData() {
        super(3);
        this.field = new FieldDefinitionIndex();
        this.type = new TypeDefinitionIndex();
        this.size = new MDInt();

        addChild(0, field);
        addChild(1, type);
        addChild(2, size);
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("field", field.getJson());
        jsonObject.put("type", type.getJson());
        jsonObject.put("size", size.getJson());
        return jsonObject;
    }

    @Override
    public MetadataSectionType<FieldMarshaledSizeData> getSectionType() {
        return MetadataSectionType.FIELD_MARSHALED_SIZE;
    }
    @Override
    public String toString() {
        return "field=" + field + ", type=" + type + ", size=" + size;
    }
}
