package org.rvsoftworks.reem.service;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.PostConstruct;
import org.rvsoftworks.commons.model.dto.agent.AgentDTO;
import org.rvsoftworks.commons.model.dto.agent.CreateAgentDTO;
import org.rvsoftworks.commons.model.heartbeat.HeartbeatDTO;
import org.rvsoftworks.reem.config.properties.AgentProperties;
import org.rvsoftworks.reem.model.dto.ChangeDescriptionRequest;
import org.rvsoftworks.reem.model.dto.agent.GetAgentDTO;
import org.rvsoftworks.reem.model.entity.agent.AgentEntity;
import org.rvsoftworks.reem.repository.AgentRepository;
import org.rvsoftworks.reem.util.SecretUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class AgentService {

    private final AgentRepository repository;
    private final AgentProperties agentProperties;

    List<GetAgentDTO> agents = new ArrayList<>();

    @Autowired
    public AgentService(AgentRepository repository, AgentProperties agentProperties) {
        this.repository = repository;
        this.agentProperties = agentProperties;
    }

    @PostConstruct
    private void loadAll() {
        agents.addAll(repository.findAll());
    }

    public CreateAgentDTO createNewAgent(String pDescription) {
        doNotPermitSameDescription(pDescription);

        AgentEntity entity = AgentEntity.builder()
                .externalId(UuidCreator.getRandomBased())
                .agentSecret(SecretUtil.generateSecret())
                .description(pDescription)
                .build();

        repository.insert(entity);

        AgentDTO agentDTO = new AgentDTO(pDescription, entity.getExternalId(), entity.getAgentSecret());

        CreateAgentDTO dto = getCreateAgentDTO(agentDTO);

        GetAgentDTO newGetDto = new GetAgentDTO(dto.agent().description(), dto.agent().externalId(), null);

        agents.add(newGetDto);

        return dto;
    }

    public List<GetAgentDTO> getAll() {
        return agents.stream().sorted(Comparator.comparing(GetAgentDTO::description)).toList();
    }

    public void changeDescription(ChangeDescriptionRequest pRequest) {
        checkIfExists(pRequest.agentExternalId());

        doNotPermitSameDescription(pRequest.description());

        UUID externalId = UuidCreator.fromString(pRequest.agentExternalId());

        repository.update(externalId, pRequest.description());

        GetAgentDTO dto = new GetAgentDTO(pRequest.description(), externalId, null);

        agents.replaceAll(agentDTO -> agentDTO.externalId().equals(dto.externalId())
                ? new GetAgentDTO(pRequest.description(), agentDTO.externalId(), agentDTO.heartbeat())
                : agentDTO
        );
    }

    public void setHeartBeat(HeartbeatDTO pHeartbeatDTO) {
        checkIfExists(pHeartbeatDTO.agentExternalId());

        agents.replaceAll(agentDTO -> agentDTO.externalId().toString().equals(pHeartbeatDTO.agentExternalId())
                ? new GetAgentDTO(agentDTO.description(), agentDTO.externalId(), pHeartbeatDTO)
                : agentDTO
        );
    }

    public void disableAgent(String pExternalId) {
        checkIfExists(pExternalId);

        UUID uuid = UuidCreator.fromString(pExternalId);

        repository.disable(UuidCreator.fromString(pExternalId));

        agents.removeIf(agentDTO -> agentDTO.externalId().equals(uuid));
    }

    public void checkIfExists(String pExternalId) {
        if (!repository.checkExists(UuidCreator.fromString(pExternalId))) {
            throw new RuntimeException("Agent '%s' não Cadastrado!".formatted(pExternalId));
        }
    }

    private void doNotPermitSameDescription(String pDescription) {
        if (repository.checkDescriptionExists(pDescription)) {
            throw new RuntimeException("Agent com a descrição '%s' ja existe!".formatted(pDescription));
        }
    }

    private CreateAgentDTO getCreateAgentDTO(AgentDTO agentDTO) {
        String scriptToInstallLinux = "wget -q %s/%s/%s -O /tmp/reem_install.sh && chmod +x /tmp/reem_install.sh && sudo /tmp/reem_install.sh http://%s:[PORT] %s %s"
                .formatted(agentProperties.getReleasePage(),
                        agentProperties.getAgentVersion(),
                        agentProperties.getScriptLinuxName(),
                        "[SERVER_URL]",
                        agentDTO.externalId(),
                        agentDTO.secret()
                );

        String scriptToInstallWindows = "Script automatico não implementado!";

        return new CreateAgentDTO(agentDTO, scriptToInstallWindows, scriptToInstallLinux);
    }
}
