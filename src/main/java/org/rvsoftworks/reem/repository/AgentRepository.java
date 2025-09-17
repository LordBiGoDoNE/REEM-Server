package org.rvsoftworks.reem.repository;

import org.rvsoftworks.reem.model.dto.agent.GetAgentDTO;
import org.rvsoftworks.reem.model.entity.agent.AgentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AgentRepository {

    @Autowired
    JdbcClient jdbcClient;

    public AgentRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<GetAgentDTO> findAll() {
        String sql = """
                SELECT
                  	description,
                  	external_id
                FROM
                  	"server".agent
                WHERE
                    disable = FALSE;
                """;

        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    String description = rs.getString("description");
                    UUID externalId = UUID.fromString(rs.getString("external_id"));

                    return new GetAgentDTO(description, externalId, null);
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

        jdbcClient.sql(sql)
                .param("description", entity.getDescription())
                .param("externalId", entity.getExternalId())
                .param("secret", entity.getAgentSecret())
                .update();
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

    public Boolean checkExists(UUID pExternalId) {
        String sql = """
                SELECT EXISTS (
                SELECT 1
                FROM "server".agent
                WHERE external_id = :externalId
                AND disable = False
                );
                """;

        return jdbcClient.sql(sql)
                .param("externalId", pExternalId)
                .query(Boolean.class)
                .single();
    }

    public Boolean checkDescriptionExists(String pDescription) {
        String sql = """
                SELECT EXISTS (
                SELECT 1
                FROM "server".agent
                WHERE description = :description
                AND disable = False
                );
                """;

        return jdbcClient.sql(sql)
                .param("description", pDescription)
                .query(Boolean.class)
                .single();
    }

    public void disable(UUID externalId) {
        String sql = """
                UPDATE
                	"server".agent
                SET
                	disable = TRUE
                WHERE
                	external_id = :externalId;
                """;

        jdbcClient.sql(sql)
                .param("externalId", externalId)
                .update();
    }
}
