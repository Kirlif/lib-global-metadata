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

import com.reandroid.arsc.io.BlockReader;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.VersionRange;
import com.reandroid.unity.metadata.base.MDInt;
import com.reandroid.unity.metadata.base.MDUInt;
import com.reandroid.unity.metadata.base.MDUShort;
import com.reandroid.unity.metadata.index.*;
import com.reandroid.unity.metadata.section.*;
import com.reandroid.unity.metadata.spec.TypeSpec;
import com.reandroid.utils.StringsUtil;

import java.io.IOException;

public class TypeDefinitionData extends SectionData implements TokenizedData {

    private final TypeDefinitionData baseDefinition;

    private final CodeStringIndex nameIndex;
    private final CodeStringIndex namespaceIndex;
    private final MDInt customAttributeIndex;
    private final MDInt byvalTypeIndex;
    private final MDInt byrefTypeIndex;
    private final TypeDefinitionIndex declaringTypeIndex;
    private final TypeDefinitionIndex parentIndex;
    private final TypeDefinitionIndex elementTypeIndex;

    private final DefinitionIndex<GenericContainerData> genericContainerIndex;
    private final MDInt delegateWrapperFromManagedToNativeIndex;
    private final MDInt marshalingFunctionsIndex;
    private final MDInt ccwFunctionIndex;
    private final MDInt guidIndex;
    private final MDUInt flags;

    private final TypeBitField bitfield;
    private final MetadataToken token;

    private final DefinitionIndexList<RGCTXDefinitionData> rgctxList;
    private final FieldDefinitionIndexList fieldList;
    private final MethodDefinitionIndexList methodList;
    private final DefinitionIndexList<EventDefinitionData> eventList;
    private final DefinitionIndexList<PropertyDefinitionData> propertyList;
    private final TypeDefinitionIndexList nestedTypeList;
    private final DefinitionIndexList<TypeEntryData> interfaceList;
    private final DefinitionIndexList<VtableMethodData> vtableList;
    private final DefinitionIndexList<InterfaceOffsetPairData> interfaceOffsetList;

    public TypeDefinitionData() {
        super(43);

        this.baseDefinition = null;

        this.nameIndex = new CodeStringIndex();
        this.namespaceIndex = new CodeStringIndex();
        this.customAttributeIndex = new MDInt(new VersionRange(null, 24.0));
        this.byvalTypeIndex = new MDInt();
        this.byrefTypeIndex = new MDInt(new VersionRange(null, 24.5));
        this.declaringTypeIndex = new TypeDefinitionIndex();
        this.parentIndex = new TypeDefinitionIndex();
        this.elementTypeIndex = new TypeDefinitionIndex();
        MDInt rgctxStartIndex = new MDInt(new VersionRange(null, 24.1));
        MDInt rgctxCount = new MDInt(new VersionRange(null, 24.1));
        this.genericContainerIndex = new DefinitionIndex<>(MetadataSectionType.GENERIC_CONTAINERS);
        this.delegateWrapperFromManagedToNativeIndex = new MDInt(new VersionRange(null, 22.0));
        this.marshalingFunctionsIndex = new MDInt(new VersionRange(null, 22.0));
        this.ccwFunctionIndex = new MDInt(new VersionRange(21.0, 22.0));
        this.guidIndex = new MDInt(new VersionRange(21.0, 22.0));
        this.flags = new MDUInt();

        MDInt fieldStart = new MDInt();
        MDInt methodStart = new MDInt();
        MDInt eventStart = new MDInt();
        MDInt propertyStart = new MDInt();
        MDInt nestedTypesStart = new MDInt();
        MDInt interfacesStart = new MDInt();
        MDInt vtableStart = new MDInt();
        MDInt interfaceOffsetsStart = new MDInt();
        MDUShort method_count = new MDUShort();
        MDUShort property_count = new MDUShort();
        MDUShort field_count = new MDUShort();
        MDUShort event_count = new MDUShort();
        MDUShort nested_type_count = new MDUShort();
        MDUShort vtable_count = new MDUShort();
        MDUShort interfaces_count = new MDUShort();
        MDUShort interface_offsets_count = new MDUShort();

        this.bitfield = new TypeBitField();
        this.token = new MetadataToken(new VersionRange(19.0, null));

        this.rgctxList = new DefinitionIndexList<>(MetadataSectionType.RGCTX_ENTRIES, rgctxStartIndex, rgctxCount);
        this.fieldList = new FieldDefinitionIndexList(fieldStart, field_count);
        this.methodList = new MethodDefinitionIndexList(methodStart, method_count);
        this.eventList = new DefinitionIndexList<>(MetadataSectionType.EVENTS, eventStart, event_count);
        this.propertyList = new DefinitionIndexList<>(MetadataSectionType.PROPERTIES, propertyStart, property_count);
        this.nestedTypeList = new TypeDefinitionIndexList(nestedTypesStart, nested_type_count);
        this.interfaceList = new DefinitionIndexList<>(MetadataSectionType.INTERFACES, interfacesStart, interfaces_count);
        this.vtableList = new DefinitionIndexList<>(MetadataSectionType.VTABLE_METHODS, vtableStart, vtable_count);
        this.interfaceOffsetList = new DefinitionIndexList<>(MetadataSectionType.INTERFACE_OFFSETS, interfacesStart,
                interface_offsets_count);

        addChild(0, nameIndex);
        addChild(1, namespaceIndex);
        addChild(2, customAttributeIndex);
        addChild(3, byvalTypeIndex);
        addChild(4, byrefTypeIndex);
        addChild(5, declaringTypeIndex);
        addChild(6, parentIndex);
        addChild(7, elementTypeIndex);
        addChild(8, rgctxStartIndex);
        addChild(9, rgctxCount);
        addChild(10, genericContainerIndex);
        addChild(11, delegateWrapperFromManagedToNativeIndex);
        addChild(12, marshalingFunctionsIndex);
        addChild(13, ccwFunctionIndex);
        addChild(14, guidIndex);
        addChild(15, flags);
        addChild(16, fieldStart);
        addChild(17, methodStart);
        addChild(18, eventStart);
        addChild(19, propertyStart);
        addChild(20, nestedTypesStart);
        addChild(21, interfacesStart);
        addChild(22, vtableStart);
        addChild(23, interfaceOffsetsStart);
        addChild(24, method_count);
        addChild(25, property_count);
        addChild(26, field_count);
        addChild(27, event_count);
        addChild(28, nested_type_count);
        addChild(29, vtable_count);
        addChild(30, interfaces_count);
        addChild(31, interface_offsets_count);
        addChild(32, bitfield);
        addChild(33, token);

        //blank blocks
        addChild(34, rgctxList);
        addChild(35, fieldList);
        addChild(36, methodList);
        addChild(37, eventList);
        addChild(38, propertyList);
        addChild(39, nestedTypeList);
        addChild(40, interfaceList);
        addChild(41, vtableList);
        addChild(42, interfaceOffsetList);
    }
    private TypeDefinitionData(TypeDefinitionData base) {
        super(0);

        this.baseDefinition = base;

        this.nameIndex = null;
        this.namespaceIndex = null;
        this.customAttributeIndex = null;
        this.byvalTypeIndex = null;
        this.byrefTypeIndex = null;
        this.declaringTypeIndex = null;
        this.parentIndex = null;
        this.elementTypeIndex = null;

        this.genericContainerIndex = null;
        this.delegateWrapperFromManagedToNativeIndex = null;
        this.marshalingFunctionsIndex = null;
        this.ccwFunctionIndex = null;
        this.guidIndex = null;
        this.flags = null;

        this.bitfield = null;
        this.token = null;

        this.rgctxList = null;
        this.fieldList = null;
        this.methodList = null;
        this.eventList = null;
        this.propertyList = null;
        this.nestedTypeList = null;
        this.interfaceList = null;
        this.vtableList = null;
        this.interfaceOffsetList = null;
    }

    public CodeStringIndex nameIndex() {
        return getDefinition().nameIndex;
    }
    public CodeStringIndex namespaceIndex() {
        return getDefinition().namespaceIndex;
    }
    public MDInt customAttributeIndex() {
        return getDefinition().customAttributeIndex;
    }
    public MDInt byvalTypeIndex() {
        return getDefinition().byvalTypeIndex;
    }
    public MDInt byrefTypeIndex() {
        return getDefinition().byrefTypeIndex;
    }
    public TypeDefinitionIndex declaringTypeIndex() {
        return getDefinition().declaringTypeIndex;
    }
    public TypeDefinitionIndex parentIndex() {
        return getDefinition().parentIndex;
    }
    public TypeDefinitionIndex elementTypeIndex() {
        return getDefinition().elementTypeIndex;
    }
    public DefinitionIndex<GenericContainerData> genericContainerIndex() {
        return getDefinition().genericContainerIndex;
    }
    public MDInt delegateWrapperFromManagedToNativeIndex() {
        return getDefinition().delegateWrapperFromManagedToNativeIndex;
    }

    public MDInt marshalingFunctionsIndex() {
        return getDefinition().marshalingFunctionsIndex;
    }
    public MDInt ccwFunctionIndex() {
        return getDefinition().ccwFunctionIndex;
    }
    public MDInt guidIndex() {
        return getDefinition().guidIndex;
    }
    public MDUInt flags() {
        return getDefinition().flags;
    }
    public DefinitionIndexList<RGCTXDefinitionData> getRgctxList() {
        return getDefinition().rgctxList;
    }
    public DefinitionIndexList<FieldDefinitionData> getFieldList() {
        return getDefinition().fieldList;
    }
    public DefinitionIndexList<MethodDefinitionData> getMethodList() {
        return getDefinition().methodList;
    }
    public DefinitionIndexList<EventDefinitionData> getEventList() {
        return getDefinition().eventList;
    }
    public DefinitionIndexList<PropertyDefinitionData> getPropertyList() {
        return getDefinition().propertyList;
    }
    public DefinitionIndexList<TypeDefinitionData> getNestedTypeList() {
        return getDefinition().nestedTypeList;
    }
    public DefinitionIndexList<TypeEntryData> getInterfaceList() {
        return getDefinition().interfaceList;
    }
    public DefinitionIndexList<VtableMethodData> getVtableList() {
        return getDefinition().vtableList;
    }
    public DefinitionIndexList<InterfaceOffsetPairData> getInterfaceOffsetList() {
        return getDefinition().interfaceOffsetList;
    }

    public TypeBitField getBitfield() {
        return getDefinition().bitfield;
    }
    @Override
    public MetadataToken getToken() {
        return getDefinition().token;
    }

    public TypeDefinitionData getBaseDefinition() {
        return baseDefinition;
    }
    public TypeDefinitionData getDefinition() {
        TypeDefinitionData base = this;
        TypeDefinitionData data;
        while ((data = base.getBaseDefinition()) != null) {
            base = data;
        }
        return base;
    }
    public TypeDefinitionData createCopy() {
        return new TypeDefinitionData(getDefinition());
    }
    public boolean isBaseDefinition() {
        return getBaseDefinition() == null;
    }
    public int getByVal() {
        if (isBaseDefinition()) {
            return byvalTypeIndex().get();
        }
        return SectionData.INVALID_IDX;
    }

    @Override
    public TypeSpec getSpec() {
        return TypeSpec.of(getNamespace(), getName());
    }

    public String getTypeName() {
        String name = getName();
        if (name == null) {
            return null;
        }
        String nameSpace = getNamespace();
        if (StringsUtil.isEmpty(nameSpace)) {
            return name;
        }
        return nameSpace + "." + name;
    }
    public String getName() {
        return nameIndex().getString();
    }
    public String getNamespace() {
        return namespaceIndex().getString();
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        if (isBaseDefinition()) {
            super.onReadBytes(reader);
            linkSelfReferences();
        }
    }
    private void linkSelfReferences() {
        if (isBaseDefinition() && isSelfId(elementTypeIndex().get())) {
            elementTypeIndex().setDataInternal(this);
        }
    }
    private boolean isSelfId(int idx) {
        return idx == getIdx() || idx == byvalTypeIndex().get();
    }

    @Override
    public void onIndexLinked(Object linker) {
        if (isBaseDefinition()) {
            elementTypeIndex().link();
            parentIndex().link();
            getRgctxList().link();
            declaringTypeIndex().link();
            getMethodList().link();
            getFieldList().link();
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idx", getIdx());
        if (!isBaseDefinition()) {
            jsonObject.put("definition", getBaseDefinition().getIdx());
            return jsonObject;
        }
        jsonObject.put("name", nameIndex().getJson());
        jsonObject.put("namespace", namespaceIndex().getJson());
        jsonObject.put("custom_attribute", customAttributeIndex().getJson());
        jsonObject.put("byval_index", byvalTypeIndex().getJson());
        jsonObject.put("byref_index", byrefTypeIndex().getJson());
        jsonObject.put("declaring", declaringTypeIndex().getJson());
        jsonObject.put("parent", parentIndex().getJson());
        jsonObject.put("element_type", elementTypeIndex().getJson());
        jsonObject.put("rgctx", getRgctxList().getJson());
        jsonObject.put("generic_container", genericContainerIndex().getJson());
        jsonObject.put("delegateWrapperFromManagedToNativeIndex", delegateWrapperFromManagedToNativeIndex().getJson());
        jsonObject.put("marshalingFunctionsIndex", marshalingFunctionsIndex().getJson());
        jsonObject.put("ccwFunctionIndex", ccwFunctionIndex().getJson());
        jsonObject.put("guidIndex", guidIndex().getJson());
        jsonObject.put("flags", flags().getJson());

        jsonObject.put("bitfield", getBitfield().getJson());
        jsonObject.put("token", getToken().getJson());

        jsonObject.put("fields", getFieldList().getJson());
        jsonObject.put("methods", getMethodList().getJson());
        jsonObject.put("events", getEventList().getJson());
        jsonObject.put("properties", getPropertyList().getJson());
        jsonObject.put("nested_types", getNestedTypeList().getJson());
        jsonObject.put("interfaces", getInterfaceList().getJson());
        jsonObject.put("vtables", getVtableList().getJson());
        jsonObject.put("interface_offsets", getInterfaceOffsetList().getJson());

        return jsonObject;
    }

    @Override
    public String toString() {
        TypeDefinitionData parentDefinition = getBaseDefinition();
        if (parentDefinition != null) {
            return getIndex() + ": parent = " + parentDefinition.getIndex() + " ("
                    + parentDefinition.getTypeName() + ")";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("namespace ").append(getNamespace()).append(" {");
        String tab = "\n  ";
        builder.append(tab).append(".class ").append(getName()).append(" {");
        tab = "\n    ";
        DefinitionIndexList<FieldDefinitionData> fields = getFieldList();
        int length = fields.size();
        for (int i = 0; i < length; i++) {
            FieldDefinitionData field = fields.get(i);
            builder.append(tab).append(".field ");
            if (field == null) {
                builder.append("null");
            } else {
                builder.append(field.getModifiers());
                builder.append(field.getName());
                builder.append(":");
                builder.append(field.getTypeName());
            }
            builder.append(";");
        }
        DefinitionIndexList<MethodDefinitionData> methods = getMethodList();
        length = methods.size();
        for (int i = 0; i < length; i++) {
            MethodDefinitionData method = methods.get(i);
            builder.append(tab).append(".method ");
            if (method == null) {
                builder.append("null");
            } else {
                builder.append(method.getModifiers());
                builder.append(method.returnType.getTypeName());
                builder.append(" ");
                builder.append(method.getSpec());
            }
        }
        tab = "\n  ";
        builder.append(tab).append("}");
        tab = "\n";
        builder.append(tab).append("}");
        return builder.toString();
    }
}
