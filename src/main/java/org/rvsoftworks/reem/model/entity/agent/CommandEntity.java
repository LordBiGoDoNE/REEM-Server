package org.rvsoftworks.reem.model.entity.agent;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
public class CommandEntity {

    private Integer id;
    private UUID externalId;
    private UUID idAgent;
    private String command;
    private Instant createdAt;
}