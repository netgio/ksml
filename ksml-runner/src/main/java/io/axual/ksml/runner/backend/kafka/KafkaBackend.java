package io.axual.ksml.runner.backend.kafka;

/*-
 * ========================LICENSE_START=================================
 * KSML Runner
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



import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import io.axual.ksml.KSMLConfig;
import io.axual.ksml.KSMLTopologyGenerator;
import io.axual.ksml.generator.DefaultSerdeGenerator;
import io.axual.ksml.runner.backend.Backend;
import io.axual.ksml.runner.config.KSMLSourceConfig;
import lombok.extern.slf4j.Slf4j;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

@Slf4j
public class KafkaBackend implements Backend {

    private final KafkaStreams kafkaStreams;
    private final AtomicBoolean stopRunning = new AtomicBoolean(false);


    public KafkaBackend(KSMLSourceConfig ksmlSourceConfig, KafkaBackendConfig backendConfig) {
        log.info("Constructing Kafka Backend");

        Properties streamsProperties = new Properties();
        if (backendConfig.getStreamsConfig() != null) {
            streamsProperties.putAll(backendConfig.getStreamsConfig());
        }

        // Explicit configs can overwrite those from the map
        streamsProperties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, backendConfig.getBootstrapUrl());
        streamsProperties.put(SCHEMA_REGISTRY_URL_CONFIG, backendConfig.getSchemaRegistryUrl());
        streamsProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, backendConfig.getApplicationId());

        // set up a stream topology generator based on the provided KSML definition
        Map<String, Object> ksmlConfigs = new HashMap<>();
        ksmlConfigs.put(KSMLConfig.PYTHON_INTERPRETER_ISOLATION, "false");
        ksmlConfigs.put(KSMLConfig.KSML_SOURCE_TYPE, "file");
        ksmlConfigs.put(KSMLConfig.KSML_WORKING_DIRECTORY, ksmlSourceConfig.getWorkingDirectory());
        ksmlConfigs.put(KSMLConfig.KSML_SOURCE, ksmlSourceConfig.getDefinitions());
        ksmlConfigs.put(KSMLConfig.SERDE_GENERATOR, new DefaultSerdeGenerator(streamsProperties));
        KSMLTopologyGenerator topologyFactory = new KSMLTopologyGenerator();
        topologyFactory.configure(ksmlConfigs);

        final Topology topology = topologyFactory.create(new StreamsBuilder());

        kafkaStreams = new KafkaStreams(topology, streamsProperties);
    }

    @Override
    public State getState() {
        return convertStreamsState(kafkaStreams.state());
    }

    @Override
    public void stop() {
        kafkaStreams.close();
    }

    @Override
    public void close() {
        kafkaStreams.close();
    }

    @Override
    public void run() {
        log.info("Starting Kafka Backend");
        kafkaStreams.start();
        Utils.sleep(1000);
        while (!stopRunning.get()) {
            final State state = getState();
            if (state == State.STOPPED || state == State.FAILED) {
                log.info("Streams implementation has stopped, stopping Kafka Backend");
                break;
            }
            Utils.sleep(200);
        }
    }
}
