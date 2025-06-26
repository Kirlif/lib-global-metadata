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
package com.reandroid.unity.metadata.base;

import com.reandroid.arsc.container.BlockList;
import com.reandroid.json.JSONArray;

import java.util.Iterator;

public interface JsonData {
    Object getJson();
    void setJson(Object obj);

    static JSONArray toJsonArray(BlockList<? extends JsonData> blockList) {
        JSONArray jsonArray = new JSONArray(blockList.size());
        Iterator<? extends JsonData> iterator = blockList.iterator();
        while (iterator.hasNext()) {
            jsonArray.put(iterator.next().getJson());
        }
        if (jsonArray.isEmpty()) {
            return null;
        }
        return jsonArray;
    }
    static JSONArray toJsonArray(Iterator<? extends JsonData> iterator) {
        JSONArray jsonArray = new JSONArray();
        while (iterator.hasNext()) {
            jsonArray.put(iterator.next().getJson());
        }
        if (jsonArray.isEmpty()) {
            return null;
        }
        return jsonArray;
    }
}
