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
package com.reandroid.unity.metadata.attribute;

import com.reandroid.arsc.base.Creator;
import com.reandroid.arsc.container.FixedBlockContainer;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.BooleanReference;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.JsonData;
import com.reandroid.unity.metadata.base.LinkableItem;
import com.reandroid.unity.metadata.base.MDCompressedSInt32;
import com.reandroid.unity.metadata.base.MDCompressedUInt32;
import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.unity.metadata.index.DefinitionIndex;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.SpecPair;
import com.reandroid.unity.metadata.value.MetadataValue;

import java.io.IOException;

public class NamedArgumentValue extends FixedBlockContainer implements LinkableItem, JsonData {

    private final boolean is_property;
    private final ArgumentValue arg;

    private final BooleanReference fieldIndexType;
    private final DefinitionIndex<?> definitionIndex;

    public NamedArgumentValue(boolean is_property) {
        super(4);
        this.is_property = is_property;

        this.arg = new ArgumentValue();
        MDCompressedSInt32 fieldIndex = new MDCompressedSInt32();
        BooleanReference fieldIndexType = new BooleanReference() {
            @Override
            public boolean get() {
                return fieldIndex.get() >= 0;
            }
            @Override
            public void set(boolean b) {
                int value = fieldIndex.get();
                if (b) {
                    if (value < 0) {
                        fieldIndex.set(0);
                    }
                } else {
                    if (value >= 0) {
                        fieldIndex.set(-1);
                    }
                }
            }
            @Override
            public String toString() {
                return Boolean.toString(get());
            }
        };
        this.fieldIndexType = fieldIndexType;

        MDCompressedUInt32 declaringTypeIndex = new MDCompressedUInt32() {
            @Override
            public boolean isNull() {
                return super.isNull() || fieldIndexType.get();
            }
        };
        IntegerReference indexReference = new IntegerReference() {
            @Override
            public int get() {
                if (fieldIndexType.get()) {
                    return fieldIndex.get();
                }
                return declaringTypeIndex.get();
            }
            @Override
            public void set(int i) {
                if (fieldIndexType.get()) {
                    fieldIndex.set(i);
                } else {
                    declaringTypeIndex.set(i);
                }
            }
            @Override
            public String toString() {
                return Integer.toString(get());
            }
        };

        this.definitionIndex = new DefinitionIndex<SectionData>(null, null, indexReference) {
            @Override
            public MetadataSectionType<? extends SectionData> getSectionType() {
                if (fieldIndexType.get()) {
                    return MetadataSectionType.FIELDS;
                }
                return MetadataSectionType.TYPE_DEFINITIONS;
            }
        };

        addChild(0, arg);
        addChild(1, fieldIndex);
        addChild(2, declaringTypeIndex);

        // blank block
        addChild(3, definitionIndex);
    }

    public boolean isProperty() {
        return is_property;
    }
    public ArgumentValue getArg() {
        return arg;
    }
    public BooleanReference getFieldIndexType() {
        return fieldIndexType;
    }
    public DefinitionIndex<?> getDefinitionIndex() {
        return definitionIndex;
    }
    public MetadataValue getValue() {
        return getArg().getValue();
    }

    public SpecPair<?, ?> getSpec() {
        return SpecPair.of(getDefinitionIndex().getSpec(),
                " = ", getArg().getSpec());
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        super.onReadBytes(reader);
    }

    @Override
    public void link() {
        LinkableItem.linkObject(arg);
        LinkableItem.linkObject(definitionIndex);
    }

    @Override
    public Object getJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("definition", getDefinitionIndex().getJson());
        jsonObject.put("arg", getArg().getJson());
        return jsonObject;
    }

    @Override
    public void setJson(Object obj) {

    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (isProperty()) {
            builder.append("Property{");
        } else {
            builder.append("Field{");
        }
        builder.append("type=");
        builder.append(getFieldIndexType());
        builder.append(", ");
        builder.append(definitionIndex);
        builder.append('=');
        builder.append(arg);
        builder.append('}');
        return builder.toString();
    }

    public static final Creator<NamedArgumentValue> CREATOR_FIELD =
            () -> new NamedArgumentValue(false);

    public static final Creator<NamedArgumentValue> CREATOR_PROPERTY =
            () -> new NamedArgumentValue(true);
}
