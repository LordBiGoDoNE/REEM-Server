package org.rvsoftworks.reem.controller;

import org.rvsoftworks.commons.model.dto.agent.AgentDTO;
import org.rvsoftworks.commons.model.dto.agent.CreateAgentDTO;
import org.rvsoftworks.commons.model.heartbeat.HeartbeatDTO;
import org.rvsoftworks.reem.model.dto.ChangeDescriptionRequest;
import org.rvsoftworks.reem.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/agent")
public class AgentController {

    private ConcurrentHashMap<String, LocalDateTime> map = new ConcurrentHashMap<>();

    @Autowired
    private final AgentService service;

    public AgentController(AgentService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AgentDTO>> getAll() {
        List<AgentDTO> dtos = service.getAll();

        return ResponseEntity.ok(dtos);
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

    @PostMapping(value = "/heatbeat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConcurrentHashMap<String, LocalDateTime>> heatbeat(@RequestBody HeartbeatDTO pRequest) {
        return ResponseEntity.ok(map);
    }
}