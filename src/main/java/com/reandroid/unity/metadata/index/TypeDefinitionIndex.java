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
package com.reandroid.unity.metadata.index;

import com.reandroid.arsc.io.BlockReader;
import com.reandroid.unity.metadata.base.VersionRange;
import com.reandroid.unity.metadata.data.TypeDefinitionData;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.TypeSpec;

import java.io.InputStream;
import java.io.OutputStream;

public class TypeDefinitionIndex extends DefinitionIndex<TypeDefinitionData> {

    public TypeDefinitionIndex(VersionRange versionRange) {
        super(MetadataSectionType.TYPE_DEFINITIONS, versionRange);
    }
    public TypeDefinitionIndex() {
        super(MetadataSectionType.TYPE_DEFINITIONS, null);
    }
    public String getName() {
        TypeDefinitionData data = getData();
        if (data != null) {
            return data.getName();
        }
        return null;
    }
    public String getNamespace() {
        TypeDefinitionData data = getData();
        if (data != null) {
            return data.getNamespace();
        }
        return null;
    }

    @Override
    public TypeSpec getSpec() {
        return (TypeSpec) super.getSpec();
    }

    public String getTypeName() {
        TypeDefinitionData data = getData();
        if (data != null) {
            return data.getTypeName();
        }
        return null;
    }

    @Override
    public String toString() {
        return get() + "{" + getName() + "}";
    }

    public static final TypeDefinitionIndex NO_TYPE = new TypeDefinitionIndex() {
        @Override
        public boolean isNull() {
            return true;
        }
        @Override
        public void link() {
        }
        @Override
        protected void onLinked(TypeDefinitionData data) {
        }
        @Override
        public void onIndexLinked(Object linker) {
        }
        @Override
        public void setData(TypeDefinitionData data) {
        }
        @Override
        public void setDataInternal(TypeDefinitionData data) {
        }
        @Override
        public void onReadBytes(BlockReader reader) {
        }
        @Override
        public int readBytes(InputStream inputStream) {
            return 0;
        }
        @Override
        protected int onWriteBytes(OutputStream stream) {
            return 0;
        }
        @Override
        public int get() {
            return -1;
        }
        @Override
        public void set(int value) {
        }
        @Override
        public String toString() {
            return "NO_TYPE";
        }
    };
}
