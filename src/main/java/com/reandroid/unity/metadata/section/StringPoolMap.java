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

import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.utils.collection.ArrayCollection;
import com.reandroid.utils.collection.CombiningIterator;

import java.util.Iterator;
import java.util.List;

public class StringPoolMap<T extends SectionData> extends SectionPoolMap<T> {

    private final ArrayCollection<T> appendixList;

    public StringPoolMap() {
        super();
        this.appendixList = new ArrayCollection<>();
    }

    public int appendicesSize() {
        return appendixList.size();
    }

    public Iterator<T> appendices() {
        return appendixList.clonedIterator();
    }

    public T removeAppendix(T value) {
        if (value != null) {
            appendixList.remove(value);
            return remove(value.getKey(), value);
        }
        return null;
    }
    public void putAppendix(String key, T value) {
        if (value != null) {
            List<T> appendixList = this.appendixList;
            if (containsValue(key, t -> t == value)) {
                if (appendixList.contains(value)) {
                    return;
                }
            }
            appendixList.add(value);
            put(key, value);
        }
    }

    @Override
    public void initialize(int initialSize, Iterator<? extends T> iterator) {
        List<T> appendixList = this.appendixList;
        int appendixSize = appendixList.size();
        if (appendixSize != 0) {
            initialSize += appendixSize;
            iterator = CombiningIterator.two(iterator, appendixList.iterator());
        }
        super.initialize(initialSize, iterator);
    }

    @Override
    public String toString() {
        return "size = " + size() + ", appendixes = " + appendicesSize();
    }
}
