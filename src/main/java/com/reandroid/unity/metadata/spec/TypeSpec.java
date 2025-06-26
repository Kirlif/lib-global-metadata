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

public class TypeSpec implements Spec {

    private final StringSpec namespace;
    private final StringSpec name;

    private TypeSpec(StringSpec namespace, StringSpec name) {
        this.namespace = namespace;
        this.name = name;
    }

    public String getName() {
        return getNameSpec().getValue();
    }

    public String getNamespace() {
        return getNamespaceSpec().getValue();
    }

    public StringSpec getNameSpec() {
        return name;
    }
    public StringSpec getNamespaceSpec() {
        return namespace;
    }

    @Override
    public String descriptor() {
        StringBuilder builder = new StringBuilder();
        StringSpec namespace = getNamespaceSpec();
        if (!namespace.isEmpty()) {
            builder.append(namespace.getValue());
            builder.append('.');
        }
        builder.append(getNameSpec().getValue());
        return builder.toString();
    }

    @Override
    public Object json() {
        return descriptor();
    }

    @Override
    public int hashCode() {
        return ObjectsUtil.hash(namespace, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TypeSpec)) {
            return false;
        }
        TypeSpec spec = (TypeSpec) obj;
        return ObjectsUtil.equals(namespace, spec.namespace) &&
                ObjectsUtil.equals(name, spec.name);
    }

    @Override
    public String toString() {
        return descriptor();
    }

    public static TypeSpec of(String namespace, String name) {
        return of(StringSpec.of(namespace), StringSpec.of(name));
    }
    public static TypeSpec of(StringSpec namespace, StringSpec name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        if (namespace == null) {
            namespace = StringSpec.NULL;
        }
        return new TypeSpec(namespace, name);
    }
}
