package org.rvsoftworks.reem.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChangeDescriptionRequest(@JsonProperty("agentId") String agentExternalId,
                                       String description) {
}
