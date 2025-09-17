CREATE TABLE "server".agent (
	id serial NOT NULL,
	description varchar NULL,
	external_id uuid NOT NULL,
	agent_secret varchar NULL,
	"disable" boolean DEFAULT false NULL,
	CONSTRAINT agent_unique UNIQUE (external_id),
	CONSTRAINT agent_pk PRIMARY KEY (id)
);

CREATE TABLE "server".command (
	id serial NOT NULL,
	external_id uuid NOT NULL,
	command text NOT NULL,
	id_agent uuid NOT NULL,
	CONSTRAINT command_pk PRIMARY KEY (id),
	CONSTRAINT command_unique UNIQUE (external_id),
	CONSTRAINT command_agent_fk FOREIGN KEY (id_agent) REFERENCES "server".agent(external_id)
);

CREATE TABLE "server".command_return (
	id serial NOT NULL,
	externalid_command uuid NOT NULL,
	"return" text NOT NULL,
	CONSTRAINT command_return_pk PRIMARY KEY (id),
	CONSTRAINT command_return_command_fk FOREIGN KEY (externalid_command) REFERENCES "server".command(external_id)
);

