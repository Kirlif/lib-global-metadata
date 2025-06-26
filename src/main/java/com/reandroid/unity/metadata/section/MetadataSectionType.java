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

import com.reandroid.arsc.base.Creator;
import com.reandroid.unity.metadata.data.CustomAttributeData;
import com.reandroid.unity.metadata.data.*;
import com.reandroid.utils.StringsUtil;
import com.reandroid.utils.collection.ArrayIterator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MetadataSectionType<T extends SectionData> {

    public static final MetadataSectionType<StringLiteral> STRING_LITERAL;
    public static final MetadataSectionType<StringLiteralData> STRING_LITERAL_DATA;
    public static final MetadataSectionType<CodeStringData> CODE_STRING;
    public static final MetadataSectionType<EventDefinitionData> EVENTS;
    public static final MetadataSectionType<PropertyDefinitionData> PROPERTIES;
    public static final MetadataSectionType<MethodDefinitionData> METHODS;
    public static final MetadataSectionType<ParameterDefaultValueData> PARAMETER_DEFAULT_VALUES;
    public static final MetadataSectionType<FieldDefaultValueData> FIELD_DEFAULT_VALUES;
    public static final MetadataSectionType<BlobValueData> FIELD_AND_PARAMETER_DEFAULT_VALUE_DATA;
    public static final MetadataSectionType<FieldMarshaledSizeData> FIELD_MARSHALED_SIZE;
    public static final MetadataSectionType<ParameterDefinitionData> PARAMETERS;
    public static final MetadataSectionType<FieldDefinitionData> FIELDS;
    public static final MetadataSectionType<GenericParameterData> GENERIC_PARAMETERS;
    public static final MetadataSectionType<TypeEntryData> GENERIC_PARAMETER_CONSTRAINTS;
    public static final MetadataSectionType<GenericContainerData> GENERIC_CONTAINERS;
    public static final MetadataSectionType<TypeEntryData> NESTED_TYPES;
    public static final MetadataSectionType<TypeEntryData> INTERFACES;
    public static final MetadataSectionType<VtableMethodData> VTABLE_METHODS;
    public static final MetadataSectionType<InterfaceOffsetPairData> INTERFACE_OFFSETS;
    public static final MetadataSectionType<TypeDefinitionData> TYPE_DEFINITIONS;
    public static final MetadataSectionType<RGCTXDefinitionData> RGCTX_ENTRIES;
    public static final MetadataSectionType<ImageDefinitionData> IMAGES;
    public static final MetadataSectionType<AssemblyDefinitionData> ASSEMBLIES;
    public static final MetadataSectionType<MetadataUsageListData> METADATA_USAGE_LISTS;
    public static final MetadataSectionType<MetadataUsagePairData> METADATA_USAGE_PAIRS;
    public static final MetadataSectionType<FieldRefData> FIELD_REFS;
    public static final MetadataSectionType<AssemblyEntryData> REFERENCED_ASSEMBLIES;
    public static final MetadataSectionType<CustomAttributeInfoData> ATTRIBUTES_INFO;
    public static final MetadataSectionType<TypeEntryData> ATTRIBUTE_TYPES;
    public static final MetadataSectionType<CustomAttributeData> ATTRIBUTE_DATA;
    public static final MetadataSectionType<CustomAttributeDataRange> ATTRIBUTE_DATA_RANGE;
    public static final MetadataSectionType<TypeEntryData> UNRESOLVED_VIRTUAL_CALL_PARAMETER_TYPES;
    public static final MetadataSectionType<TypeRangeData> UNRESOLVED_VIRTUAL_CALL_PARAMETER_RANGES;
    public static final MetadataSectionType<WindowsRuntimeTypeNameData> WINDOWS_RUNTIME_TYPE_NAMES;
    public static final MetadataSectionType<SectionUnknown.Data> WINDOWS_RUNTIME_STRINGS;
    public static final MetadataSectionType<TypeEntryData> EXPORTED_TYPE_DEFINITIONS;

    private static final MetadataSectionType<?>[] VALUES;
    private static final MetadataSectionType<?>[] LOADING_ORDER;
    private static final Map<String, MetadataSectionType<?>> name_map;

    static {
        STRING_LITERAL = new MetadataSectionType<>("STRING_LITERAL",
                StringLiteral::new);
        STRING_LITERAL_DATA = new MetadataSectionType<>("STRING_LITERAL_DATA");
        CODE_STRING = new MetadataSectionType<>("CODE_STRING", CodeStringData::new);
        EVENTS = new MetadataSectionType<>("EVENTS", EventDefinitionData::new);
        PROPERTIES = new MetadataSectionType<>("PROPERTIES", PropertyDefinitionData::new);
        METHODS = new MetadataSectionType<>("METHODS", MethodDefinitionData::new);
        PARAMETER_DEFAULT_VALUES = new MetadataSectionType<>("PARAMETER_DEFAULT_VALUES",
                ParameterDefaultValueData::new);
        FIELD_DEFAULT_VALUES = new MetadataSectionType<>("FIELD_DEFAULT_VALUES",
                FieldDefaultValueData::new);
        FIELD_AND_PARAMETER_DEFAULT_VALUE_DATA = new MetadataSectionType<>(
                "FIELD_AND_PARAMETER_DEFAULT_VALUE_DATA", null);
        FIELD_MARSHALED_SIZE = new MetadataSectionType<>("FIELD_MARSHALED_SIZE",
                FieldMarshaledSizeData::new);
        PARAMETERS = new MetadataSectionType<>("PARAMETERS", ParameterDefinitionData::new);
        FIELDS = new MetadataSectionType<>("FIELDS", FieldDefinitionData::new);
        GENERIC_PARAMETERS = new MetadataSectionType<>("GENERIC_PARAMETERS", GenericParameterData::new);
        GENERIC_PARAMETER_CONSTRAINTS = new MetadataSectionType<>("GENERIC_PARAMETER_CONSTRAINTS",
                TypeEntryData.CREATOR);
        GENERIC_CONTAINERS = new MetadataSectionType<>("GENERIC_CONTAINERS", GenericContainerData::new);
        NESTED_TYPES = new MetadataSectionType<>("NESTED_TYPES", TypeEntryData.CREATOR);
        INTERFACES = new MetadataSectionType<>("INTERFACES", TypeEntryData.CREATOR);
        VTABLE_METHODS = new MetadataSectionType<>("VTABLE_METHODS", VtableMethodData::new);
        INTERFACE_OFFSETS = new MetadataSectionType<>("INTERFACE_OFFSETS", InterfaceOffsetPairData::new);
        TYPE_DEFINITIONS = new MetadataSectionType<>("TYPE_DEFINITIONS", TypeDefinitionData::new);
        RGCTX_ENTRIES = new MetadataSectionType<>("RGCTX_ENTRIES", RGCTXDefinitionData::new);
        IMAGES = new MetadataSectionType<>("IMAGES", ImageDefinitionData::new);
        ASSEMBLIES = new MetadataSectionType<>("ASSEMBLIES", AssemblyDefinitionData::new);
        METADATA_USAGE_LISTS = new MetadataSectionType<>("METADATA_USAGE_LISTS", MetadataUsageListData::new);
        METADATA_USAGE_PAIRS = new MetadataSectionType<>("METADATA_USAGE_PAIRS", MetadataUsagePairData::new);
        FIELD_REFS = new MetadataSectionType<>("FIELD_REFS", FieldRefData::new);
        REFERENCED_ASSEMBLIES = new MetadataSectionType<>("REFERENCED_ASSEMBLIES", AssemblyEntryData::new);
        ATTRIBUTES_INFO = new MetadataSectionType<>("ATTRIBUTES_INFO", CustomAttributeInfoData::new);
        ATTRIBUTE_TYPES = new MetadataSectionType<>("ATTRIBUTE_TYPES", TypeEntryData::new);
        ATTRIBUTE_DATA = new MetadataSectionType<>("ATTRIBUTE_DATA", CustomAttributeData::new);
        ATTRIBUTE_DATA_RANGE = new MetadataSectionType<>("ATTRIBUTE_DATA_RANGE",
                CustomAttributeDataRange::new);
        UNRESOLVED_VIRTUAL_CALL_PARAMETER_TYPES = new MetadataSectionType<>(
                "UNRESOLVED_VIRTUAL_CALL_PARAMETER_TYPES", TypeEntryData.CREATOR);
        UNRESOLVED_VIRTUAL_CALL_PARAMETER_RANGES = new MetadataSectionType<>(
                "UNRESOLVED_VIRTUAL_CALL_PARAMETER_RANGES", TypeRangeData::new);
        WINDOWS_RUNTIME_TYPE_NAMES = new MetadataSectionType<>(
                "WINDOWS_RUNTIME_TYPE_NAMES", WindowsRuntimeTypeNameData::new);
        WINDOWS_RUNTIME_STRINGS = new MetadataSectionType<>("WINDOWS_RUNTIME_STRINGS", null);
        EXPORTED_TYPE_DEFINITIONS = new MetadataSectionType<>("EXPORTED_TYPE_DEFINITIONS",
                TypeEntryData.CREATOR);

        VALUES = new MetadataSectionType[] {
                STRING_LITERAL,
                STRING_LITERAL_DATA,
                CODE_STRING,
                EVENTS,
                PROPERTIES,
                METHODS,
                PARAMETER_DEFAULT_VALUES,
                FIELD_DEFAULT_VALUES,
                FIELD_AND_PARAMETER_DEFAULT_VALUE_DATA,
                FIELD_MARSHALED_SIZE,
                PARAMETERS,
                FIELDS,
                GENERIC_PARAMETERS,
                GENERIC_PARAMETER_CONSTRAINTS,
                GENERIC_CONTAINERS,
                NESTED_TYPES,
                INTERFACES,
                VTABLE_METHODS,
                INTERFACE_OFFSETS,
                TYPE_DEFINITIONS,
                RGCTX_ENTRIES,
                IMAGES,
                ASSEMBLIES,
                METADATA_USAGE_LISTS,
                METADATA_USAGE_PAIRS,
                FIELD_REFS,
                REFERENCED_ASSEMBLIES,
                ATTRIBUTES_INFO,
                ATTRIBUTE_TYPES,
                ATTRIBUTE_DATA,
                ATTRIBUTE_DATA_RANGE,
                UNRESOLVED_VIRTUAL_CALL_PARAMETER_TYPES,
                UNRESOLVED_VIRTUAL_CALL_PARAMETER_RANGES,
                WINDOWS_RUNTIME_TYPE_NAMES,
                WINDOWS_RUNTIME_STRINGS,
                EXPORTED_TYPE_DEFINITIONS
        };
        Map<String, MetadataSectionType<?>> map = new HashMap<>();
        name_map = map;
        for (MetadataSectionType<?> type : VALUES) {
            map.put(type.name(), type);
        }

        LOADING_ORDER = new MetadataSectionType[] {
                STRING_LITERAL,
                STRING_LITERAL_DATA,
                CODE_STRING,
                EVENTS,
                PROPERTIES,
                METHODS,
                PARAMETER_DEFAULT_VALUES,
                FIELD_DEFAULT_VALUES,
                FIELD_AND_PARAMETER_DEFAULT_VALUE_DATA,
                FIELD_MARSHALED_SIZE,
                PARAMETERS,
                FIELDS,
                GENERIC_PARAMETERS,
                GENERIC_PARAMETER_CONSTRAINTS,
                GENERIC_CONTAINERS,
                NESTED_TYPES,
                INTERFACES,
                VTABLE_METHODS,
                INTERFACE_OFFSETS,
                TYPE_DEFINITIONS,
                RGCTX_ENTRIES,
                IMAGES,
                ASSEMBLIES,
                METADATA_USAGE_LISTS,
                METADATA_USAGE_PAIRS,
                FIELD_REFS,
                REFERENCED_ASSEMBLIES,
                ATTRIBUTE_DATA_RANGE,
                ATTRIBUTES_INFO,
                ATTRIBUTE_TYPES,
                ATTRIBUTE_DATA,
                UNRESOLVED_VIRTUAL_CALL_PARAMETER_TYPES,
                UNRESOLVED_VIRTUAL_CALL_PARAMETER_RANGES,
                WINDOWS_RUNTIME_TYPE_NAMES,
                WINDOWS_RUNTIME_STRINGS,
                EXPORTED_TYPE_DEFINITIONS
        };
    }

    private final String name;
    private final Creator<T> creator;

    private MetadataSectionType(String name, Creator<T> creator) {
        this.name = name;
        this.creator = creator;
    }
    private MetadataSectionType(String name) {
        this(name, null);
    }

    public String name() {
        return name;
    }

    public Creator<T> getCreator() {
        return creator;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name();
    }

    public static Iterator<MetadataSectionType<?>> getLoadingOrder() {
        return ArrayIterator.of(LOADING_ORDER);
    }

    public static MetadataSectionType<?> forName(String name) {
        return name_map.get(StringsUtil.toUpperCase(name));
    }

}
