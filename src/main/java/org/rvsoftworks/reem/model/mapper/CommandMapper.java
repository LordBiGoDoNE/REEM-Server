package org.rvsoftworks.reem.model.mapper;

import com.github.f4b6a3.uuid.UuidCreator;
import com.github.f4b6a3.uuid.util.UuidUtil;
import org.rvsoftworks.commons.model.dto.command.CommandDTO;
import org.rvsoftworks.reem.model.entity.agent.CommandEntity;

import java.time.Instant;

public class CommandMapper {

    public static CommandEntity toEntity(CommandDTO pDTO){
        return CommandEntity.builder()
                .externalId(UuidCreator.getRandomBased())
                .idAgent(pDTO.idAgent())
                .command(pDTO.command())
                .createdAt(Instant.now())
                .build();
    }
}
