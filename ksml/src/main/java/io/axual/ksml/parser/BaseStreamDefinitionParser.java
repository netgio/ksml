package io.axual.ksml.parser;

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



import io.axual.ksml.dsl.BaseStreamDefinition;
import io.axual.ksml.exception.KSMLParseException;

import static io.axual.ksml.dsl.KSMLDSL.GLOBALTABLE_DEFINITION;
import static io.axual.ksml.dsl.KSMLDSL.STREAM_DEFINITION;
import static io.axual.ksml.dsl.KSMLDSL.TABLE_DEFINITION;

public class BaseStreamDefinitionParser extends ContextAwareParser<BaseStreamDefinition> {
    public BaseStreamDefinitionParser(ParseContext context) {
        super(context);
    }

    @Override
    public BaseStreamDefinition parse(YamlNode node) {
        if (node == null) return null;
        if (parseText(node, STREAM_DEFINITION) != null) {
            return new InlineOrReferenceParser<>(context.getStreams(), new StreamDefinitionParser(), STREAM_DEFINITION).parse(node);
        }
        if (parseText(node, TABLE_DEFINITION) != null) {
            return new InlineOrReferenceParser<>(context.getStreams(), new TableDefinitionParser(), TABLE_DEFINITION).parse(node);
        }
        if (parseText(node, GLOBALTABLE_DEFINITION) != null) {
            return new InlineOrReferenceParser<>(context.getStreams(), new GlobalTableDefinitionParser(), GLOBALTABLE_DEFINITION).parse(node);
        }
        throw new KSMLParseException(node, "Stream definition missing");
    }
}
