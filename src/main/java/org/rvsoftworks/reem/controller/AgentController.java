package org.rvsoftworks.reem.controller;

import org.rvsoftworks.commons.constants.Headers;
import org.rvsoftworks.commons.model.dto.agent.CreateAgentDTO;
import org.rvsoftworks.commons.model.heartbeat.HeartbeatDTO;
import org.rvsoftworks.reem.model.dto.ChangeDescriptionRequest;
import org.rvsoftworks.reem.model.dto.agent.GetAgentDTO;
import org.rvsoftworks.reem.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    private final AgentService service;

    public AgentController(AgentService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<GetAgentDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<CreateAgentDTO> create(@RequestBody String pDescription) {
        CreateAgentDTO dto = service.createNewAgent(pDescription);

        return ResponseEntity.ok(dto);
    }

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changeDescription(@RequestBody ChangeDescriptionRequest pRequest) {
        service.changeDescription(pRequest);

        return ResponseEntity.ok("Description changed!");
    }

    @PostMapping(value = "/heartbeat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> heartbeat(@RequestBody HeartbeatDTO pRequest) {
        service.setHeartBeat(pRequest);

        return Mono.just(ResponseEntity.ok("Heartbeat registrado com sucesso!"));
    }

    @DeleteMapping
    public Mono<ResponseEntity<String>> deleteAgent(@RequestHeader(Headers.AGENT_ID) String pAgentId) {
        service.disableAgent(pAgentId);

        return Mono.just(ResponseEntity.ok("Agent excluído com Sucesso!"));
    }
}