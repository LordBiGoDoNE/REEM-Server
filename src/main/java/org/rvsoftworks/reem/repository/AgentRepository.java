package org.rvsoftworks.reem.repository;

import org.rvsoftworks.commons.model.dto.agent.AgentDTO;
import org.rvsoftworks.reem.model.entity.agent.AgentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AgentRepository {

    @Autowired
    JdbcClient jdbcClient;

    public AgentRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<AgentDTO> findAll() {
        String sql = """
                SELECT
                  	id,
                  	description,
                  	external_id,
                  	agent_secret
                FROM
                  	"server".agent;
                """;

        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    String description = rs.getString("description");
                    UUID externalId = UUID.fromString(rs.getString("external_id"));
                    String secret = rs.getString("agent_secret");

                    return new AgentDTO(description, externalId, secret);
                })
                .list();
    }

    public void insert(AgentEntity entity) {
        String sql = """
                INSERT
                	INTO
                	"server".agent (
                		description,
                		external_id,
                		agent_secret
                	)
                VALUES (
                	:description,
                	:externalId,
                	:secret
                );
                """;

        GeneratedKeyHolder holder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .param("description", entity.getDescription())
                .param("externalId", entity.getExternalId())
                .param("secret", entity.getAgentSecret())
                .update(holder, new String[]{"id"});

        return Optional.ofNullable(holder.getKeyAs(Integer.class));
    }

    public void update(UUID pExternalId, String pDescription) {
        String sql = """
                UPDATE
                	"server".agent
                SET
                	description = :description,
                WHERE
                	external_id = :externalId;
                """;

        jdbcClient.sql(sql)
                .param("description", pDescription)
                .param("externalId", pExternalId)
                .update();
    }

    public Boolean checkExists(String pDescription) {
        String sql = """
                SELECT EXISTS (
                SELECT 1
                FROM "server".agent
                WHERE description = :description
                );
                """;

        return jdbcClient.sql(sql)
                .param("description", pDescription)
                .query(Boolean.class)
                .single();
    }
}
