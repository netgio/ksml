package io.axual.ksml.example.producer.exception;

/*-
 * ========================LICENSE_START=================================
 * KSML Example Producer
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

public class ConfigurationException extends RuntimeException{
    public static final String MESSAGE_DETAIL_FORMAT="%nConfiguration Key   : '%s'%nConfiguration Value : '%s' ";
    public static final String DEFAULT_MESSAGE="An invalid configuration has been found.";
    
    private final String configKey;
    private final Object configValue;

    public ConfigurationException(String configKey, Object configValue) {
        this(configKey,configValue,DEFAULT_MESSAGE);
    }

    public ConfigurationException(String configKey, Object configValue, String message) {
        super(message+ String.format(MESSAGE_DETAIL_FORMAT, configKey,configValue));
        this.configKey = configKey;
        this.configValue = configValue;
    }
}
