package org.rvsoftworks.reem.model.entity.agent;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class AgentEntity {
    private Integer id;
    private String description;
    private UUID externalId;
    private String agentSecret;
}