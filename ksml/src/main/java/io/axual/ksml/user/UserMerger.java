package io.axual.ksml.user;

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



import org.apache.kafka.streams.kstream.Merger;

import io.axual.ksml.python.Invoker;

public class UserMerger extends Invoker implements Merger<Object, Object> {

    public UserMerger(UserFunction function) {
        super(function);
        verifyParameterCount(3);
        verify(function.parameters[1].type.equals(function.parameters[2].type), "Merger should take two value parameters of the same type");
        verify(function.parameters[1].type.equals(function.resultType), "Merger should return same type as its value parameters");
    }

    @Override
    public Object apply(Object key, Object value1, Object value2) {
        return function.call(key, value1, value2);
    }
}
