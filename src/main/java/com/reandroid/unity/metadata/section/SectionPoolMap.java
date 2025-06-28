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
import com.reandroid.utils.collection.MultiMap;

import java.util.Iterator;

public class SectionPoolMap<T extends SectionData> extends MultiMap<Object, T> {

    public SectionPoolMap() {
        super();
    }

    public void initialize(int initialSize, Iterator<? extends T> iterator) {
        clear();
        if (initialSize != 0) {
            setInitialSize(initialSize);
        }
        super.putAll(T::getKey, iterator);
    }
}
