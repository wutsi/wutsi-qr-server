CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE T_KEY;
CREATE TABLE T_KEY(
    id            SERIAL NOT NULL,
    algorithm     TEXT NOT NULL,
    private_key   VARCHAR(36) NOT NULL,
    active        BOOLEAN NOT NULL,
    created       TIMESTAMPTZ NOT NULL DEFAULT now(),
    expired       TIMESTAMPTZ DEFAULT NULL,

    PRIMARY KEY (id)
);
