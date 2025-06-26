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

public class FieldSpec implements Spec {

    private final TypeSpec declaring;
    private final StringSpec name;
    private final TypeSpec type;
    private final Spec value;

    private FieldSpec(TypeSpec declaring, StringSpec name, TypeSpec type, Spec value) {
        this.declaring = declaring;
        this.name = name;
        this.type = type;
        this.value = value;
    }
    public FieldSpec changeDeclaring(TypeSpec declaring) {
        if (ObjectsUtil.equals(this.declaring, declaring)) {
            return this;
        }
        return of(declaring, name, type, value);
    }
    @Override
    public String descriptor() {
        StringBuilder builder = new StringBuilder();
        if (declaring != null) {
            builder.append(declaring.descriptor());
            builder.append("->");
        }
        builder.append(name.getValue());
        if (type != null) {
            builder.append(':');
            builder.append(type.descriptor());
        }
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
        if (!(obj instanceof FieldSpec)) {
            return false;
        }
        FieldSpec spec = (FieldSpec) obj;
        return ObjectsUtil.equals(name, spec.name) &&
                ObjectsUtil.equals(type, spec.type);
    }

    @Override
    public String toString() {
        return descriptor();
    }

    public static FieldSpec of(StringSpec name, TypeSpec type) {
        return of(name, type, null);
    }
    public static FieldSpec of(StringSpec name, TypeSpec type, Spec value) {
        return of(null, name, type, value);
    }
    public static FieldSpec of(TypeSpec declaring, StringSpec name, TypeSpec type, Spec value) {
        if (name == null) {
            return null;
        }
        return new FieldSpec(declaring, name, type, value);
    }
}
