package org.rvsoftworks.reem.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.rvsoftworks.commons.model.dto.command.CommandDTO;
import org.rvsoftworks.commons.model.dto.command.CommandResultDTO;
import org.rvsoftworks.reem.model.entity.agent.CommandEntity;
import org.rvsoftworks.reem.model.mapper.CommandMapper;
import org.rvsoftworks.reem.repository.AgentRepository;
import org.rvsoftworks.reem.repository.CommandRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommandService {

    private final CommandRepository repository;

    private ConcurrentHashMap<String, List<CommandDTO>> commands;

    public CommandService(CommandRepository repository) {
        this.repository = repository;
    }

    public void create(CommandDTO pCommand) {
        CommandEntity entity = CommandMapper.toEntity(pCommand);

        repository.insert(entity);

        log.info("Command created for Agent ID: {}", pCommand.idAgent().toString());

        commands.get(pCommand.idAgent().toString()).add(pCommand);
    }

    public void get(CommandDTO pCommand) {
        CommandEntity entity = CommandMapper.toEntity(pCommand);

        repository.insert(entity);

        log.info("Command created for Agent ID: {}", pCommand.idAgent().toString());

        commands.get(pCommand.idAgent().toString()).add(pCommand);
    }

    @PostConstruct
    public void getAll(CommandDTO pCommand) {
        commands.putAll(repository.getAllCommandsToSend().stream()
                .collect(Collectors.groupingBy(CommandDTO::idAgentAsString)));

        log.info("Comandos carregados da base de dados!");
    }
}
