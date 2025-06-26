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

import com.reandroid.arsc.item.BooleanReference;
import com.reandroid.arsc.item.IntegerReference;
import com.reandroid.json.JSONObject;
import com.reandroid.unity.metadata.base.BitBooleanReference;
import com.reandroid.unity.metadata.base.BitIntegerReference;
import com.reandroid.unity.metadata.base.MDUInt;
import com.reandroid.unity.metadata.util.JsonDataUtil;

public class TypeBitField extends MDUInt {

    public final BooleanReference value_type;
    public final BooleanReference enum_type;
    public final BooleanReference has_finalize;
    public final BooleanReference has_cctor;
    public final BooleanReference is_blittable;
    public final BooleanReference is_import_or_windows_runtime;
    public final IntegerReference packing_size;
    public final BooleanReference packing_size_is_default;
    public final BooleanReference class_size_is_default;
    public final IntegerReference specified_packing_size;
    public final BooleanReference is_byref_like;

    public final IntegerReference unused_bits;

    public TypeBitField() {
        super();

        IntegerReference reference = this;

        this.value_type = new BitBooleanReference(reference, 0);
        this.enum_type = new BitBooleanReference(reference, 1);
        this.has_finalize = new BitBooleanReference(reference, 2);
        this.has_cctor = new BitBooleanReference(reference, 3);
        this.is_blittable = new BitBooleanReference(reference, 4);
        this.is_import_or_windows_runtime = new BitBooleanReference(reference, 5);
        this.packing_size = new BitIntegerReference(reference, 9, 4);
        this.packing_size_is_default = new BitBooleanReference(reference, 10);
        this.class_size_is_default = new BitBooleanReference(reference, 11);
        this.specified_packing_size = new BitIntegerReference(reference, 15, 4);
        this.is_byref_like = new BitBooleanReference(reference, 16);

        this.unused_bits = new BitIntegerReference(reference, 31, 15);
    }

    @Override
    public Object getJson() {
        JSONObject json = new JSONObject();
        JsonDataUtil.putOptional(json, "value_type", value_type.get());
        JsonDataUtil.putOptional(json, "enum_type", enum_type.get());
        JsonDataUtil.putOptional(json, "has_finalize", has_finalize.get());
        JsonDataUtil.putOptional(json, "has_cctor", has_cctor.get());
        JsonDataUtil.putOptional(json, "is_blittable", is_blittable.get());
        JsonDataUtil.putOptional(json, "is_import_or_windows_runtime", is_import_or_windows_runtime.get());
        JsonDataUtil.putOptional(json, "packing_size", packing_size.get());
        JsonDataUtil.putOptional(json, "packing_size_is_default", packing_size_is_default.get());
        JsonDataUtil.putOptional(json, "class_size_is_default", class_size_is_default.get());
        JsonDataUtil.putOptional(json, "specified_packing_size", specified_packing_size.get());
        JsonDataUtil.putOptional(json, "is_byref_like", is_byref_like.get());
        JsonDataUtil.putOptional(json, "unused_bits", unused_bits.get());
        return json;
    }
    @Override
    public void setJson(Object obj) {
        JSONObject json = JsonDataUtil.nonNullObject(obj);
        value_type.set(json.optBoolean("value_type"));
        enum_type.set(json.optBoolean("enum_type"));
        has_finalize.set(json.optBoolean("has_finalize"));
        has_cctor.set(json.optBoolean("has_cctor"));
        is_blittable.set(json.optBoolean("is_blittable"));
        is_import_or_windows_runtime.set(json.optBoolean("is_import_or_windows_runtime"));
        packing_size.set(json.optInt("packing_size"));
        packing_size_is_default.set(json.optBoolean("packing_size_is_default"));
        class_size_is_default.set(json.optBoolean("class_size_is_default"));
        specified_packing_size.set(json.optInt("specified_packing_size"));
        is_byref_like.set(json.optBoolean("is_byref_like"));
        unused_bits.set(json.optInt("unused_bits"));
    }

    @Override
    public String toString() {
        return "value_type=" + value_type +
                ", enum_type=" + enum_type +
                ", has_finalize=" + has_finalize +
                ", has_cctor=" + has_cctor +
                ", is_blittable=" + is_blittable +
                ", is_import_or_windows_runtime=" + is_import_or_windows_runtime +
                ", packing_size=" + packing_size +
                ", default_packing_size=" + packing_size_is_default +
                ", default_class_size=" + class_size_is_default +
                ", specified_packing_size=" + specified_packing_size +
                ", is_byref_like=" + is_byref_like +
                ", unused_bits=" + unused_bits;
    }
}
