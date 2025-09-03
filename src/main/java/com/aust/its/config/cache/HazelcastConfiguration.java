package com.aust.its.config.cache;

import com.hazelcast.config.ClasspathYamlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfiguration {

    @Bean(name = "hazelcastServerInstance")
    public HazelcastInstance hazelcastServerConfig() {
        Config config = new ClasspathYamlConfig("hazelcast.yaml");
        return Hazelcast.newHazelcastInstance(config);
    }
}