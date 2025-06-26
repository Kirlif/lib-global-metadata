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

import com.reandroid.unity.metadata.base.VersionRange;
import com.reandroid.unity.metadata.base.LinkableItem;
import com.reandroid.unity.metadata.base.MDCompressedSInt32;
import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.unity.metadata.section.MetadataSection;

public abstract class CompressedIntIndex<T extends SectionData> extends MDCompressedSInt32
        implements LinkableItem {

    private T mData;

    public CompressedIntIndex(VersionRange versionRange) {
        super(versionRange);
    }
    public CompressedIntIndex() {
        this(null);
    }

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        this.mData = data;
        // FIXME
    }

    public abstract T pullData(int idx);
    public abstract MetadataSection<?> getSection();
}
