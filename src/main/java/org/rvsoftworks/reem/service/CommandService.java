package org.rvsoftworks.reem.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.rvsoftworks.commons.model.dto.command.CommandDTO;
import org.rvsoftworks.commons.model.dto.command.CommandResultDTO;
import org.rvsoftworks.reem.model.entity.agent.CommandEntity;
import org.rvsoftworks.reem.model.entity.agent.CommandReturnEntity;
import org.rvsoftworks.reem.model.mapper.CommandMapper;
import org.rvsoftworks.reem.model.mapper.CommandReturnMapper;
import org.rvsoftworks.reem.repository.AgentRepository;
import org.rvsoftworks.reem.repository.CommandRepository;
import org.rvsoftworks.reem.repository.CommandReturnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommandService {

    private final CommandRepository repository;
    private final CommandReturnRepository commandReturnRepository;
    private final AgentService agentService;

    private ConcurrentHashMap<String, List<CommandDTO>> commands = new ConcurrentHashMap<>();

    @Autowired
    public CommandService(CommandRepository repository, CommandReturnRepository commandReturnRepository, AgentService agentService) {
        this.repository = repository;
        this.commandReturnRepository = commandReturnRepository;
        this.agentService = agentService;
    }

    public void create(CommandDTO pCommand) {
        agentService.checkIfExists(pCommand.idAgent().toString());

        CommandEntity entity = CommandMapper.toEntity(pCommand);

        repository.insert(entity);

        log.info("Command created for Agent ID: {}", pCommand.idAgent());

        commands.computeIfAbsent(pCommand.idAgent().toString(), key -> new ArrayList<>()).add(pCommand);
    }

    public void createResult(CommandResultDTO pCommandResult) {
        CommandReturnEntity entity = CommandReturnMapper.toEntity(pCommandResult);

        commandReturnRepository.insert(entity);

        log.info("Resultado recebido com com sucesso: ID Agent:{}", pCommandResult.command().idAgent());
    }

    public List<CommandDTO> get(String pAgentId) {
        agentService.checkIfExists(pAgentId);

        log.info("Obtendo COMANDOS para o Agent {}", pAgentId);

        return commands.remove(pAgentId);
    }

    @PostConstruct
    private void loadAll() {
        commands.putAll(repository.getAllCommandsToSend().stream()
                .collect(Collectors.groupingBy(CommandDTO::idAgentAsString)));

        log.info("Comandos carregados da base de dados!");
    }
}
