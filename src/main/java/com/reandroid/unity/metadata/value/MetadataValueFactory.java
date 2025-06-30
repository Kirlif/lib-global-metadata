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
package com.reandroid.unity.metadata.value;

import com.reandroid.arsc.io.BlockReader;

import java.io.IOException;

public class MetadataValueFactory {

    public static MetadataValue read(BlockReader reader) throws IOException {
        MetadataValue value = forType(reader.read());
        reader.offset(-1);
        return value;
    }
    public static MetadataValue forType(int type) {
        return forType(Il2CppTypeEnum.valueOf(type));
    }
    public static MetadataValue forType(Il2CppTypeEnum typeEnum) {
        if (typeEnum == null) {
            return null;
        }
        if (typeEnum == Il2CppTypeEnum.END) {
            return new ValueEnd();
        }
        if (typeEnum == Il2CppTypeEnum.VOID) {
            return new ValueVoid();
        }
        if (typeEnum == Il2CppTypeEnum.BOOLEAN) {
            return new ValueBoolean();
        }
        if (typeEnum == Il2CppTypeEnum.CHAR) {
            return new ValueChar();
        }
        if (typeEnum == Il2CppTypeEnum.I1) {
            return new ValueI1();
        }
        if (typeEnum == Il2CppTypeEnum.U1) {
            return new ValueU1();
        }
        if (typeEnum == Il2CppTypeEnum.I2) {
            return new ValueI2();
        }
        if (typeEnum == Il2CppTypeEnum.U2) {
            return new ValueU2();
        }
        if (typeEnum == Il2CppTypeEnum.I4) {
            return new ValueI4();
        }
        if (typeEnum == Il2CppTypeEnum.U4) {
            return new ValueU4();
        }
        if (typeEnum == Il2CppTypeEnum.I8) {
            return new ValueI8();
        }
        if (typeEnum == Il2CppTypeEnum.U8) {
            return new ValueU8();
        }
        if (typeEnum == Il2CppTypeEnum.R4) {
            return new ValueR4();
        }
        if (typeEnum == Il2CppTypeEnum.R8) {
            return new ValueR8();
        }
        if (typeEnum == Il2CppTypeEnum.STRING) {
            return new ValueString();
        }
        if (typeEnum == Il2CppTypeEnum.PTR) {
            return new ValuePtr();
        }
        if (typeEnum == Il2CppTypeEnum.BYREF) {
            return new ValueByref();
        }
        if (typeEnum == Il2CppTypeEnum.VALUETYPE) {
            return new ValueValuetype();
        }
        if (typeEnum == Il2CppTypeEnum.CLASS) {
            return new ValueClass();
        }
        if (typeEnum == Il2CppTypeEnum.VAR) {
            return new ValueVar();
        }
        if (typeEnum == Il2CppTypeEnum.ARRAY) {
            return new ValueArray();
        }
        if (typeEnum == Il2CppTypeEnum.GENERICINST) {
            return new ValueGenericinst();
        }
        if (typeEnum == Il2CppTypeEnum.TYPEDBYREF) {
            return new ValueTypedbyref();
        }
        if (typeEnum == Il2CppTypeEnum.I) {
            return new ValueI();
        }
        if (typeEnum == Il2CppTypeEnum.U) {
            return new ValueU();
        }
        if (typeEnum == Il2CppTypeEnum.FNPTR) {
            return new ValueFnptr();
        }
        if (typeEnum == Il2CppTypeEnum.OBJECT) {
            return new ValueObject();
        }
        if (typeEnum == Il2CppTypeEnum.SZARRAY) {
            return new ValueSZArray();
        }
        if (typeEnum == Il2CppTypeEnum.MVAR) {
            return new ValueMvar();
        }
        if (typeEnum == Il2CppTypeEnum.CMOD_REQD) {
            return new ValueCmodReqd();
        }
        if (typeEnum == Il2CppTypeEnum.CMOD_OPT) {
            return new ValueCmodOpt();
        }
        if (typeEnum == Il2CppTypeEnum.INTERNAL) {
            return new ValueInternal();
        }
        if (typeEnum == Il2CppTypeEnum.MODIFIER) {
            return new ValueModifier();
        }
        if (typeEnum == Il2CppTypeEnum.SENTINEL) {
            return new ValueSentinel();
        }
        if (typeEnum == Il2CppTypeEnum.PINNED) {
            return new ValuePinned();
        }
        if (typeEnum == Il2CppTypeEnum.ENUM) {
            return new ValueEnum();
        }
        if (typeEnum == Il2CppTypeEnum.TYPE_INDEX) {
            return new ValueTypeIndex();
        }
        if (typeEnum == Il2CppTypeEnum.StaticArrayInitType) {
            return createValueStaticArrayInitType();
        }
        if (typeEnum == Il2CppTypeEnum.UNKNOWN) {
            return createUnknown();
        }
        throw new IllegalStateException("Unimplemented type: " + typeEnum);
    }
    public static ValueUnknown createUnknown() {
        return new ValueUnknown();
    }
    public static ValueStaticArrayInitType createValueStaticArrayInitType() {
        return new ValueStaticArrayInitType();
    }
}
