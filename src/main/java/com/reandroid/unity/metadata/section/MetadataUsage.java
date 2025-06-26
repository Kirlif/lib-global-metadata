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
package com.reandroid.unity.metadata.section;

public class MetadataUsage {

    public static final MetadataUsage Invalid;
    public static final MetadataUsage TypeInfo;
    public static final MetadataUsage Il2CppType;
    public static final MetadataUsage MethodDef;
    public static final MetadataUsage FieldInfo;
    public static final MetadataUsage StringLiteral;
    public static final MetadataUsage MethodRef;

    private static final MetadataUsage[] VALUES;

    static {
        Invalid = new MetadataUsage("Invalid", 0, null);
        TypeInfo = new MetadataUsage("TypeInfo", 1,
                MetadataSectionType.TYPE_DEFINITIONS);
        Il2CppType = new MetadataUsage("Il2CppType", 2, null);
        MethodDef = new MetadataUsage("MethodDef", 3, MetadataSectionType.METHODS);
        FieldInfo = new MetadataUsage("FieldInfo", 4, MetadataSectionType.FIELDS);
        StringLiteral = new MetadataUsage("StringLiteral", 5,
                MetadataSectionType.STRING_LITERAL_DATA);
        MethodRef = new MetadataUsage("MethodRef", 6, MetadataSectionType.METHODS);

        VALUES = new MetadataUsage[] {
                Invalid,
                TypeInfo,
                Il2CppType,
                MethodDef,
                FieldInfo,
                StringLiteral,
                MethodRef
        };
    }

    private final String name;
    private final int value;
    private final MetadataSectionType<?> sectionType;

    private MetadataUsage(String name, int value, MetadataSectionType<?> sectionType) {
        this.name = name;
        this.value = value;
        this.sectionType = sectionType;
    }

    public MetadataSectionType<?> getSectionType() {
        return sectionType;
    }

    public String name() {
        return name;
    }
    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }

    public static MetadataUsage valueOf(int i) {
        if (i >= 0 && i < VALUES.length) {
            return VALUES[i];
        }
        return null;
    }
    public static MetadataSectionType<?> getSectionTypeForUsage(int usage) {
        MetadataUsage metadataUsage = valueOf(usage);
        if (metadataUsage != null) {
            return metadataUsage.getSectionType();
        }
        return null;
    }
}
