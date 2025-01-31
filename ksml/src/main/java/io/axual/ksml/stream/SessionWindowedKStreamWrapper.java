package io.axual.ksml.stream;

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



import org.apache.kafka.streams.kstream.SessionWindowedKStream;

import io.axual.ksml.generator.StreamDataType;
import io.axual.ksml.parser.StreamOperation;

public class SessionWindowedKStreamWrapper extends BaseStreamWrapper {
    public final SessionWindowedKStream<Object, Object> sessionWindowedKStream;

    public SessionWindowedKStreamWrapper(SessionWindowedKStream<Object, Object> sessionWindowedKStream, StreamDataType keyType, StreamDataType valueType) {
        super(keyType, valueType);
        this.sessionWindowedKStream = sessionWindowedKStream;
    }

    @Override
    public StreamWrapper apply(StreamOperation operation) {
        return operation.apply(this);
    }
}
