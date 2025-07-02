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
package com.reandroid.unity.metadata.value;

import com.reandroid.arsc.base.Block;
import com.reandroid.arsc.base.DirectStreamReader;
import com.reandroid.arsc.container.FixedBlockContainer;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.JsonData;
import com.reandroid.unity.metadata.section.BlobDataPool;
import com.reandroid.unity.metadata.spec.Spec;
import com.reandroid.utils.ObjectsUtil;

import java.io.IOException;
import java.io.InputStream;

public class MetadataValue extends FixedBlockContainer implements DirectStreamReader, JsonData {

    static final int START_INDEX = ObjectsUtil.of(1);

    private final Il2CppTypeEnum il2CppTypeEnum;
    private final Il2CppTypeEnumBlock typeEnumBlock;

    public MetadataValue(int childesCount, Il2CppTypeEnum il2CppTypeEnum) {
        super(START_INDEX + childesCount);
        this.il2CppTypeEnum = il2CppTypeEnum;
        this.typeEnumBlock = new Il2CppTypeEnumBlock(il2CppTypeEnum);

        addChild(0, typeEnumBlock);
    }

    public void clearEnumBlock() {
        Block[] blocks = getChildes();
        if (blocks[0] == typeEnumBlock) {
            blocks[0] = null;
        }
    }
    public Il2CppTypeEnumBlock getTypeEnumBlock() {
        return typeEnumBlock;
    }
    public Il2CppTypeEnum getTypeEnum() {
        return il2CppTypeEnum;
    }

    @Override
    public int readBytes(InputStream inputStream) throws IOException {
        Block[] blocks = getChildes();
        int sum = 0;
        for (Block block : blocks) {
            sum += ((DirectStreamReader) block).readBytes(inputStream);
        }
        return sum;
    }

    @Override
    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        Il2CppTypeEnum typeEnum = getTypeEnum();
        if (typeEnum == Il2CppTypeEnum.StaticArrayInitType) {
            jsonObject.put("type", Il2CppTypeEnum.getStaticArrayInitTypeName(countBytes()));
        } else {
            jsonObject.put("type", typeEnum.name());
        }
        jsonObject.put("value", getJsonValue());
        return jsonObject;
    }
    public Spec getSpec() {
        return null;
    }
    public Object value() {
        return null;
    }
    public Object getJsonValue() {
        Object obj = value();
        if (obj != null) {
            return obj;
        }
        Spec spec = getSpec();
        if (spec != null) {
            return spec.json();
        }
        return "***_NOT_IMPLEMENTED_***";
    }
    @Override
    public void setJson(Object obj) {

    }
    public void onDataChanged() {
        BlobDataPool dataPool = getParentInstance(BlobDataPool.class);
        if (dataPool != null) {
            dataPool.markModified();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getTypeEnum().name() + ")";
    }
}
