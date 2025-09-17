package org.rvsoftworks.reem.model.entity.agent;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@Setter
public class CommandReturnEntity {
    private UUID externalId;
    private String retorno;
}