package org.rvsoftworks.reem.model.dto.agent;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.rvsoftworks.commons.model.heartbeat.HeartbeatDTO;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GetAgentDTO(String description,
                          UUID externalId,
                          HeartbeatDTO heartbeat) {
}