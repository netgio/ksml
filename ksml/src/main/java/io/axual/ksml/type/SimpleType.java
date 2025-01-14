package io.axual.ksml.type;

/*-
 * ========================LICENSE_START=================================
 * KSML
 * %%
 * Copyright (C) 2021 Axual B.V.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */



import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SimpleType implements DataType {
    public static final SimpleType BOOLEAN = new SimpleType(Boolean.class);
    public static final SimpleType BYTES = new SimpleType(byte[].class);
    public static final SimpleType DOUBLE = new SimpleType(Double.class);
    public static final SimpleType FLOAT = new SimpleType(Float.class);
    public static final SimpleType INTEGER = new SimpleType(Integer.class);
    public static final SimpleType LONG = new SimpleType(Long.class);
    public static final SimpleType STRING = new SimpleType(String.class);

    public final Class<?> type;

    @Override
    public String toString() {
        return type.getSimpleName();
    }

    public boolean isAssignableFrom(Class<?> type) {
        return this.type.isAssignableFrom(type);
    }

    @Override
    public boolean isAssignableFrom(DataType other) {
        if (other instanceof SimpleType) {
            return isAssignableFrom(((SimpleType) other).type);
        }
        return false;
    }

    @Override
    public boolean isAssignableFrom(Object value) {
        return isAssignableFrom(value.getClass());
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SimpleType other = (SimpleType) obj;
        return isAssignableFrom(other) && other.isAssignableFrom(this);
    }

    public int hashCode() {
        int result = super.hashCode();
        return result * 31 + type.hashCode();
    }
}
