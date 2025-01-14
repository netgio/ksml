package io.axual.ksml.operation;

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



import io.axual.ksml.exception.KSMLApplyException;
import io.axual.ksml.stream.KStreamWrapper;
import io.axual.ksml.stream.StreamWrapper;

public class MergeOperation extends BaseOperation {
    private final KStreamWrapper mergeStream;

    public MergeOperation(KStreamWrapper mergeStream) {
        this.mergeStream = mergeStream;
    }

    @Override
    public StreamWrapper apply(KStreamWrapper input) {
        // Check if the two streams contain similar key and value types
        if (input.keyType.isAssignableFrom(mergeStream.keyType) && input.valueType.isAssignableFrom(mergeStream.valueType)) {
            return new KStreamWrapper(
                    input.stream.merge(mergeStream.stream),
                    input.keyType,
                    input.valueType);
        }
        throw new KSMLApplyException("Incompatible stream types: " + input + " and " + mergeStream);
    }
}
