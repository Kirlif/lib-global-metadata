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
package com.reandroid.unity.metadata.section;

import com.reandroid.arsc.base.Creator;
import com.reandroid.json.JSONArray;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.header.MetadataSectionHeader;
import com.reandroid.unity.metadata.data.MDString;
import com.reandroid.utils.ObjectsUtil;
import com.reandroid.utils.collection.ComputeIterator;
import com.reandroid.utils.collection.GroupMap;

import java.util.Iterator;

public abstract class SectionStringData<T extends MDString> extends MetadataSection<T> implements Iterable<T> {

    private final GroupMap<String, T> groupMap;

    private boolean mModified;

    public SectionStringData(Creator<T> creator, MetadataSectionHeader sectionHeader) {
        super(creator, sectionHeader);

        this.groupMap = new GroupMap<>();

        getEntriesAlignment().setAlignment(4);
    }
    public SectionStringData(MetadataSectionHeader offsetSize) {
        this(ObjectsUtil.cast(offsetSize.getCreator()), offsetSize);
    }

    public T getString(int i) {
        return get(i);
    }
    public T getString(String text) {
        return groupMap.get(text);
    }
    public T getOrCreate(String text) {
        GroupMap<String, T> groupMap = this.groupMap;
        T mdString = groupMap.get(text);
        if (mdString == null) {
            mdString = createNext();
            mdString.set(text);
            groupMap.put(text, mdString);
        }
        return mdString;
    }
    public Iterator<String> getStrings() {
        return ComputeIterator.of(iterator(), MDString::get);
    }

    public void buildMap() {
        GroupMap<String, T> map = this.groupMap;
        map.clear();
        for (T mdString : this) {
            map.put(mdString.get(), mdString);
        }
    }
    public void onStringChanged(String old, String text) {
        groupMap.updateKey(old, text);
        if (!mModified) {
            mModified = true;
        }
    }

    @Override
    public void fromJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        JSONArray jsonArray = jsonObject.optJSONArray(getSectionType().name());
        if (jsonArray == null) {
            return;
        }
        int count = jsonArray.length();
        MetadataEntryList<T> stringList = getEntryList();
        stringList.setSize(count);
        for (int i = 0; i < count; i++) {
            get(i).set(jsonArray.getString(i));
        }
        buildMap();
    }

    @Override
    int sizeOfEntry() {
        return 0;
    }

    @Override
    public void link() {
    }
}
