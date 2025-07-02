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

import com.reandroid.arsc.container.CountedBlockList;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.attribute.AttributeInstance;
import com.reandroid.unity.metadata.attribute.ArgumentValue;
import com.reandroid.unity.metadata.base.BytesKey;
import com.reandroid.unity.metadata.base.JsonData;
import com.reandroid.unity.metadata.base.LinkableItem;
import com.reandroid.unity.metadata.base.MDCompressedUInt32;
import com.reandroid.unity.metadata.index.MethodDefinitionIndex;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.value.MetadataValue;
import com.reandroid.utils.StringsUtil;
import com.reandroid.utils.collection.ComputeIterator;
import com.reandroid.utils.collection.IterableIterator;

import java.io.IOException;
import java.util.Iterator;

public class CustomAttributeData extends SectionData implements OffsetIdxData {

    private final MDCompressedUInt32 count;
    private final CountedBlockList<MethodDefinitionIndex> constructorIndexes;
    private final CountedBlockList<AttributeInstance> constructorArgumentList;

    private int mOffset;

    public CustomAttributeData() {
        super(3);
        this.count = new MDCompressedUInt32();
        this.constructorIndexes = new CountedBlockList<>(MethodDefinitionIndex.CREATOR, count);
        this.constructorArgumentList = new CountedBlockList<>(AttributeInstance.CREATOR, count);
        this.mOffset = -1;

        addChild(0, count);
        addChild(1, constructorIndexes);
        addChild(2, constructorArgumentList);
    }

    public Iterator<MetadataValue> visitValues() {
        return ComputeIterator.of(visitArguments(), ArgumentValue::getValue);
    }
    public Iterator<ArgumentValue> visitArguments() {
        return new IterableIterator<AttributeInstance, ArgumentValue>(
                getConstructorArguments()) {
            @Override
            public Iterator<ArgumentValue> iterator(AttributeInstance element) {
                return element.visitArguments();
            }
        };
    }
    public Iterator<AttributeInstance> getConstructorArguments() {
        return constructorArgumentList.iterator();
    }

    @Override
    public int getIdx() {
        return getOffset();
    }

    @Override
    public BytesKey getKey() {
        return checkKey(new BytesKey(getBytes()));
    }

    @Override
    public int getOffset() {
        return mOffset;
    }
    @Override
    public void setOffset(int offset) {
        this.mOffset = offset;
    }
    @Override
    public int getDataSize() {
        return countBytes();
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        setOffset(reader.getPosition());
        super.onReadBytes(reader);
    }

    @Override
    public void link() {
        LinkableItem.linkAll(constructorIndexes.iterator());
        LinkableItem.linkAll(constructorArgumentList.iterator());
    }

    @Override
    public int computeUnitSize() {
        return 0;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("indexes", JsonData.toJsonArray(constructorIndexes));
        if (constructorArgumentList .iterator(a -> !a.isEmpty()).hasNext()) {
            jsonObject.put("arguments", JsonData.toJsonArray(constructorArgumentList));
        }
        return jsonObject;
    }

    @Override
    public Object getJson() {
        return toJson();
    }

    @Override
    public MetadataSectionType<CustomAttributeData> getSectionType() {
        return MetadataSectionType.ATTRIBUTE_DATA;
    }
    @Override
    public String toString() {
        return "count=" + count +
                ", constructorIndexes=" +
                StringsUtil.join(constructorIndexes.iterator(), ", ") +
                ", constructorArgumentList=" +
                StringsUtil.join(constructorArgumentList.iterator(), ", ");
    }
}
