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
import com.reandroid.unity.metadata.data.CodeStringData;
import com.reandroid.unity.metadata.data.SectionData;
import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.unity.metadata.spec.StringSpec;

public class CodeStringIndex extends DefinitionIndex<CodeStringData> {

    public CodeStringIndex(VersionRange versionRange) {
        super(MetadataSectionType.CODE_STRING, versionRange);
    }
    public CodeStringIndex() {
        super(MetadataSectionType.CODE_STRING, null);
    }

    public String getString() {
        CodeStringData data = getData();
        if (data != null) {
            return data.get();
        }
        return null;
    }
    @Override
    public StringSpec getSpec() {
        return (StringSpec) super.getSpec();
    }

    @Override
    public boolean enableUpdate() {
        return true;
    }

    @Override
    public Object getJson() {
        int idx = get();
        if (idx == SectionData.INVALID_IDX) {
            return null;
        }
        String str = getString();
        if (str == null) {
            return idx;
        }
        return str;
    }

    @Override
    public String toString() {
        return get() + "{" + getString() + "}";
    }
}
