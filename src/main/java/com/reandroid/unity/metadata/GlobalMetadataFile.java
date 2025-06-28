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
package com.reandroid.unity.metadata;

import com.reandroid.arsc.container.FixedBlockContainer;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.common.BytesOutputStream;
import com.reandroid.json.JSONConvert;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.data.BlobValueData;
import com.reandroid.unity.metadata.data.CustomAttributeData;
import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.unity.metadata.data.StringLiteralData;
import com.reandroid.unity.metadata.header.MetadataFileHeader;
import com.reandroid.unity.metadata.section.MetadataSection;
import com.reandroid.unity.metadata.section.MetadataSectionList;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.section.SectionCodeString;
import com.reandroid.unity.metadata.section.SectionStringData;
import com.reandroid.unity.metadata.value.MetadataValue;
import com.reandroid.utils.ObjectsUtil;
import com.reandroid.utils.collection.CombiningIterator;
import com.reandroid.utils.collection.ComputeIterator;
import com.reandroid.utils.collection.InstanceIterator;
import com.reandroid.utils.collection.IterableIterator;
import com.reandroid.utils.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

public class GlobalMetadataFile extends FixedBlockContainer implements JSONConvert<JSONObject> {

    private final MetadataFileHeader header;
    private final MetadataSectionList sectionList;

    public GlobalMetadataFile() {
        super(2);

        MetadataFileHeader header = new MetadataFileHeader();
        this.header = header;
        this.sectionList = new MetadataSectionList(header);

        addChild(0, header);
        addChild(1, this.sectionList);
    }
    public double getVersion() {
        return getHeader().version.version();
    }
    public MetadataFileHeader getHeader() {
        return header;
    }
    public MetadataSectionList getSectionList() {
        return sectionList;
    }

    public SectionStringData<StringLiteralData> getLiteralStringsData() {
        return ObjectsUtil.cast(getSection(MetadataSectionType.STRING_LITERAL_DATA));
    }
    public SectionCodeString getCodeStringsData() {
        return ObjectsUtil.cast(getSection(MetadataSectionType.CODE_STRING));
    }
    public<T extends SectionData> MetadataSection<T> getSection(MetadataSectionType<T> sectionType) {
        return getSectionList().getSection(sectionType);
    }
    public<T extends SectionData> Iterator<T> getItems(MetadataSectionType<T> sectionClass) {
        return getSection(sectionClass).iterator();
    }
    public<T extends MetadataValue> Iterator<T> visitValues(Class<T> valueClass) {
        return InstanceIterator.of(visitValues(), valueClass);
    }
    public Iterator<MetadataValue> visitValues() {
        Iterator<MetadataValue> attributes = new IterableIterator<CustomAttributeData, MetadataValue>(
                getSection(MetadataSectionType.ATTRIBUTE_DATA).iterator()) {
            @Override
            public Iterator<MetadataValue> iterator(CustomAttributeData element) {
                return element.visitValues();
            }
        };
        Iterator<MetadataValue> blobs = ComputeIterator.of(
                getSection(MetadataSectionType.FIELD_AND_PARAMETER_DEFAULT_VALUE_DATA).iterator(),
                BlobValueData::getValue);
        return CombiningIterator.two(attributes, blobs);
    }

    @Override
    protected void onRefreshed() {
        super.onRefreshed();
        getSectionList().refresh();
    }

    @Override
    public byte[] getBytes() {
        BytesOutputStream outputStream = new BytesOutputStream();
        try {
            writeBytes(outputStream);
            outputStream.close();
        } catch (IOException ignored) {
        }
        return outputStream.toByteArray();
    }

    public void write(File file) throws IOException {
        OutputStream outputStream = FileUtil.outputStream(file);
        writeBytes(outputStream);
        outputStream.close();
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("header", getHeader().toJson());
        jsonObject.put("sections", getSectionList().toJson());
        return jsonObject;
    }
    @Override
    public void fromJson(JSONObject json) {
        getHeader().fromJson(json.getJSONObject("header"));
        getSectionList().fromJson(json.getJSONArray("sections"));
    }
    public void dumpJsons(File dir) throws IOException {
        File file = new File(dir, "header.json");
        getHeader().toJson().write(file);
        getSectionList().dumpJsons(dir);
    }

    public static GlobalMetadataFile read(byte[] bytes) throws IOException {
        GlobalMetadataFile globalMetadataFile = new GlobalMetadataFile();
        BlockReader reader = new BlockReader(bytes);
        globalMetadataFile.readBytes(reader);
        reader.close();
        return globalMetadataFile;
    }
    public static GlobalMetadataFile read(InputStream inputStream) throws IOException {
        GlobalMetadataFile globalMetadataFile = new GlobalMetadataFile();
        BlockReader reader = new BlockReader(inputStream);
        globalMetadataFile.readBytes(reader);
        reader.close();
        return globalMetadataFile;
    }
    public static GlobalMetadataFile read(File file) throws IOException {
        GlobalMetadataFile globalMetadataFile = new GlobalMetadataFile();
        BlockReader reader = new BlockReader(file);
        globalMetadataFile.readBytes(reader);
        reader.close();
        return globalMetadataFile;
    }

    public static final String FILE_NAME = ObjectsUtil.of("global-metadata.dat");
    public static final String FILE_PATH = ObjectsUtil.of("assets/bin/Data/Managed/Metadata/global-metadata.dat");

}
