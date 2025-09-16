package org.rvsoftworks.reem.controller;

import com.github.f4b6a3.uuid.UuidCreator;
import org.rvsoftworks.commons.constants.CommandType;
import org.rvsoftworks.commons.model.dto.command.CommandDTO;
import org.rvsoftworks.commons.model.dto.command.CommandResultDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/command")
public class CommandController {

    private ConcurrentHashMap<UUID, List<CommandDTO>> map = new ConcurrentHashMap<>();

    @PostMapping("/set/{agentId}")
    public String definirComando(@PathVariable String pAgentId, @RequestBody String pCommand) {
        return "Comando definido para " + pAgentId;
    }

    @GetMapping
    public ResponseEntity<List<CommandDTO>> getCommand(@RequestHeader("X-Agent-ID") String pAgentId,
                                                       @RequestHeader("X-Agent-Secret") String pAgentSecret) {

        UUID agentId = UuidCreator.fromString(pAgentId);

        CommandDTO dto = new CommandDTO(UUID.randomUUID(), agentId, "ping www.google.com", CommandType.RUN_COMMAND, Duration.ofSeconds(50), OffsetDateTime.now());

        ConcurrentHashMap<String, List<CommandDTO>> map = new ConcurrentHashMap<>();
        map.put(pAgentId, List.of(dto));

        ResponseEntity<List<CommandDTO>> response;

        if (map.get(pAgentId).isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(map.remove(pAgentId));
    }

    @PostMapping
    public String receberResultado(@RequestBody CommandResultDTO agentReturn) {
        System.out.println("Saída de " + agentReturn.command()+ ":\n" + agentReturn.result());
        return "OK";
    }
}
