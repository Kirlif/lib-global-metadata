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
package com.reandroid.unity.metadata.data;

import com.reandroid.arsc.container.FixedBlockContainer;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.VersionRange;
import com.reandroid.unity.metadata.base.MDByteArray;
import com.reandroid.unity.metadata.base.MDInt;
import com.reandroid.unity.metadata.base.MDUInt;
import com.reandroid.unity.metadata.index.CodeStringIndex;
import com.reandroid.unity.metadata.index.DefinitionIndex;
import com.reandroid.unity.metadata.index.DefinitionIndexList;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.StringSpec;

public class AssemblyDefinitionData extends SectionData implements TokenizedData {

    public final DefinitionIndex<ImageDefinitionData> imageIndex;
    public final MetadataToken token;
    public final MDInt customAttributeIndex;
    public final DefinitionIndexList<AssemblyEntryData> referencedAssemblies;
    public final AssemblyName assemblyName;

    public AssemblyDefinitionData() {
        super(6);

        this.imageIndex = new DefinitionIndex<>(MetadataSectionType.IMAGES);
        this.token = new MetadataToken(new VersionRange(24.1, null));
        this.customAttributeIndex = new MDInt(new VersionRange(null, 24.0));
        this.referencedAssemblies = new DefinitionIndexList<>(
                MetadataSectionType.REFERENCED_ASSEMBLIES,
                new VersionRange(20.0, null));
        this.assemblyName = new AssemblyName();

        addChild(0, imageIndex);
        addChild(1, token);
        addChild(2, customAttributeIndex);
        addChild(3, referencedAssemblies);
        addChild(4, assemblyName);
    }

    public MetadataToken getToken() {
        return token;
    }

    public String getName() {
        return assemblyName.nameIndex.getString();
    }
    public String getImageName() {
        ImageDefinitionData data = imageIndex.getData();
        if (data != null) {
            return data.getName();
        }
        return null;
    }

    @Override
    public StringSpec getSpec() {
        return assemblyName.nameIndex.getSpec();
    }

    @Override
    public MetadataSectionType<AssemblyDefinitionData> getSectionType() {
        return MetadataSectionType.ASSEMBLIES;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        Object imageName = getImageName();
        if (imageName == null) {
            imageName = imageIndex.getJson();
        }
        jsonObject.put("image", imageName);
        jsonObject.put("token", token.getJson());
        jsonObject.put("attribute", customAttributeIndex.getJson());
        jsonObject.put("referenced_assemblies", referencedAssemblies.getJson());
        jsonObject.put("assembly_name", assemblyName.toJson());
        return jsonObject;
    }

    @Override
    public String toString() {
        return "imageIndex=" + imageIndex
                + ", token=" + token
                + ", customAttributeIndex=" + customAttributeIndex
                + ", referencedAssemblies=" + referencedAssemblies
                + ", aname=" + assemblyName;
    }

    public static class AssemblyName extends FixedBlockContainer {

        public final CodeStringIndex nameIndex;
        public final MDUInt cultureIndex;
        public final MDInt hashValueIndex;
        public final MDUInt publicKeyIndex;
        public final MDUInt hash_alg;
        public final MDInt hash_len;
        public final MDUInt flags;
        public final MDInt major;
        public final MDInt minor;
        public final MDInt build;
        public final MDInt revision;
        public final MDByteArray public_key_token;

        public AssemblyName() {
            super(12);

            this.nameIndex = new CodeStringIndex();
            this.cultureIndex = new MDUInt();
            this.hashValueIndex = new MDInt(new VersionRange(null, 24.3));
            this.publicKeyIndex = new MDUInt();
            this.hash_alg = new MDUInt();
            this.hash_len = new MDInt();
            this.flags = new MDUInt();
            this.major = new MDInt();
            this.minor = new MDInt();
            this.build = new MDInt();
            this.revision = new MDInt();
            this.public_key_token = new MDByteArray(8);

            addChild(0, nameIndex);
            addChild(1, cultureIndex);
            addChild(2, hashValueIndex);
            addChild(3, publicKeyIndex);
            addChild(4, hash_alg);
            addChild(5, hash_len);
            addChild(6, flags);
            addChild(7, major);
            addChild(8, minor);
            addChild(9, build);
            addChild(10, revision);
            addChild(11, public_key_token);
        }

        public JSONObject toJson() {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", nameIndex.getJson());
            jsonObject.put("culture", cultureIndex.getJson());
            jsonObject.put("hash_value", hashValueIndex.getJson());
            jsonObject.put("public_key", publicKeyIndex.getJson());
            jsonObject.put("hash_alg", hash_alg.getJson());
            jsonObject.put("hash_len", hash_len.getJson());
            jsonObject.put("flags", flags.getJson());
            jsonObject.put("major", major.getJson());
            jsonObject.put("minor", minor.getJson());
            jsonObject.put("build", build.getJson());
            jsonObject.put("revision", revision.getJson());
            jsonObject.put("public_key_token", public_key_token.getJson());
            return jsonObject;
        }

        @Override
        public String toString() {
            return "nameIndex=" + nameIndex
                    + ", cultureIndex=" + cultureIndex
                    + ", hashValueIndex=" + hashValueIndex
                    + ", publicKeyIndex=" + publicKeyIndex
                    + ", hash_alg=" + hash_alg
                    + ", hash_len=" + hash_len
                    + ", flags=" + flags
                    + ", major=" + major
                    + ", minor=" + minor
                    + ", build=" + build
                    + ", revision=" + revision
                    + ", public_key_token=" + public_key_token;
        }
    }
}
