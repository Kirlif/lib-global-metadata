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

import com.reandroid.json.JSONObject;

public class GenericParameterSpec implements Spec {

    private final int flags;
    private final StringSpec name;
    private final SpecList<TypeSpec> constraints;

    private GenericParameterSpec(int flags, StringSpec name, SpecList<TypeSpec> constraints) {
        this.flags = flags;
        this.name = name;
        this.constraints = constraints;
    }

    public int getFlags() {
        return flags;
    }
    public String getName() {
        return getNameSpec().getValue();
    }
    public StringSpec getNameSpec() {
        return name;
    }
    public SpecList<TypeSpec> getConstraints() {
        return constraints;
    }

    @Override
    public String descriptor() {
        StringBuilder builder = new StringBuilder();
        builder.append(getName());
        SpecList<TypeSpec> constraints = getConstraints();
        if (!constraints.isEmpty()) {
            builder.append(' ');
            builder.append(constraints.descriptor(", "));
        }
        return builder.toString();
    }

    @Override
    public Object json() {
        JSONObject jsonObject = new JSONObject();
        int flags = getFlags();
        if (flags != 0) {
            jsonObject.put("flags", flags);
        }
        jsonObject.put("name", getName());
        SpecList<TypeSpec> constraints = getConstraints();
        if (!constraints.isEmpty()) {
            jsonObject.put("constraints", constraints.json());
        }
        return jsonObject;
    }

    @Override
    public String toString() {
        return descriptor();
    }

    public static GenericParameterSpec of(int flags, StringSpec name, SpecList<TypeSpec> constraints) {
        if (name == null) {
            return null;
        }
        if (constraints == null) {
            constraints = SpecList.getEmptyList();
        }
        return new GenericParameterSpec(flags, name, constraints);
    }
    public static GenericParameterSpec of(int flags, StringSpec name, TypeSpec ... constraints) {
        return of(flags, name, SpecList.of(constraints));
    }
    public static GenericParameterSpec of(StringSpec name, TypeSpec ... constraints) {
        return of(0, name, SpecList.of(constraints));
    }
    public static GenericParameterSpec of(StringSpec name, SpecList<TypeSpec> constraints) {
        return of(0, name, constraints);
    }
}
