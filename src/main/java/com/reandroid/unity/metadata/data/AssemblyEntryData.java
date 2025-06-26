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

import com.reandroid.unity.metadata.section.MetadataSectionType;
import com.reandroid.utils.ObjectsUtil;

public class AssemblyEntryData extends DefinitionEntryData<AssemblyDefinitionData> {

    public AssemblyEntryData() {
        super(MetadataSectionType.ASSEMBLIES);
    }

    @Override
    public MetadataSectionType<AssemblyEntryData> getSectionType() {
        return ObjectsUtil.cast(super.getSectionType());
    }
}
