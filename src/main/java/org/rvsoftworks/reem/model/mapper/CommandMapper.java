package org.rvsoftworks.reem.model.mapper;

import com.github.f4b6a3.uuid.util.UuidUtil;
import org.rvsoftworks.commons.model.dto.command.CommandDTO;
import org.rvsoftworks.reem.model.entity.agent.CommandEntity;

public class CommandMapper {

    public static CommandEntity toEntity(CommandDTO pDTO){
        return CommandEntity.builder()
                .externalId(pDTO.externalId())
                .idAgent(pDTO.idAgent())
                .command(pDTO.command())
                .createdAt(UuidUtil.getInstant(pDTO.externalId()))
                .build();
    }
}
