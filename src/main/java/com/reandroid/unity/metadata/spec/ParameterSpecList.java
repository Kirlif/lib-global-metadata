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
package com.reandroid.unity.metadata.spec;

import com.reandroid.utils.ObjectsStore;

import java.util.Iterator;

public class ParameterSpecList extends SpecList<ParameterSpec> {

    public static ParameterSpecList EMPTY = new ParameterSpecList(null);

    private ParameterSpecList(Object[] elements) {
        super(", ", elements);
    }

    @Override
    public Object json() {
        return descriptor();
    }

    public static ParameterSpecList build(Iterator<? extends ParameterSpec> iterator) {
        Object list = ObjectsStore.addAll(null, iterator);
        int size = ObjectsStore.size(list);
        if (size == 0) {
            return EMPTY;
        }
        if (size == 1) {
            ParameterSpec spec = ObjectsStore.get(list, 0);
            return of(spec);
        }
        ParameterSpec[] elements = new ParameterSpec[size];
        ObjectsStore.collect(list, elements);
        return of(elements);
    }
    public static ParameterSpecList of(ParameterSpec ... elements) {
        if (elements == null || elements.length == 0) {
            return EMPTY;
        }
        return new ParameterSpecList(elements);
    }
}
