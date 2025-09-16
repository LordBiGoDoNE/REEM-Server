package org.rvsoftworks.reem.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import org.rvsoftworks.commons.constants.CommandType;
import org.rvsoftworks.commons.model.dto.command.CommandDTO;
import org.rvsoftworks.commons.model.dto.command.CommandResultDTO;
import org.rvsoftworks.reem.model.entity.agent.AgentEntity;
import org.rvsoftworks.reem.model.entity.agent.CommandEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CommandRepository {

    @Autowired
    JdbcClient jdbcClient;

    public CommandRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void insert(CommandEntity entity) {
        String sql = """
                INSERT
                  	INTO
                  	"server".command (
                  		externalId,
                  		command,
                  		created_at,
                  		id_agent
                  	)
                VALUES(
                  	:externalId,
                  	:idAgent,
                  	:command,
                  	:createdAt
                );
                """;

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .param("id", entity.getId())
                .param("idAgent", entity.getIdAgent())
                .param("command", entity.getCommand())
                .param("createdAt", entity.getCreatedAt())
                .update(holder, "id");
    }

    public List<CommandResultDTO> getByAgentId(UUID pAgentId) {
        String sql = """
                SELECT
                	c.external_id,
                	command,
                	created_at,
                	id_agent,
                	ct.description AS description_type,
                	cr.return,
                	cr.exitcode,
                	cr.executed_at
                FROM
                	"server".command c
                INNER JOIN "server".command_type ct ON
                	c.id_type = ct.id
                LEFT JOIN "server".command_return cr ON
                	cr.externalid_command = c.external_id
                WHERE
                	id_agent = :idAgent
                """;

        return jdbcClient.sql(sql)
                .param("idAgent", pAgentId)
                .query((rs, rowNum) -> {
                    UUID commandId = UuidCreator.fromString(rs.getString("id"));
                    UUID agentId = UuidCreator.fromString(rs.getString("id_agent"));
                    String command = rs.getString("command");
                    CommandType type = CommandType.getByName(rs.getString("description_type"));
                    OffsetDateTime createdAt = rs.getObject("created_at", OffsetDateTime.class);

                    CommandDTO commandDTO = new CommandDTO(commandId, agentId, command, type, null, createdAt);

                    String commandReturn = rs.getString("return");
                    int exitCode = rs.getInt("exitcode");
                    long executedAt = rs.getLong("executed_at");

                    return new CommandResultDTO(commandDTO, commandReturn, exitCode, executedAt);
                })
                .list();
    }

    public List<CommandDTO> getCommandsToSend(UUID pAgentId) {
        String sql = """
                SELECT
                	c.external_id,
                	command,
                	created_at,
                	id_agent,
                	ct.description AS description_type
                FROM
                	"server".command c
                INNER JOIN "server".command_type ct ON
                	c.id_type = ct.id
                LEFT JOIN "server".command_return cr ON
                	cr.externalid_command = c.external_id
                WHERE
                	id_agent = :idAgent
                	AND cr.externalid_command IS NULL
                """;

        return jdbcClient.sql(sql)
                .param("idAgent", pAgentId)
                .query((rs, rowNum) -> {
                    UUID commandId = UuidCreator.fromString(rs.getString("id"));
                    UUID agentId = UuidCreator.fromString(rs.getString("id_agent"));
                    String command = rs.getString("command");
                    CommandType type = CommandType.getByName(rs.getString("description_type"));
                    OffsetDateTime createdAt = rs.getObject("created_at", OffsetDateTime.class);

                    return new CommandDTO(commandId, agentId, command, type, null, createdAt);
                })
                .list();
    }

    public List<CommandDTO> getAllCommandsToSend() {
        String sql = """
                SELECT
                	c.external_id,
                	command,
                	created_at,
                	id_agent,
                	ct.description AS description_type
                FROM
                	"server".command c
                INNER JOIN "server".command_type ct ON
                	c.id_type = ct.id
                LEFT JOIN "server".command_return cr ON
                	cr.externalid_command = c.external_id
                WHERE
                	cr.externalid_command  IS NULL
                """;

        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    UUID commandId = UuidCreator.fromString(rs.getString("id"));
                    UUID agentId = UuidCreator.fromString(rs.getString("id_agent"));
                    String command = rs.getString("command");
                    CommandType type = CommandType.getByName(rs.getString("description_type"));
                    OffsetDateTime createdAt = rs.getObject("created_at", OffsetDateTime.class);

                    return new CommandDTO(commandId, agentId, command, type, null, createdAt);
                })
                .list();
    }
}
