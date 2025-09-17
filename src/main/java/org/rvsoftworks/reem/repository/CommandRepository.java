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

import java.sql.Types;
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
                  		external_id,
                  		command,
                  		id_agent
                  	)
                VALUES(
                  	:externalId,
                  	:command,
                  	:idAgent
                );
                """;

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .param("externalId", entity.getExternalId())
                .param("idAgent", entity.getIdAgent())
                .param("command", entity.getCommand())
                .update(holder, "id");
    }

    public List<CommandDTO> getAllCommandsToSend() {
        String sql = """
                SELECT
                	c.external_id,
                	command,
                	id_agent
                FROM
                	"server".command c
                WHERE NOT EXISTS (
                	SELECT 1
                	FROM "server".command_return cr
                	WHERE cr.externalid_command = c.external_id
                )
                """;

        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    UUID commandId = UuidCreator.fromString(rs.getString("external_id"));
                    UUID agentId = UuidCreator.fromString(rs.getString("id_agent"));
                    String command = rs.getString("command");

                    return new CommandDTO(commandId, agentId, command);
                })
                .list();
    }
}
