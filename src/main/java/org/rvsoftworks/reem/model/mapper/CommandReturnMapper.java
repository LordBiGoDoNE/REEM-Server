package org.rvsoftworks.reem.model.mapper;

import com.github.f4b6a3.uuid.UuidCreator;
import org.rvsoftworks.commons.model.dto.command.CommandDTO;
import org.rvsoftworks.commons.model.dto.command.CommandResultDTO;
import org.rvsoftworks.reem.model.entity.agent.CommandEntity;
import org.rvsoftworks.reem.model.entity.agent.CommandReturnEntity;

import java.time.Instant;

public class CommandReturnMapper {

    public static CommandReturnEntity toEntity(CommandResultDTO pDTO){
        return CommandReturnEntity.builder()
                .externalId(pDTO.command().externalId())
                .retorno(pDTO.result())
                .build();
    }
}
