package org.rvsoftworks.reem.controller;

import com.github.f4b6a3.uuid.UuidCreator;
import org.rvsoftworks.commons.constants.CommandType;
import org.rvsoftworks.commons.constants.Headers;
import org.rvsoftworks.commons.model.dto.command.CommandDTO;
import org.rvsoftworks.commons.model.dto.command.CommandResultDTO;
import org.rvsoftworks.reem.service.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/command")
public class CommandController {

    private final CommandService service;

    private ConcurrentHashMap<UUID, List<CommandDTO>> map = new ConcurrentHashMap<>();

    @Autowired
    public CommandController(CommandService service) {
        this.service = service;
    }

    @PostMapping
    public String createCommand(@RequestBody CommandDTO pCommand) {
        service.create(pCommand);
        return "Comando definido para " + pCommand.idAgent();
    }

    @GetMapping
    public ResponseEntity<List<CommandDTO>> getCommand(@RequestHeader(Headers.AGENT_ID) String pAgentId,
                                                       @RequestHeader(Headers.AGENT_SECRET) String pAgentSecret) {
        List<CommandDTO> commands = service.get(pAgentId);

        return ResponseEntity.ok(commands);
    }

    @PostMapping("/result")
    public Mono<ResponseEntity<String>> receberResultado(@RequestBody CommandResultDTO resultDTO) {
        service.createResult(resultDTO);

        return Mono.just(ResponseEntity.ok("Resultado inserido com sucesso!"));
    }
}
