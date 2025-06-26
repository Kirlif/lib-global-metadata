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
import com.reandroid.utils.ObjectsUtil;

public class SpecPair<TFirst extends Spec, TSecond extends Spec> implements Spec {

    private final TFirst first;
    private final Object separator;
    private final TSecond second;

    private SpecPair(TFirst first, Object separator, TSecond second) {
        this.first = first;
        this.separator = separator;
        this.second = second;
    }

    public TFirst getFirst() {
        return first;
    }
    public TSecond getSecond() {
        return second;
    }

    @Override
    public String descriptor() {
        return getFirst().descriptor() + separator + getSecond().descriptor();
    }

    @Override
    public Object json() {
        JSONArray jsonArray = new JSONArray(2);
        jsonArray.put(getFirst().descriptor());
        jsonArray.put(getSecond().descriptor());
        return jsonArray;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SpecPair)) {
            return false;
        }
        SpecPair<?, ?> spec = (SpecPair<?, ?>) obj;
        return ObjectsUtil.equals(getFirst(), spec.getFirst()) &&
                ObjectsUtil.equals(getSecond(), spec.getSecond());
    }

    @Override
    public int hashCode() {
        return ObjectsUtil.hash(getFirst(), getSecond());
    }
    @Override
    public String toString() {
        return descriptor();
    }

    public static<TFirst extends Spec, TSecond extends Spec> SpecPair<TFirst, TSecond> of(
            TFirst first, TSecond second) {
        return of(first, ", ", second);
    }
    public static<TFirst extends Spec, TSecond extends Spec> SpecPair<TFirst, TSecond> of(
            TFirst first, Object separator, TSecond second) {
        if (first == null || second == null) {
            return null;
        }
        return new SpecPair<>(first, separator, second);
    }
}
