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
import com.reandroid.utils.CompareUtil;
import com.reandroid.utils.ObjectsUtil;
import com.reandroid.utils.collection.ArrayCollection;
import com.reandroid.utils.collection.ComputeIterator;

import java.util.Iterator;
import java.util.List;

public abstract class SectionStringData<T extends MDString> extends
        MetadataSection<T> implements Iterable<T> {

    public SectionStringData(Creator<T> creator, MetadataSectionHeader sectionHeader) {
        super(creator, sectionHeader);

        getEntriesAlignment().setAlignment(4);

        getPoolMap().setFavouriteObjectsSorter(CompareUtil.getComparableComparator());
    }
    public SectionStringData(MetadataSectionHeader offsetSize) {
        this(ObjectsUtil.cast(offsetSize.getCreator()), offsetSize);
    }

    public T getString(int i) {
        return get(i);
    }
    public T getString(String text) {
        return getPoolMap().get(text);
    }
    public T getOrCreate(String text) {
        StringPoolMap<T> map = this.getPoolMap();
        T mdString = map.get(text);
        if (mdString == null) {
            mdString = createNext();
            mdString.set(text);
            map.put(text, mdString);
        }
        return mdString;
    }
    public Iterator<String> getStrings() {
        return ComputeIterator.of(iterator(), MDString::get);
    }

    @Override
    public StringPoolMap<T> getPoolMap() {
        return (StringPoolMap<T>) super.getPoolMap();
    }
    @Override
    StringPoolMap<T> newPoolMap() {
        return new StringPoolMap<>();
    }
    public boolean clearDuplicates() {
        List<T> removeList = new ArrayCollection<>();
        getPoolMap().findDuplicates(
                (item1, item2) -> CompareUtil.compare(item1.get(), item2.get()),
                list -> {
            T first = list.get(0);
            int size = list.size();
            for (int i = 1; i < size; i++) {
                T item = list.get(i);
                item.setReplacement(first);
                removeList.add(item);
            }
        });
        for (T item : removeList) {
            item.removeSelf();
        }
        return !removeList.isEmpty();
    }
    public void onKeyChanged(String old, String text, T stringData) {
        getPoolMap().updateKey(old, text, stringData);
    }

    @Override
    public void onPreRemove(T item) {
        getPoolMap().remove(item.get(), item);
        super.onPreRemove(item);
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
        reBuildMap();
    }

    @Override
    int sizeOfEntry() {
        return 0;
    }

    @Override
    public void link() {
    }
}
