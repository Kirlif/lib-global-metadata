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
package com.reandroid.unity.metadata.spec;

import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.value.Il2CppTypeEnum;

public abstract class PrimitiveSpec implements ValueSpec {

    private final Il2CppTypeEnum type;

    PrimitiveSpec(Il2CppTypeEnum type) {
        this.type = type;
    }

    @Override
    public boolean isPrimitiveSpec() {
        return true;
    }

    public Il2CppTypeEnum type() {
        return type;
    }

    public long longValue() {
        return 0;
    }

    @Override
    public abstract Object getValue();
    @Override
    public abstract String descriptor();

    @Override
    public Object json() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type().name());
        jsonObject.put("value", getValue());
        return descriptor();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PrimitiveSpec spec = (PrimitiveSpec) obj;
        return this.longValue() == spec.longValue();
    }

    @Override
    public int hashCode() {
        return (int) longValue();
    }

    @Override
    public String toString() {
        return descriptor();
    }

    public static class BooleanSpec extends PrimitiveSpec {

        public static BooleanSpec FALSE = new BooleanSpec(false);
        public static BooleanSpec TRUE = new BooleanSpec(true);

        private final boolean value;

        private BooleanSpec(boolean value) {
            super(Il2CppTypeEnum.BOOLEAN);
            this.value = value;
        }

        public boolean get() {
            return value;
        }
        @Override
        public Boolean getValue() {
            if (get()) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        @Override
        public String descriptor() {
            if (get()) {
                return "true";
            }
            return "false";
        }

        @Override
        public long longValue() {
            return get() ? 1 : 0;
        }
    }

    public static class CharSpec extends PrimitiveSpec {

        private final char value;

        public CharSpec(char value) {
            super(Il2CppTypeEnum.CHAR);
            this.value = value;
        }

        public char get() {
            return value;
        }
        @Override
        public Character getValue() {
            return get();
        }
        @Override
        public String descriptor() {
            return "'" + get() + "'";
        }

        @Override
        public long longValue() {
            return get();
        }
    }

    public static class I1Spec extends PrimitiveSpec {

        private final byte value;

        public I1Spec(byte value) {
            super(Il2CppTypeEnum.I1);
            this.value = value;
        }

        public byte get() {
            return value;
        }
        @Override
        public Byte getValue() {
            return get();
        }
        @Override
        public String descriptor() {
            return Byte.toString(get());
        }

        @Override
        public long longValue() {
            return get();
        }
    }

    public static class U1Spec extends PrimitiveSpec {

        private final int value;

        public U1Spec(int value) {
            super(Il2CppTypeEnum.U1);
            this.value = value & 0xff;
        }

        public int get() {
            return value;
        }
        @Override
        public Integer getValue() {
            return get();
        }
        @Override
        public String descriptor() {
            return Integer.toString(get());
        }

        @Override
        public long longValue() {
            return get();
        }
    }

    public static class I2Spec extends PrimitiveSpec {

        private final short value;

        public I2Spec(short value) {
            super(Il2CppTypeEnum.I2);
            this.value = value;
        }

        public short get() {
            return value;
        }
        @Override
        public Short getValue() {
            return get();
        }
        @Override
        public String descriptor() {
            return Short.toString(get());
        }

        @Override
        public long longValue() {
            return get();
        }
    }

    public static class U2Spec extends PrimitiveSpec {

        private final int value;

        public U2Spec(int value) {
            super(Il2CppTypeEnum.U2);
            this.value = value & 0xffff;
        }

        public int get() {
            return value;
        }
        @Override
        public Integer getValue() {
            return get();
        }
        @Override
        public String descriptor() {
            return Integer.toString(get());
        }

        @Override
        public long longValue() {
            return get();
        }
    }

    public static class I4Spec extends PrimitiveSpec {

        private final int value;

        public I4Spec(int value) {
            super(Il2CppTypeEnum.I4);
            this.value = value;
        }

        public int get() {
            return value;
        }
        @Override
        public Integer getValue() {
            return get();
        }
        @Override
        public String descriptor() {
            return Integer.toString(get());
        }

        @Override
        public long longValue() {
            return get();
        }
    }

    public static class U4Spec extends PrimitiveSpec {

        private final long value;

        public U4Spec(long value) {
            super(Il2CppTypeEnum.U4);
            this.value = value & 0xffffffffL;
        }

        public long get() {
            return value;
        }
        @Override
        public Long getValue() {
            return get();
        }
        @Override
        public String descriptor() {
            return Long.toString(get());
        }

        @Override
        public long longValue() {
            return get();
        }
    }

    public static class I8Spec extends PrimitiveSpec {

        private final long value;

        public I8Spec(long value) {
            super(Il2CppTypeEnum.I8);
            this.value = value;
        }

        public long get() {
            return value;
        }
        @Override
        public Long getValue() {
            return get();
        }
        @Override
        public String descriptor() {
            return get() + "L";
        }

        @Override
        public long longValue() {
            return get();
        }
    }

    public static class U8Spec extends PrimitiveSpec {

        private final long value;

        public U8Spec(long value) {
            super(Il2CppTypeEnum.U8);
            this.value = value;
        }

        public long get() {
            return value;
        }
        @Override
        public Long getValue() {
            return get();
        }
        @Override
        public String descriptor() {
            return get() + "L";
        }

        @Override
        public long longValue() {
            return get();
        }
    }

    public static class R4Spec extends PrimitiveSpec {

        private final float value;

        public R4Spec(float value) {
            super(Il2CppTypeEnum.R4);
            this.value = value;
        }

        public float get() {
            return value;
        }
        @Override
        public Float getValue() {
            return get();
        }
        @Override
        public String descriptor() {
            return get() + "f";
        }

        @Override
        public long longValue() {
            return Float.floatToIntBits(get());
        }
    }

    public static class R8Spec extends PrimitiveSpec {

        private final double value;

        public R8Spec(double value) {
            super(Il2CppTypeEnum.R8);
            this.value = value;
        }

        public double get() {
            return value;
        }
        @Override
        public Double getValue() {
            return get();
        }
        @Override
        public String descriptor() {
            return Double.toString(get());
        }

        @Override
        public long longValue() {
            return Double.doubleToLongBits(get());
        }
    }
}
