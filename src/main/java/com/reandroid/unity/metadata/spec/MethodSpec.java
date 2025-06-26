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

public class MethodSpec implements Spec {

    private final TypeSpec declaring;
    private final StringSpec name;
    private final TypeSpec returnType;
    private final ParameterSpecList parameters;

    private MethodSpec(TypeSpec declaring, StringSpec name, TypeSpec returnType, ParameterSpecList parameters) {
        this.declaring = declaring;
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    public TypeSpec getDeclaring() {
        return declaring;
    }
    public String getName() {
        return getNameSpec().getValue();
    }
    public StringSpec getNameSpec() {
        return name;
    }
    public TypeSpec getReturnType() {
        return returnType;
    }
    public ParameterSpecList getParameters() {
        return parameters;
    }

    @Override
    public String descriptor() {
        StringBuilder builder = new StringBuilder();
        builder.append(declaring.descriptor());
        builder.append("->");
        builder.append(name.getValue());
        builder.append('(');
        builder.append(parameters.descriptor());
        builder.append(')');
        if (returnType != null) {
            builder.append(returnType.descriptor());
        }
        return builder.toString();
    }

    @Override
    public Object json() {
        return descriptor();
    }
    @Override
    public int hashCode() {
        return ObjectsUtil.hash(declaring, name, returnType, parameters);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MethodSpec)) {
            return false;
        }
        MethodSpec spec = (MethodSpec) obj;
        return ObjectsUtil.equals(declaring, spec.declaring) &&
                ObjectsUtil.equals(name, spec.name) &&
                ObjectsUtil.equals(returnType, spec.returnType) &&
                ObjectsUtil.equals(parameters, spec.parameters);
    }

    @Override
    public String toString() {
        return descriptor();
    }

    public static MethodSpec of(TypeSpec declaring, StringSpec name,
                                TypeSpec returnType, ParameterSpecList parameters) {
        if (declaring == null || name == null || name.isEmpty()) {
            return null;
        }
        if (parameters == null) {
            parameters = ParameterSpecList.EMPTY;
        }
        return new MethodSpec(declaring, name, returnType, parameters);
    }
}
