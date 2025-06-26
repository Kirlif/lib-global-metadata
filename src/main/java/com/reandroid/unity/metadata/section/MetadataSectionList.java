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

import com.reandroid.arsc.container.BlockList;
import com.reandroid.arsc.container.FixedBlockContainer;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.AlignItem;
import com.reandroid.json.JSONArray;
import com.reandroid.json.JSONConvert;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.LinkableItem;
import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.unity.metadata.header.MetadataFileHeader;
import com.reandroid.utils.CompareUtil;
import com.reandroid.utils.ObjectsUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MetadataSectionList extends FixedBlockContainer implements
        LinkableItem, Iterable<MetadataSection<?>>, JSONConvert<JSONArray> {

    private final MetadataFileHeader header;
    private final BlockList<MetadataSection<?>> sectionList;
    private final AlignItem alignment;

    private final Map<MetadataSectionType<?>, MetadataSection<?>> sectionTypeMap;

    public MetadataSectionList(MetadataFileHeader header) {
        super(2);
        this.header = header;
        BlockList<MetadataSection<?>> sectionList = new BlockList<>();
        this.sectionList = sectionList;
        this.alignment = new AlignItem();

        addChild(0, sectionList);
        addChild(1, alignment);

        Map<MetadataSectionType<?>, MetadataSection<?>> sectionTypeMap = new HashMap<>();
        this.sectionTypeMap = sectionTypeMap;

        SectionStringLiteral stringLiteral = new SectionStringLiteral(header.stringLiteral);
        sectionList.add(stringLiteral);
        SecStringLiteralData secStringLiteralData = new SecStringLiteralData(
                header.stringLiteralData, stringLiteral);
        sectionList.add(secStringLiteralData);

        sectionList.add(new SectionCodeString(header.codeString));

        sectionList.add(new MetadataSection<>(header.events));
        sectionList.add(new MetadataSection<>(header.properties));
        sectionList.add(new MetadataSection<>(header.methods));
        sectionList.add(new MetadataSection<>(header.parameterDefaultValues));
        sectionList.add(new MetadataSection<>(header.fieldDefaultValues));
        sectionList.add(new SecFieldAndParameterDefaultValueData(header.fieldAndParameterDefaultValueData));
        sectionList.add(new MetadataSection<>(header.fieldMarshaledSize));
        sectionList.add(new MetadataSection<>(header.parameters));
        sectionList.add(new MetadataSection<>(header.fields));
        sectionList.add(new MetadataSection<>(header.genericParameters));
        sectionList.add(new MetadataSection<>(header.genericParameterConstraints));
        sectionList.add(new MetadataSection<>(header.genericContainers));
        sectionList.add(new MetadataSection<>(header.nestedTypes));
        sectionList.add(new MetadataSection<>(header.interfaces));
        sectionList.add(new MetadataSection<>(header.vtableMethods));
        sectionList.add(new MetadataSection<>(header.interfaceOffsets));
        sectionList.add(new SectionTypeDefinition(header.typeDefinitions));
        sectionList.add(new MetadataSection<>(header.rgctxEntries));
        sectionList.add(new MetadataSection<>(header.images));
        sectionList.add(new MetadataSection<>(header.assemblies));
        sectionList.add(new MetadataSection<>(header.metadataUsageLists));
        sectionList.add(new MetadataSection<>(header.metadataUsagePairs));
        sectionList.add(new MetadataSection<>(header.fieldRefs));
        sectionList.add(new MetadataSection<>(header.referencedAssemblies));
        sectionList.add(new MetadataSection<>(header.attributeDataRange));
        sectionList.add(new MetadataSection<>(header.attributeTypes));
        sectionList.add(new SectionCustomAttributeData(header.attributeData));
        sectionList.add(new MetadataSection<>(header.attributesInfo));
        sectionList.add(new MetadataSection<>(header.unresolvedVirtualCallParameterTypes));
        sectionList.add(new MetadataSection<>(header.unresolvedVirtualCallParameterRanges));
        sectionList.add(new MetadataSection<>(header.windowsRuntimeTypeNames));
        sectionList.add(new SectionUnknown(header.windowsRuntimeStrings));
        sectionList.add(new MetadataSection<>(header.exportedTypeDefinitions));

        int size = sectionList.size();
        for (int i = 0; i < size; i++) {
            MetadataSection<?> section = sectionList.get(i);
            sectionTypeMap.put(section.getSectionType(), section);
        }
    }

    @Override
    public Iterator<MetadataSection<?>> iterator() {
        return sectionList.iterator();
    }
    public MetadataSection<?> get(int i) {
        return sectionList.get(i);
    }
    public int size() {
        return sectionList.size();
    }

    public<T extends SectionData> MetadataSection<T> getSection(MetadataSectionType<? extends T> sectionType) {
        return ObjectsUtil.cast(sectionTypeMap.get(sectionType));
    }

    public MetadataFileHeader getHeader() {
        return header;
    }

    public double getVersion() {
        return getHeader().version.version();
    }
    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        sortSectionsByOffset();
        Iterator<MetadataSectionType<?>> iterator = MetadataSectionType.getLoadingOrder();
        while (iterator.hasNext()) {
            getSection(iterator.next()).readBytes(reader);
        }
        link();
    }
    private void sortSectionsByOffset() {
        sectionList.sort((section1, section2) ->
                CompareUtil.compare(section1.getOffset(), section2.getOffset()));
    }
    @Override
    protected void onRefreshed() {
        sortSectionsByOffset();
        super.onRefreshed();
        refreshOffsets();
    }
    private void refreshOffsets() {
        int offset = header.countBytes();
        int size = this.size();
        for (int i = 0; i < size; i++) {
            offset = get(i).setOffset(offset);
        }
        alignment.align(offset);
    }

    @Override
    public void link() {
        for (int i = 0; i < LINK_DEPTH; i++) {
            LinkableItem.linkBlockList(sectionList);
        }
        for (MetadataSection<?> section : this) {
            section.notifyLinkCompleted();
        }
    }

    @Override
    public void fromJson(JSONArray json) {
        int length = json.length();
        for (int i = 0; i < length; i++) {
            JSONObject jsonObject = json.optJSONObject(i);
            if (jsonObject == null) {
                continue;
            }
            String name = jsonObject.getString("name");
            MetadataSection<?> section = getSection(MetadataSectionType.forName(name));
            section.fromJson(jsonObject);
        }
    }
    @Override
    public JSONArray toJson() {
        JSONArray jsonArray = new JSONArray();
        int size = this.size();
        for (int i = 0; i < size; i++) {
            MetadataSection<?> section = get(i);
            JSONObject jsonObject = section.toJson();
            if (jsonObject == null) {
                continue;
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
    public void dumpJsons(File dir) throws IOException {
        for (MetadataSection<?> section : this) {
            if (!section.hasValidVersion()) {
                continue;
            }
            JSONObject jsonObject = section.toJson();
            if (jsonObject == null) {
                continue;
            }
            File file = new File(dir, section.getSectionType().name() + ".json");
            jsonObject.write(file);
        }
    }

    private static final int LINK_DEPTH = 3;
}
