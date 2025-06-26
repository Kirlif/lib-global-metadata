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

import com.reandroid.json.JSONArray;
import com.reandroid.utils.ObjectsStore;
import com.reandroid.utils.ObjectsUtil;
import com.reandroid.utils.collection.ArrayIterator;

import java.util.Iterator;

public class SpecList<T extends Spec> implements Spec {

    private static final Object[] EMPTY_ARRAY = new Object[0];
    private static final SpecList<?> emptyList = new SpecList<>(EMPTY_ARRAY);

    private final String separator;
    private final Object[] elements;

    SpecList(String separator, Object[] elements) {
        this.separator = separator;
        if (elements == null || elements.length == 0) {
            elements = EMPTY_ARRAY;
        }
        this.elements = elements;
    }
    SpecList(Object[] elements) {
        this(", ", elements);
    }

    public int size() {
        return elements.length;
    }
    public T get(int i) {
        return ObjectsUtil.cast(elements[i]);
    }
    public Iterator<T> iterator() {
        return ArrayIterator.of(elements);
    }
    public boolean isEmpty() {
        return size() == 0;
    }

    public String getSeparator() {
        return separator;
    }
    @Override
    public String descriptor() {
        return descriptor(getSeparator());
    }
    public String descriptor(String separator) {
        if (separator == null) {
            separator = "";
        }
        StringBuilder builder = new StringBuilder();
        int size = size();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                builder.append(separator);
            }
            T item = get(i);
            builder.append(item.descriptor());
        }
        return builder.toString();
    }

    @Override
    public Object json() {
        int size = size();
        if (size == 0) {
            return null;
        }
        JSONArray jsonArray = new JSONArray(size());
        Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            jsonArray.put(iterator.next().json());
        }
        return jsonArray;
    }

    @Override
    public int hashCode() {
        return ObjectsUtil.hashElements(elements);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SpecList)) {
            return false;
        }
        SpecList<?> spec = (SpecList<?>) obj;
        return ObjectsUtil.equalsArray(this.elements, spec.elements);
    }

    @Override
    public String toString() {
        return descriptor();
    }

    public static<T extends Spec> SpecList<T>  collect(Iterator<? extends T> iterator) {
        Object list = ObjectsStore.addAll(null, iterator);
        int size = ObjectsStore.size(list);
        if (size == 0) {
            return getEmptyList();
        }
        if (size == 1) {
            return of(new Object[]{ObjectsStore.get(list, 0)});
        }
        Object[] elements = new Object[size];
        ObjectsStore.collect(list, elements);
        return new SpecList<>(elements);
    }
    public static<T extends Spec> SpecList<T> of(Object[] elements) {
        if (elements == null || elements.length == 0) {
            return getEmptyList();
        }
        return new SpecList<>(elements);
    }
    public static<T extends Spec> SpecList<T> checkNonNull(Spec ... elements) {
        if (isEmptyOrHasNullElement(elements)) {
            return null;
        }
        return new SpecList<>(elements);
    }

    public static<T extends Spec> SpecList<T> getEmptyList() {
        return ObjectsUtil.cast(emptyList);
    }


    private static boolean isEmptyOrHasNullElement(Object[] elements) {
        if (elements == null) {
            return true;
        }
        int length = elements.length;
        if (length == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (elements[i] == null) {
                return true;
            }
        }
        return false;
    }
}
