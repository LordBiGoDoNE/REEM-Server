package org.rvsoftworks.reem.config;

import org.rvsoftworks.reem.config.properties.AgentProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class AgentConfig {
    @Value("${AGENT_VERSION:v0.1}")
    private String agentVersion;

    @Value("${AGENT_RELEASE_PAGE:https://github.com/LordBiGoDoNE/REEM-Agent/releases/download/}")
    private String releasePage;

    @Value("${AGENT_SCRIPT_LINUX:reem_install.sh}")
    private String scriptLinuxName;

    @Value("${AGENT_SCRIPT_WINDOWS:scriptWindowsName}")
    private String scriptWindowsName;

    @Bean
    public AgentProperties agentProperties() {
        return new AgentProperties(agentVersion, releasePage, scriptLinuxName, scriptWindowsName);
    }
}