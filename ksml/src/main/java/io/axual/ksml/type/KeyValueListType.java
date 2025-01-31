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



import java.util.List;

import io.axual.ksml.exception.KSMLTopologyException;

public class KeyValueListType extends ComplexType {
    public KeyValueListType(DataType keyType, DataType valueType) {
        super(List.class, new DataType[]{new KeyValueType(keyType, valueType)});
    }

    public DataType getKeyType() {
        return ((KeyValueType) subTypes[0]).getKeyType();
    }

    public DataType getValueType() {
        return ((KeyValueType) subTypes[0]).getValueType();
    }

    public static KeyValueListType createFrom(DataType type) {
        if (type instanceof ListType && ((ListType) type).getValueType() instanceof TupleType) {
            TupleType valueType = (TupleType) ((ListType) type).getValueType();
            if (valueType.subTypes.length == 2) {
                return new KeyValueListType(valueType.subTypes[0], valueType.subTypes[1]);
            }
        }
        throw new KSMLTopologyException("Could not convert type to KeyValueList: " + type);
    }
}
