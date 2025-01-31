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



import org.apache.kafka.streams.kstream.Grouped;

import io.axual.ksml.exception.KSMLApplyException;
import io.axual.ksml.generator.StreamDataType;
import io.axual.ksml.stream.KGroupedStreamWrapper;
import io.axual.ksml.stream.KGroupedTableWrapper;
import io.axual.ksml.stream.KStreamWrapper;
import io.axual.ksml.stream.KTableWrapper;
import io.axual.ksml.stream.StreamWrapper;
import io.axual.ksml.type.KeyValueType;
import io.axual.ksml.user.UserFunction;
import io.axual.ksml.user.UserKeyTransformer;
import io.axual.ksml.user.UserKeyValueTransformer;

public class GroupByOperation extends BaseOperation {
    private final UserFunction transformer;

    public GroupByOperation(UserFunction transformer) {
        this.transformer = transformer;
    }

    @Override
    public StreamWrapper apply(KStreamWrapper input) {
        final StreamDataType resultKeyType = StreamDataType.of(transformer.resultType, true);
        return new KGroupedStreamWrapper(
                input.stream.groupBy(
                        new UserKeyTransformer(transformer), Grouped.with(resultKeyType.serde, input.valueType.serde)),
                resultKeyType,
                input.valueType);
    }

    @Override
    public StreamWrapper apply(KTableWrapper input) {
        if (!(transformer.resultType instanceof KeyValueType)) {
            throw new KSMLApplyException("Can not apply given transformer to KTable.groupBy operation");
        }
        KeyValueType resultType = (KeyValueType) transformer.resultType;
        final StreamDataType resultKeyType = StreamDataType.of(resultType.getKeyType(), true);
        final StreamDataType resultValueType = StreamDataType.of(resultType.getValueType(), false);

        return new KGroupedTableWrapper(
                input.table.groupBy(
                        new UserKeyValueTransformer(transformer),
                        Grouped.with(resultKeyType.serde, resultValueType.serde)),
                resultKeyType,
                resultValueType);
    }
}
