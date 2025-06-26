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

public class VersionRange {

    private final Double min;
    private final Double max;

    public VersionRange(Double min, Double max) {
        this.min = min;
        this.max = max;
    }

    public boolean contains(double version) {
        if (version == 0.0) {
            return true;
        }
        if (min != null && version < min) {
            return false;
        }
        return max == null || version <= max;
    }
    public Double max() {
        return max;
    }
    public Double min() {
        return min;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        Double min = min();
        if (min == null) {
            builder.append("-\u221e");
        } else {
            builder.append(min);
        }
        builder.append(" .. ");
        Double max = max();
        if (max == null) {
            builder.append("+\u221e");
        } else {
            builder.append(max);
        }
        builder.append(')');
        return builder.toString();
    }
}
