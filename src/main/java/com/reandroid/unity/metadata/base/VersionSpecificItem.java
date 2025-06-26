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
package com.reandroid.unity.metadata.base;

import com.reandroid.unity.metadata.GlobalMetadataFile;

public class VersionSpecificItem extends MDBlockItem {

    private final VersionRange versionRange;

    public VersionSpecificItem(VersionRange versionRange, int bytesLength) {
        super(bytesLength);
        this.versionRange = versionRange;
    }

    public VersionRange getVersionRange() {
        return versionRange;
    }

    @Override
    public boolean isNull() {
        return !hasValidVersion() || super.isNull();
    }
    public boolean hasValidVersion() {
        VersionRange versionRange = this.getVersionRange();
        if (versionRange == null) {
            return true;
        }
        return versionRange.contains(getFileVersion());
    }
    public double getFileVersion() {
        GlobalMetadataFile globalMetadataFile = getParentInstance(GlobalMetadataFile.class);
        if (globalMetadataFile != null) {
            return globalMetadataFile.getVersion();
        }
        return 0.0;
    }
}
