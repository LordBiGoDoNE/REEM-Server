package org.rvsoftworks.reem.service;

import com.github.f4b6a3.uuid.UuidCreator;
import org.rvsoftworks.commons.model.dto.agent.AgentDTO;
import org.rvsoftworks.commons.model.dto.agent.CreateAgentDTO;
import org.rvsoftworks.reem.config.properties.AgentProperties;
import org.rvsoftworks.reem.model.entity.agent.AgentEntity;
import org.rvsoftworks.reem.model.dto.ChangeDescriptionRequest;
import org.rvsoftworks.reem.repository.AgentRepository;
import org.rvsoftworks.reem.util.HostUtil;
import org.rvsoftworks.reem.util.SecretUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AgentService {

    private final AgentRepository repository;
    private final AgentProperties agentProperties;

    ConcurrentHashMap<String, AgentDTO> agents;

    @Autowired
    public AgentService(AgentRepository repository, AgentProperties agentProperties) {
        this.repository = repository;
        this.agentProperties = agentProperties;
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

        String scriptToInstallLinux = "wget -q %s/%s/%s -O /tmp/reem_install.sh && chmod +x /tmp/reem_install.sh && sudo /tmp/reem_install.sh http://%s:[PORT] %s %s"
                .formatted(agentProperties.getReleasePage(),
                        agentProperties.getAgentVersion(),
                        agentProperties.getScriptLinuxName(),
                        HostUtil.getHostIP(),
                        agentDTO.externalId(),
                        agentDTO.secret()
                );

        String scriptToInstallWindows = "Script automatico não implementado!";

        return new CreateAgentDTO(agentDTO, scriptToInstallWindows, scriptToInstallLinux);
    }

    public List<AgentDTO> getAll() {
        return repository.findAll();
    }

    public void changeDescription(ChangeDescriptionRequest pRequest) {
        doNotPermitSameDescription(pRequest.description());

        UUID externalId = UuidCreator.fromString(pRequest.agentExternalId());

        repository.update(externalId, pRequest.description());
    }

    private void doNotPermitSameDescription(String pDescription) {
        if (repository.checkExists(pDescription)) {
            throw new RuntimeException("Agent com a descrição '%s' ja existe!".formatted(pDescription));
        }
    }
}
