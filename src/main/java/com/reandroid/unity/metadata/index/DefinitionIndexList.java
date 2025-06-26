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
package com.reandroid.unity.metadata.index;

import com.reandroid.arsc.base.BlockRefresh;
import com.reandroid.arsc.item.IndirectInteger;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.section.MetadataSectionList;
import com.reandroid.unity.metadata.base.VersionRange;
import com.reandroid.unity.metadata.base.LinkableItem;
import com.reandroid.unity.metadata.base.VersionSpecificItem;
import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.unity.metadata.section.MetadataSection;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.Spec;
import com.reandroid.unity.metadata.spec.SpecList;
import com.reandroid.utils.NumbersUtil;
import com.reandroid.utils.ObjectsUtil;
import com.reandroid.utils.collection.ArrayIterator;
import com.reandroid.utils.collection.ComputeIterator;

import java.util.Iterator;

public class DefinitionIndexList<T extends SectionData> extends VersionSpecificItem implements
        BlockRefresh, LinkableItem {

    private final MetadataSectionType<T> sectionType;

    private final IntegerReference startReference;
    private final IntegerReference countReference;

    private Object[] dataList;

    private int linkStamp;

    public DefinitionIndexList(int bytesLength, MetadataSectionType<T> sectionType, VersionRange versionRange,
                               IntegerReference start, IntegerReference count) {
        super(versionRange, bytesLength);
        this.startReference = start;
        this.countReference = count;
        this.sectionType = sectionType;
    }
    public DefinitionIndexList(MetadataSectionType<T> sectionType, IntegerReference start, IntegerReference count) {
        this(0, sectionType, null, start, count);
    }
    public DefinitionIndexList(MetadataSectionType<T> sectionType, VersionRange versionRange) {
        super(versionRange, 8);
        this.sectionType = sectionType;
        this.startReference = new IndirectInteger(this, 0);
        this.countReference = new IndirectInteger(this, 4);
    }
    public DefinitionIndexList(MetadataSectionType<T> sectionType) {
        this(sectionType, null);
    }

    public SpecList<?> getSpec() {
        return SpecList.collect(ComputeIterator.of(iterator(), this::getSpec));
    }
    Spec getSpec(T data) {
        return data.getSpec();
    }
    public Iterator<T> iterator() {
        return ArrayIterator.of(getDataList());
    }
    public T getFirst() {
        int size = size();
        if (size != 0) {
            return get(0);
        }
        return null;
    }
    public T getLast() {
        int size = size();
        if (size != 0) {
            return get(size - 1);
        }
        return null;
    }
    public T get(int i) {
        return ObjectsUtil.cast(getDataList()[i]);
    }
    public int size() {
        Object[] dataList = getDataList();
        if (dataList != null) {
            return dataList.length;
        }
        return 0;
    }
    public IntegerReference getStartReference() {
        return startReference;
    }
    public IntegerReference getCountReference() {
        return countReference;
    }
    public Object[] getDataList() {
        return dataList;
    }
    @Override
    public void link() {
        if (!hasValidVersion()) {
            return;
        }
        int start = getStartReference().get();
        int count = getCountReference().get();
        int stamp = start * 31 + count;
        if (stamp == this.linkStamp) {
            return;
        }
        this.linkStamp = stamp;
        Object[] dataList = this.dataList;
        if (count == 0) {
            this.dataList = null;
            return;
        }
        if (dataList == null || count != dataList.length) {
            dataList = new Object[count];
            this.dataList = dataList;
        }
        MetadataSection<?> section = getSection();
        if (section != null) {
            section.getByIdx(dataList, start, count);
            if (!hasNullElement(start, dataList)) {
                linkDataElements(dataList);
            } else {
                onLinkFailed();
            }
        } else {
            onLinkFailed();
        }
    }
    private void onLinkFailed() {
        linkStamp --;
        this.dataList = null;
    }
    protected void linkDataElements(Object[] dataList) {
        int length = dataList.length;
        for (int i = 0; i < length; i++) {
            T data = ObjectsUtil.cast(dataList[i]);
            if (data != null) {
                onLinked(i, data);
            }
        }
    }
    protected void onLinked(int i, T data) {
        if (data.getParent() != this.getParent() && this.getParent(data.getClass()) != data) {
            data.onIndexLinked(this);
        }
    }
    @Override
    public void refresh() {
        /*
        if (hasValidVersion()) {
            refreshStartAndCount();
        }
        */
    }
    public MetadataSectionType<T> getSectionType() {
        return sectionType;
    }

    public MetadataSection<T> getSection() {
        MetadataSectionList sectionList = getSectionList();
        if (sectionList != null) {
            return sectionList.getSection(getSectionType());
        }
        return null;
    }
    public MetadataSectionList getSectionList() {
        return getParentInstance(MetadataSectionList.class);
    }

    protected String simpleToString(T item) {
        if (item == null) {
            return "null";
        }
        String str = item.toString();
        if (str.length() > 50) {
            str = str.substring(0, 50);
        }
        return str;
    }


    @Override
    public Object getJson() {
        if (getCountReference().get() != size()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("start", getStartReference().get());
            jsonObject.put("count", getCountReference().get());
            return jsonObject;
        }
        if (size() == 0) {
            return null;
        }
        SpecList<?> specList = getSpec();
        if (specList != null) {
            return specList.json();
        }
        return super.getJson();
    }

    @Override
    public String toString() {
        if (!hasValidVersion()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        int start = getStartReference().get();
        builder.append(start);
        builder.append(':');
        builder.append(getCountReference().get());
        builder.append(']');
        if (this.linkStamp != 0) {
            builder.append(" {");
            int size = size();
            if (size != 0) {
                int max = 10;
                int length = NumbersUtil.min(size, max);
                for (int i = 0; i < length; i++) {
                    if (i != 0) {
                        builder.append(", ");
                    }
                    T data = get(i);
                    if (data == null) {
                        builder.append(start + i);
                    } else {
                        builder.append(simpleToString(data));
                    }
                }
                length = size();
                if (length > max) {
                    builder.append(", ... +");
                    builder.append(length - max);
                    builder.append(" more");
                }
                if (length != 0) {
                    builder.append("(last = ");
                    builder.append(simpleToString(get(length - 1)));
                    builder.append(")");
                }
            }
            builder.append('}');
        }
        return builder.toString();
    }

    private static boolean hasNullElement(int start, Object[] dataList) {
        for (Object data : dataList) {
            if (start >= 0 && data == null) {
                return true;
            }
            start ++;
        }
        return false;
    }
}
