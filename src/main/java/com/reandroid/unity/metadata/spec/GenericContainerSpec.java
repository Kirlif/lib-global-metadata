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

public abstract class GenericContainerSpec<T extends Spec> implements Spec {

    private final T owner;
    private final SpecList<GenericParameterSpec> parameters;

    GenericContainerSpec(T owner, SpecList<GenericParameterSpec> parameters) {
        this.owner = owner;
        this.parameters = parameters;
    }

    public T getOwner() {
        return owner;
    }

    public SpecList<GenericParameterSpec> getParameters() {
        return parameters;
    }

    public abstract boolean isMethod();

    @Override
    public String descriptor() {
        StringBuilder builder = new StringBuilder();
        builder.append('<');
        SpecList<GenericParameterSpec> parameters = getParameters();
        if (!parameters.isEmpty()) {
            builder.append(parameters.descriptor(", "));
        }
        builder.append(">");
        return builder.toString();
    }

    @Override
    public Object json() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("owner", getOwner().json());
        SpecList<GenericParameterSpec> parameters = getParameters();
        if (!parameters.isEmpty()) {
            jsonObject.put("parameters", parameters.json());
        }
        return jsonObject;
    }

    @Override
    public String toString() {
        return descriptor();
    }

    public static GenericContainerSpec<?> of(Spec owner, SpecList<GenericParameterSpec> parameters) {
        if (owner == null) {
            return null;
        }
        if (owner instanceof MethodSpec) {
            return GenericMethodSpec.create((MethodSpec) owner, parameters);
        }
        return GenericTypeSpec.create((TypeSpec) owner, parameters);
    }
}
