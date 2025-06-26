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

import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.VersionRange;
import com.reandroid.unity.metadata.base.MDInt;
import com.reandroid.unity.metadata.base.MDUInt;
import com.reandroid.unity.metadata.base.MDUShort;
import com.reandroid.unity.metadata.index.CodeStringIndex;
import com.reandroid.unity.metadata.index.DefinitionIndex;
import com.reandroid.unity.metadata.index.ParameterDefinitionIndexList;
import com.reandroid.unity.metadata.index.TypeDefinitionIndex;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.MethodSpec;

public class MethodDefinitionData extends SectionData implements TokenizedData {

    public final CodeStringIndex nameIndex;
    public final TypeDefinitionIndex declaringType;
    public final TypeDefinitionIndex returnType;
    public final MDInt returnParameterToken;
    public final MDInt parameterStart;
    public final MDInt customAttributeIndex;
    public final DefinitionIndex<GenericContainerData> genericContainerIndex;
    public final MDInt methodIndex;
    public final MDInt invokerIndex;
    public final MDInt delegateWrapperIndex;
    public final MDInt rgctxStartIndex;
    public final MDInt rgctxCount;
    public final MetadataToken token;
    public final MDUShort flags;
    public final MDUShort iflags;
    public final MDUShort slot;
    public final MDUShort parameterCount;

    private final ParameterDefinitionIndexList parametersList;

    public MethodDefinitionData() {
        super(18);

        this.nameIndex = new CodeStringIndex();
        this.declaringType = new TypeDefinitionIndex();
        this.returnType = new TypeDefinitionIndex();
        this.returnParameterToken = new MDInt(new VersionRange(31.0, null));
        this.parameterStart = new MDUInt();
        this.customAttributeIndex = new MDInt(new VersionRange(null, 24.0));
        this.genericContainerIndex = new DefinitionIndex<>(MetadataSectionType.GENERIC_CONTAINERS);
        this.methodIndex = new MDInt(new VersionRange(null, 24.1));
        this.invokerIndex = new MDInt(new VersionRange(null, 24.1));
        this.delegateWrapperIndex = new MDInt(new VersionRange(null, 24.1));
        this.rgctxStartIndex = new MDInt(new VersionRange(null, 24.1));
        this.rgctxCount = new MDInt(new VersionRange(null, 24.1));
        this.token = new MetadataToken();
        this.flags = new MDUShort();
        this.iflags = new MDUShort();
        this.slot = new MDUShort();
        this.parameterCount = new MDUShort();

        this.parametersList = new ParameterDefinitionIndexList(parameterStart, parameterCount);

        addChild(0, nameIndex);
        addChild(1, declaringType);
        addChild(2, returnType);
        addChild(3, returnParameterToken);
        addChild(4, parameterStart);
        addChild(5, customAttributeIndex);
        addChild(6, genericContainerIndex);
        addChild(7, methodIndex);
        addChild(8, invokerIndex);
        addChild(9, delegateWrapperIndex);
        addChild(10, rgctxStartIndex);
        addChild(11, rgctxCount);
        addChild(12, token);
        addChild(13, flags);
        addChild(14, iflags);
        addChild(15, slot);
        addChild(16, parameterCount);
        addChild(17, parametersList);
    }

    public MetadataToken getToken() {
        return token;
    }
    @Override
    public MethodSpec getSpec() {
        return MethodSpec.of(declaringType.getSpec(),
                nameIndex.getSpec(), returnType.getSpec(),
                parametersList.getSpec());
    }

    public String getName() {
        return nameIndex.getString();
    }

    public String getModifiers() {
        int flags = this.flags.get();
        StringBuilder builder = new StringBuilder();
        int access = flags & METHOD_ATTRIBUTE_MEMBER_ACCESS_MASK;
        switch (access) {
            case METHOD_ATTRIBUTE_PRIVATE:
                builder.append("private ");
                break;
            case METHOD_ATTRIBUTE_PUBLIC:
                builder.append("public ");
                break;
            case METHOD_ATTRIBUTE_FAMILY:
                builder.append("protected ");
                break;
            case METHOD_ATTRIBUTE_ASSEM:
            case METHOD_ATTRIBUTE_FAM_AND_ASSEM:
                builder.append("internal ");
                break;
            case METHOD_ATTRIBUTE_FAM_OR_ASSEM:
                builder.append("protected internal ");
                break;
        }
        if ((flags & METHOD_ATTRIBUTE_STATIC) != 0) {
            builder.append("static ");
        }
        if ((flags & METHOD_ATTRIBUTE_ABSTRACT) != 0) {
            builder.append("abstract ");
            if ((flags & METHOD_ATTRIBUTE_VTABLE_LAYOUT_MASK) == METHOD_ATTRIBUTE_REUSE_SLOT) {
                builder.append("override ");
            }
        } else if ((flags & METHOD_ATTRIBUTE_FINAL) != 0) {
            if ((flags & METHOD_ATTRIBUTE_VTABLE_LAYOUT_MASK) == METHOD_ATTRIBUTE_REUSE_SLOT) {
                builder.append("sealed override ");
            }
        } else if ((flags & METHOD_ATTRIBUTE_VIRTUAL) != 0) {
            if ((flags & METHOD_ATTRIBUTE_VTABLE_LAYOUT_MASK) == METHOD_ATTRIBUTE_NEW_SLOT) {
                builder.append("virtual ");
            } else {
                builder.append("override ");
            }
        }
        if ((flags & METHOD_ATTRIBUTE_PINVOKE_IMPL) != 0) {
            builder.append("extern ");
        }
        return builder.toString();
    }

    public ParameterDefinitionIndexList getParametersList() {
        return parametersList;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", nameIndex.getJson());
        jsonObject.put("declaringType", declaringType.getJson());
        jsonObject.put("returnType", returnType.getJson());
        jsonObject.put("returnParameterToken", returnParameterToken.getJson());
        jsonObject.put("customAttributeIndex", customAttributeIndex.getJson());
        jsonObject.put("generic_container", genericContainerIndex.getJson());
        jsonObject.put("methodIndex", methodIndex.getJson());
        jsonObject.put("invokerIndex", invokerIndex.getJson());
        jsonObject.put("delegateWrapperIndex", delegateWrapperIndex.getJson());
        jsonObject.put("rgctxStartIndex", rgctxStartIndex.getJson());
        jsonObject.put("rgctxCount", rgctxCount.getJson());
        jsonObject.put("token", token.getJson());
        jsonObject.put("flags", flags.getJson());
        jsonObject.put("iflags", iflags.getJson());
        jsonObject.put("slot", slot.getJson());
        jsonObject.put("parameters", parametersList.getJson());
        return jsonObject;
    }

    @Override
    public MetadataSectionType<MethodDefinitionData> getSectionType() {
        return MetadataSectionType.METHODS;
    }

    @Override
    public String toString() {
        return "nameIndex=" + nameIndex
                + ", declaringType=" + declaringType
                + ", returnType=" + returnType
                + ", returnParameterToken=" + returnParameterToken
                + ", parameterStart=" + parameterStart
                + ", customAttributeIndex=" + customAttributeIndex
                + ", genericContainerIndex=" + genericContainerIndex
                + ", methodIndex=" + methodIndex
                + ", invokerIndex=" + invokerIndex
                + ", delegateWrapperIndex=" + delegateWrapperIndex
                + ", rgctxStartIndex=" + rgctxStartIndex
                + ", rgctxCount=" + rgctxCount
                + ", token=" + token
                + ", flags=" + flags
                + ", iflags=" + iflags
                + ", slot=" + slot
                + ", parameterCount=" + parameterCount;
    }

    public static final int METHOD_ATTRIBUTE_MEMBER_ACCESS_MASK = 0x0007;
    public static final int METHOD_ATTRIBUTE_COMPILER_CONTROLLED = 0x0000;
    public static final int METHOD_ATTRIBUTE_PRIVATE = 0x0001;
    public static final int METHOD_ATTRIBUTE_FAM_AND_ASSEM = 0x0002;
    public static final int METHOD_ATTRIBUTE_ASSEM = 0x0003;
    public static final int METHOD_ATTRIBUTE_FAMILY = 0x0004;
    public static final int METHOD_ATTRIBUTE_FAM_OR_ASSEM = 0x0005;
    public static final int METHOD_ATTRIBUTE_PUBLIC = 0x0006;

    public static final int METHOD_ATTRIBUTE_STATIC = 0x0010;
    public static final int METHOD_ATTRIBUTE_FINAL = 0x0020;
    public static final int METHOD_ATTRIBUTE_VIRTUAL = 0x0040;

    public static final int METHOD_ATTRIBUTE_VTABLE_LAYOUT_MASK = 0x0100;
    public static final int METHOD_ATTRIBUTE_REUSE_SLOT = 0x0000;
    public static final int METHOD_ATTRIBUTE_NEW_SLOT = 0x0100;

    public static final int METHOD_ATTRIBUTE_ABSTRACT = 0x0400;
    public static final int METHOD_ATTRIBUTE_PINVOKE_IMPL = 0x2000;
}
