package org.rvsoftworks.reem.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
public class AgentProperties {
    private String agentVersion;
    private String releasePage;
    private String scriptLinuxName;
    private String scriptWindowsName;


}