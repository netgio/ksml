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



import io.axual.ksml.dsl.FunctionDefinition;
import io.axual.ksml.dsl.ParameterDefinition;

import static io.axual.ksml.dsl.KSMLDSL.FUNCTION_CODE_ATTRIBUTE;
import static io.axual.ksml.dsl.KSMLDSL.FUNCTION_EXPRESSION_ATTRIBUTE;
import static io.axual.ksml.dsl.KSMLDSL.FUNCTION_GLOBALCODE_ATTRIBUTE;
import static io.axual.ksml.dsl.KSMLDSL.FUNCTION_PARAMETERS_ATTRIBUTE;
import static io.axual.ksml.dsl.KSMLDSL.FUNCTION_RESULTTYPE_ATTRIBUTE;

public class FunctionDefinitionParser extends BaseParser<FunctionDefinition> {
    @Override
    public FunctionDefinition parse(YamlNode node) {
        if (node == null) return null;
        return FunctionDefinition.as(
                new ListParser<>(new ParameterDefinitionParser(), 1).parse(node.get(FUNCTION_PARAMETERS_ATTRIBUTE)).toArray(new ParameterDefinition[0]),
                TypeParser.parse(parseText(node, FUNCTION_RESULTTYPE_ATTRIBUTE)),
                parseText(node, FUNCTION_EXPRESSION_ATTRIBUTE),
                parseMultilineText(node, FUNCTION_CODE_ATTRIBUTE),
                parseMultilineText(node, FUNCTION_GLOBALCODE_ATTRIBUTE));
    }
}
