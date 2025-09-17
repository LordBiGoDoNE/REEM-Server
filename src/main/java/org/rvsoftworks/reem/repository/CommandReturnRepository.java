package org.rvsoftworks.reem.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import org.rvsoftworks.commons.model.dto.command.CommandDTO;
import org.rvsoftworks.commons.model.dto.command.CommandResultDTO;
import org.rvsoftworks.reem.model.entity.agent.CommandReturnEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CommandReturnRepository {

    @Autowired
    JdbcClient jdbcClient;

    public CommandReturnRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void insert(CommandReturnEntity entity) {
        String sql = """
                INSERT
                	INTO
                	"server".command_return (
                		externalid_command,
                		"return"
                	)
                VALUES(
                	:externalId,
                	:retorno
                );
                """;

        jdbcClient.sql(sql)
                .param("externalId", entity.getExternalId())
                .param("retorno", entity.getRetorno())
                .update();
    }
}
