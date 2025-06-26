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

import com.reandroid.utils.ObjectsUtil;

public class ParameterSpec implements Spec {

    private final StringSpec name;
    private final TypeSpec type;
    private final Spec value;

    private ParameterSpec(StringSpec name, TypeSpec type, Spec value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return getNameSpec().getValue();
    }

    public String getType() {
        return getTypeSpec().descriptor();
    }

    public StringSpec getNameSpec() {
        return name;
    }

    public TypeSpec getTypeSpec() {
        return type;
    }

    @Override
    public String descriptor() {
        StringBuilder builder = new StringBuilder();
        builder.append(getType());
        builder.append(' ');
        builder.append(getName());
        if (value != null) {
            builder.append(" = ");
            builder.append(value.descriptor());
        }
        return builder.toString();
    }

    @Override
    public Object json() {
        return descriptor();
    }

    @Override
    public int hashCode() {
        return ObjectsUtil.hash(name, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ParameterSpec)) {
            return false;
        }
        ParameterSpec spec = (ParameterSpec) obj;
        return ObjectsUtil.equals(name, spec.name) &&
                ObjectsUtil.equals(type, spec.type);
    }

    @Override
    public String toString() {
        return descriptor();
    }

    public static ParameterSpec of(StringSpec name, TypeSpec type) {
        if (name == null || type == null) {
            return null;
        }
        return of(name, type, null);
    }
    public static ParameterSpec of(StringSpec name, TypeSpec type, Spec value) {
        if (name == null || type == null) {
            return null;
        }
        return new ParameterSpec(name, type, value);
    }
}
