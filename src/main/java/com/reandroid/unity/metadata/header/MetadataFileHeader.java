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
package com.reandroid.unity.metadata.header;

import com.reandroid.arsc.base.Block;
import com.reandroid.arsc.base.DirectStreamReader;
import com.reandroid.arsc.container.FixedBlockContainer;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.json.JSONConvert;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.VersionRange;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.utils.collection.ArrayIterator;
import com.reandroid.utils.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class MetadataFileHeader extends FixedBlockContainer
        implements DirectStreamReader, JSONConvert<JSONObject> {

    public final MetadataFileMagic magic = new MetadataFileMagic();
    public final MetadataFileVersion version = new MetadataFileVersion();

    public final MetadataSectionHeader stringLiteral = new MetadataSectionHeader(MetadataSectionType.STRING_LITERAL);
    public final MetadataSectionHeader stringLiteralData = new MetadataSectionHeader(MetadataSectionType.STRING_LITERAL_DATA);
    public final MetadataSectionHeader codeString = new MetadataSectionHeader(MetadataSectionType.CODE_STRING);
    public final MetadataSectionHeader events = new MetadataSectionHeader(MetadataSectionType.EVENTS);
    public final MetadataSectionHeader properties = new MetadataSectionHeader(MetadataSectionType.PROPERTIES);
    public final MetadataSectionHeader methods = new MetadataSectionHeader(MetadataSectionType.METHODS);
    public final MetadataSectionHeader parameterDefaultValues = new MetadataSectionHeader(MetadataSectionType.PARAMETER_DEFAULT_VALUES);
    public final MetadataSectionHeader fieldDefaultValues = new MetadataSectionHeader(MetadataSectionType.FIELD_DEFAULT_VALUES);
    public final MetadataSectionHeader fieldAndParameterDefaultValueData = new MetadataSectionHeader(MetadataSectionType.FIELD_AND_PARAMETER_DEFAULT_VALUE_DATA);
    public final MetadataSectionHeader fieldMarshaledSize = new MetadataSectionHeader(MetadataSectionType.FIELD_MARSHALED_SIZE);
    public final MetadataSectionHeader parameters = new MetadataSectionHeader(MetadataSectionType.PARAMETERS);
    public final MetadataSectionHeader fields = new MetadataSectionHeader(MetadataSectionType.FIELDS);
    public final MetadataSectionHeader genericParameters = new MetadataSectionHeader(MetadataSectionType.GENERIC_PARAMETERS);
    public final MetadataSectionHeader genericParameterConstraints = new MetadataSectionHeader(MetadataSectionType.GENERIC_PARAMETER_CONSTRAINTS);
    public final MetadataSectionHeader genericContainers = new MetadataSectionHeader(MetadataSectionType.GENERIC_CONTAINERS);
    public final MetadataSectionHeader nestedTypes = new MetadataSectionHeader(MetadataSectionType.NESTED_TYPES);
    public final MetadataSectionHeader interfaces = new MetadataSectionHeader(MetadataSectionType.INTERFACES);
    public final MetadataSectionHeader vtableMethods = new MetadataSectionHeader(MetadataSectionType.VTABLE_METHODS);
    public final MetadataSectionHeader interfaceOffsets = new MetadataSectionHeader(MetadataSectionType.INTERFACE_OFFSETS);
    public final MetadataSectionHeader typeDefinitions = new MetadataSectionHeader(MetadataSectionType.TYPE_DEFINITIONS);
    public final MetadataSectionHeader rgctxEntries = new MetadataSectionHeader(MetadataSectionType.RGCTX_ENTRIES,
            new VersionRange(null, 24.1));
    public final MetadataSectionHeader images = new MetadataSectionHeader(MetadataSectionType.IMAGES);
    public final MetadataSectionHeader assemblies = new MetadataSectionHeader(MetadataSectionType.ASSEMBLIES);
    public final MetadataSectionHeader metadataUsageLists = new MetadataSectionHeader(MetadataSectionType.METADATA_USAGE_LISTS,
            new VersionRange(19.0, 24.5));
    public final MetadataSectionHeader metadataUsagePairs = new MetadataSectionHeader(MetadataSectionType.METADATA_USAGE_PAIRS,
            new VersionRange(19.0, 24.5));
    public final MetadataSectionHeader fieldRefs = new MetadataSectionHeader(MetadataSectionType.FIELD_REFS,
            new VersionRange(19.0, null));
    public final MetadataSectionHeader referencedAssemblies = new MetadataSectionHeader(MetadataSectionType.REFERENCED_ASSEMBLIES,
            new VersionRange(20.0, null));
    public final MetadataSectionHeader attributesInfo = new MetadataSectionHeader(MetadataSectionType.ATTRIBUTES_INFO,
            new VersionRange(21.0, 27.2));
    public final MetadataSectionHeader attributeTypes = new MetadataSectionHeader(MetadataSectionType.ATTRIBUTE_TYPES,
            new VersionRange(21.0, 27.2));
    public final MetadataSectionHeader attributeData = new MetadataSectionHeader(MetadataSectionType.ATTRIBUTE_DATA,
            new VersionRange(29.0, null));
    public final MetadataSectionHeader attributeDataRange = new MetadataSectionHeader(MetadataSectionType.ATTRIBUTE_DATA_RANGE,
            new VersionRange(29.0, null));
    public final MetadataSectionHeader unresolvedVirtualCallParameterTypes = new MetadataSectionHeader(MetadataSectionType.UNRESOLVED_VIRTUAL_CALL_PARAMETER_TYPES,
            new VersionRange(22.0, null));
    public final MetadataSectionHeader unresolvedVirtualCallParameterRanges = new MetadataSectionHeader(MetadataSectionType.UNRESOLVED_VIRTUAL_CALL_PARAMETER_RANGES,
            new VersionRange(22.0, null));
    public final MetadataSectionHeader windowsRuntimeTypeNames = new MetadataSectionHeader(MetadataSectionType.WINDOWS_RUNTIME_TYPE_NAMES,
            new VersionRange(23.0, null));
    public final MetadataSectionHeader windowsRuntimeStrings = new MetadataSectionHeader(MetadataSectionType.WINDOWS_RUNTIME_STRINGS,
            new VersionRange(27.0, null));
    public final MetadataSectionHeader exportedTypeDefinitions = new MetadataSectionHeader(MetadataSectionType.EXPORTED_TYPE_DEFINITIONS,
            new VersionRange(24.0, null));

    private final MetadataSectionHeader[] offsetSizes;

    public MetadataFileHeader() {
        super(38);
        int index = 0;
        addChild(index++, magic);
        addChild(index++, version);
        MetadataSectionHeader[] elements = new MetadataSectionHeader[] {
                stringLiteral,
                stringLiteralData,
                codeString,
                events,
                properties,
                methods,
                parameterDefaultValues,
                fieldDefaultValues,
                fieldAndParameterDefaultValueData,
                fieldMarshaledSize,
                parameters,
                fields,
                genericParameters,
                genericParameterConstraints,
                genericContainers,
                nestedTypes,
                interfaces,
                vtableMethods,
                interfaceOffsets,
                typeDefinitions,
                rgctxEntries,
                images,
                assemblies,
                metadataUsageLists,
                metadataUsagePairs,
                fieldRefs,
                referencedAssemblies,
                attributesInfo,
                attributeTypes,
                attributeData,
                attributeDataRange,
                unresolvedVirtualCallParameterTypes,
                unresolvedVirtualCallParameterRanges,
                windowsRuntimeTypeNames,
                windowsRuntimeStrings,
                exportedTypeDefinitions
        };
        for (int i = 0; i < elements.length; i++) {
            MetadataSectionHeader item = elements[i];
            addChild(index, item);
            index++;
        }
        this.offsetSizes = elements;
    }

    public double getFileVersion() {
        return version.version();
    }
    public Iterator<MetadataSectionHeader> iterator() {
        return new ArrayIterator<>(offsetSizes);
    }
    public Iterator<MetadataSectionHeader> iterator(int start) {
        return new ArrayIterator<>(offsetSizes, start, offsetSizes.length - start);
    }
    public int computeFileSize() {
        int result = 0;
        Iterator<MetadataSectionHeader> iterator = this.iterator();
        while (iterator.hasNext()) {
            MetadataSectionHeader item = iterator.next();
            int size = item.getOffset() + item.getSize();
            if (size > result) {
                result = size;
            }
        }
        return result;
    }

    @Override
    public int readBytes(InputStream inputStream) throws IOException {
        Block[] childes = getChildes();
        int length = childes.length;
        int read = 0;
        for (int i = 0; i < length; i ++) {
            Block block = childes[i];
            if (block instanceof DirectStreamReader) {
                read += ((DirectStreamReader) block).readBytes(inputStream);
            }
        }
        return read;
    }
    @Override
    public void fromJson(JSONObject json) {
        magic.set(json.getInt("magic"));
        version.set(json.getInt("version"));
    }
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("magic", magic.get());
        jsonObject.put("version", version.get());

        jsonObject.put("file_size_computed", computeFileSize());

        Iterator<MetadataSectionHeader> iterator = this.iterator();
        while (iterator.hasNext()) {
            MetadataSectionHeader item = iterator.next();
            jsonObject.put(item.getSectionType().name(), item.getJson());
        }
        return jsonObject;
    }

    @Override
    public String toString() {
        return toJson().toString(2);
    }

    public static MetadataFileHeader read(File file) throws IOException {
        InputStream inputStream = FileUtil.inputStream(file);
        MetadataFileHeader header = read(inputStream);
        inputStream.close();
        return header;
    }
    public static MetadataFileHeader read(InputStream inputStream) throws IOException {
        MetadataFileHeader header = new MetadataFileHeader();
        header.readBytes(inputStream);
        return header;
    }
    public static MetadataFileHeader read(BlockReader reader) throws IOException {
        MetadataFileHeader header = new MetadataFileHeader();
        header.readBytes(reader);
        return header;
    }

}
