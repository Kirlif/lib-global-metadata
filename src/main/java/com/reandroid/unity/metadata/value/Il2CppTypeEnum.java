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

import com.reandroid.utils.HexUtil;
import com.reandroid.utils.StringsUtil;

import java.util.HashMap;
import java.util.Map;

public class Il2CppTypeEnum {

    public static final Il2CppTypeEnum END;
    public static final Il2CppTypeEnum VOID;
    public static final Il2CppTypeEnum BOOLEAN;
    public static final Il2CppTypeEnum CHAR;
    public static final Il2CppTypeEnum I1;
    public static final Il2CppTypeEnum U1;
    public static final Il2CppTypeEnum I2;
    public static final Il2CppTypeEnum U2;
    public static final Il2CppTypeEnum I4;
    public static final Il2CppTypeEnum U4;
    public static final Il2CppTypeEnum I8;
    public static final Il2CppTypeEnum U8;
    public static final Il2CppTypeEnum R4;
    public static final Il2CppTypeEnum R8;
    public static final Il2CppTypeEnum STRING;
    public static final Il2CppTypeEnum PTR;
    public static final Il2CppTypeEnum BYREF;
    public static final Il2CppTypeEnum VALUETYPE;
    public static final Il2CppTypeEnum CLASS;
    public static final Il2CppTypeEnum VAR;
    public static final Il2CppTypeEnum ARRAY;
    public static final Il2CppTypeEnum GENERICINST;
    public static final Il2CppTypeEnum TYPEDBYREF;
    public static final Il2CppTypeEnum I;
    public static final Il2CppTypeEnum U;
    public static final Il2CppTypeEnum FNPTR;
    public static final Il2CppTypeEnum OBJECT;
    public static final Il2CppTypeEnum SZARRAY;
    public static final Il2CppTypeEnum MVAR;
    public static final Il2CppTypeEnum CMOD_REQD;
    public static final Il2CppTypeEnum CMOD_OPT;
    public static final Il2CppTypeEnum INTERNAL;
    public static final Il2CppTypeEnum MODIFIER;
    public static final Il2CppTypeEnum SENTINEL;
    public static final Il2CppTypeEnum PINNED;
    public static final Il2CppTypeEnum ENUM;
    public static final Il2CppTypeEnum TYPE_INDEX;

    public static final Il2CppTypeEnum UNKNOWN;

    private static final Il2CppTypeEnum[] VALUES_1;
    private static final Il2CppTypeEnum[] VALUES_2;

    private static final Map<String, Il2CppTypeEnum> name_map;
    private static final Map<String, Il2CppTypeEnum> system_type_map;

    static {
        END = new Il2CppTypeEnum("END", 0x00);
        VOID = new Il2CppTypeEnum("VOID", 0x01, "Void");
        BOOLEAN = new Il2CppTypeEnum("BOOLEAN", 0x02, "Boolean");
        CHAR = new Il2CppTypeEnum("CHAR", 0x03, "Char");
        I1 = new Il2CppTypeEnum("I1", 0x04, "Byte");
        U1 = new Il2CppTypeEnum("U1", 0x05, "SByte");
        I2 = new Il2CppTypeEnum("I2", 0x06, "Int16");
        U2 = new Il2CppTypeEnum("U2", 0x07, "UInt16");
        I4 = new Il2CppTypeEnum("I4", 0x08, "Int32");
        U4 = new Il2CppTypeEnum("U4", 0x09, "UInt32");
        I8 = new Il2CppTypeEnum("I8", 0x0a, "Int64");
        U8 = new Il2CppTypeEnum("U8", 0x0b, "UInt64");
        R4 = new Il2CppTypeEnum("R4", 0x0c, "Single");
        R8 = new Il2CppTypeEnum("R8", 0x0d, "Double");
        STRING = new Il2CppTypeEnum("STRING", 0x0e, "String");
        PTR = new Il2CppTypeEnum("PTR", 0x0f);
        BYREF = new Il2CppTypeEnum("BYREF", 0x10);
        VALUETYPE = new Il2CppTypeEnum("VALUETYPE", 0x11);
        CLASS = new Il2CppTypeEnum("CLASS", 0x12);
        VAR = new Il2CppTypeEnum("VAR", 0x13);
        ARRAY = new Il2CppTypeEnum("ARRAY", 0x14);
        GENERICINST = new Il2CppTypeEnum("GENERICINST", 0x15);
        TYPEDBYREF = new Il2CppTypeEnum("TYPEDBYREF", 0x16);
        I = new Il2CppTypeEnum("I", 0x18);
        U = new Il2CppTypeEnum("U", 0x19);
        FNPTR = new Il2CppTypeEnum("FNPTR", 0x1b);
        OBJECT = new Il2CppTypeEnum("OBJECT", 0x1c);
        SZARRAY = new Il2CppTypeEnum("SZARRAY", 0x1d);
        MVAR = new Il2CppTypeEnum("MVAR", 0x1e);
        CMOD_REQD = new Il2CppTypeEnum("CMOD_REQD", 0x1f);
        CMOD_OPT = new Il2CppTypeEnum("CMOD_OPT", 0x20);
        INTERNAL = new Il2CppTypeEnum("INTERNAL", 0x21);
        MODIFIER = new Il2CppTypeEnum("MODIFIER", 0x40);
        SENTINEL = new Il2CppTypeEnum("SENTINEL", 0x41);
        PINNED = new Il2CppTypeEnum("PINNED", 0x45);
        ENUM = new Il2CppTypeEnum("ENUM", 0x55, "Enum");
        TYPE_INDEX = new Il2CppTypeEnum("TYPE_INDEX", 0xff);

        UNKNOWN = new Il2CppTypeEnum("UNKNOWN", 0xffffffff);

        VALUES_1 = new Il2CppTypeEnum[] {
                END,
                VOID,
                BOOLEAN,
                CHAR,
                I1,
                U1,
                I2,
                U2,
                I4,
                U4,
                I8,
                U8,
                R4,
                R8,
                STRING,
                PTR,
                BYREF,
                VALUETYPE,
                CLASS,
                VAR,
                ARRAY,
                GENERICINST,
                TYPEDBYREF,
                I,
                null,
                U,
                null,
                FNPTR,
                OBJECT,
                SZARRAY,
                MVAR,
                CMOD_REQD,
                CMOD_OPT,
                INTERNAL
        };
        VALUES_2 = new Il2CppTypeEnum[] {
                MODIFIER,
                SENTINEL,
                PINNED,
                ENUM,
                TYPE_INDEX
        };
        Map<String, Il2CppTypeEnum> nameMap = new HashMap<>();
        name_map = nameMap;
        Map<String, Il2CppTypeEnum> typeMap = new HashMap<>();
        system_type_map = typeMap;
        for (Il2CppTypeEnum typeEnum : VALUES_1) {
            if (typeEnum != null) {
                nameMap.put(typeEnum.name, typeEnum);
                String type = typeEnum.systemType;
                if (type != null) {
                    typeMap.put(type, typeEnum);
                }
            }
        }
        for (Il2CppTypeEnum typeEnum : VALUES_2) {
            if (typeEnum != null) {
                nameMap.put(typeEnum.name, typeEnum);
                String type = typeEnum.systemType;
                if (type != null) {
                    typeMap.put(type, typeEnum);
                }
            }
        }
        nameMap.put(UNKNOWN.name, UNKNOWN);
    }

    private final String name;
    private final int value;

    private final String systemType;

    private Il2CppTypeEnum(String name, int value, String systemType) {
        this.name = name;
        this.value = value;
        this.systemType = systemType;
    }
    private Il2CppTypeEnum(String name, int value) {
        this(name, value, null);
    }

    public String name() {
        return name;
    }
    public int value() {
        return value;
    }

    public String systemType() {
        return systemType(false);
    }
    public String systemType(boolean appendNamespace) {
        String type = this.systemType;
        if (appendNamespace) {
            type = "System." + type;
        }
        return type;
    }

    @Override
    public String toString() {
        return name() + "(" + HexUtil.toHex(value(), 2) + ")";
    }

    public static Il2CppTypeEnum valueOf(int type) {
        if (type >= 0 && type < VALUES_1.length) {
            return VALUES_1[type];
        }
        for (Il2CppTypeEnum typeEnum : VALUES_2) {
            if (typeEnum.value() == type) {
                return typeEnum;
            }
        }
        return null;
    }
    public static Il2CppTypeEnum valueOf(String name) {
        return name_map.get(name);
    }

    public static Il2CppTypeEnum valueOfSystemType(String namespace, String type) {
        if (!StringsUtil.isEmpty(namespace) && !namespace.equals("System")) {
            return null;
        }
        return valueOfSystemType(type);
    }
    public static Il2CppTypeEnum valueOfSystemType(String type) {
        if (type == null) {
            return null;
        }
        int i = type.lastIndexOf('.');
        if (i > 0) {
            String namespace = type.substring(0, i);
            if (!namespace.equals("System")) {
                return null;
            }
            type = type.substring(i + 1);
        }
        return system_type_map.get(type);
    }
}
