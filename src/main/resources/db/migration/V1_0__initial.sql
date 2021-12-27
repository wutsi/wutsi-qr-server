CREATE TABLE T_KEY(
    id            SERIAL NOT NULL,
    tenant_id     BIGINT NOT NULL,
    algorithm     TEXT NOT NULL,
    public_key    TEXT NOT NULL,
    private_key   TEXT NOT NULL,
    active        BOOLEAN NOT NULL,
    created       TIMESTAMPTZ NOT NULL DEFAULT now(),
    expired       TIMESTAMPTZ DEFAULT NULL,

    PRIMARY KEY (id)
);
