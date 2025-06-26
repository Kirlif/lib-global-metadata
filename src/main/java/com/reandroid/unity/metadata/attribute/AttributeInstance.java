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
import com.reandroid.arsc.container.BlockList;
import com.reandroid.arsc.container.CountedBlockList;
import com.reandroid.arsc.container.FixedBlockContainer;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.JsonData;
import com.reandroid.unity.metadata.base.LinkableItem;
import com.reandroid.unity.metadata.base.MDCompressedUInt32;
import com.reandroid.unity.metadata.util.JsonDataUtil;
import com.reandroid.utils.NumbersUtil;
import com.reandroid.utils.collection.CombiningIterator;
import com.reandroid.utils.collection.ComputeIterator;

import java.util.Iterator;
import java.util.function.Function;

public class AttributeInstance extends FixedBlockContainer implements LinkableItem, JsonData {

    private final CountedBlockList<ArgumentValue> constructorArgs;
    private final CountedBlockList<NamedArgumentValue> fieldArgs;
    private final CountedBlockList<NamedArgumentValue> propertyArgs;

    public AttributeInstance() {
        super(6);

        MDCompressedUInt32 constructorArgCount = new MDCompressedUInt32();
        MDCompressedUInt32 fieldArgCount = new MDCompressedUInt32();
        MDCompressedUInt32 propertyArgCount = new MDCompressedUInt32();

        this.constructorArgs = new CountedBlockList<>(ArgumentValue.CREATOR, constructorArgCount);
        this.fieldArgs = new CountedBlockList<>(NamedArgumentValue.CREATOR_FIELD, fieldArgCount);
        this.propertyArgs = new CountedBlockList<>(NamedArgumentValue.CREATOR_PROPERTY, propertyArgCount);

        addChild(0, constructorArgCount);
        addChild(1, fieldArgCount);
        addChild(2, propertyArgCount);
        addChild(3, constructorArgs);
        addChild(4, fieldArgs);
        addChild(5, propertyArgs);
    }

    public Iterator<ArgumentValue> visitArguments() {
        Function<NamedArgumentValue, ArgumentValue> function = NamedArgumentValue::getArg;
        return CombiningIterator.three(
                constructorArgs.iterator(),
                ComputeIterator.of(fieldArgs.iterator(), function),
                ComputeIterator.of(propertyArgs.iterator(), function)
        );
    }
    @Override
    public void link() {
        LinkableItem.linkBlockList(constructorArgs);
        LinkableItem.linkBlockList(fieldArgs);
        LinkableItem.linkBlockList(propertyArgs);
    }

    public boolean isEmpty() {
        return constructorArgs.size() == 0 &&
                fieldArgs.size() == 0 &&
                propertyArgs.size() == 0;
    }

    @Override
    public Object getJson() {
        if (isEmpty()) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("constructors", JsonDataUtil.collectOptional(
                ComputeIterator.of(constructorArgs.iterator(), ArgumentValue::getSpec)));
        jsonObject.put("fields", JsonDataUtil.collectOptional(
                ComputeIterator.of(fieldArgs.iterator(), NamedArgumentValue::getSpec)));
        jsonObject.put("properties", JsonDataUtil.collectOptional(
                ComputeIterator.of(propertyArgs.iterator(), NamedArgumentValue::getSpec)));
        return jsonObject;
    }

    @Override
    public void setJson(Object obj) {

    }
    @Override
    public String toString() {
        return "constructors" + toDebugString(constructorArgs)
                + "\nfields" + toDebugString(fieldArgs)
                + "\nproperties" + toDebugString(propertyArgs);
    }

    private static String toDebugString(BlockList<?> blockList) {
        int limit = 10;
        int size = blockList.size();
        int min = NumbersUtil.min(size, limit);
        StringBuilder builder = new StringBuilder();
        builder.append(" size = ");
        builder.append(size);
        builder.append('[');
        for (int i = 0; i < min; i++) {
            if (i != 0) {
                builder.append(", ");
            }
            builder.append(blockList.get(i));
        }
        size = size - min;
        if (size != 0) {
            builder.append(", ... +");
            builder.append(size);
            builder.append(" more");
        }
        builder.append(']');
        return builder.toString();
    }
    public static final Creator<AttributeInstance> CREATOR = AttributeInstance::new;
}
