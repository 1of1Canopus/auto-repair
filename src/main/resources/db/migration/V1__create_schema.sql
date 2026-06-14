CREATE TABLE quotes (
    id                  CHAR(36) PRIMARY KEY,
    customer_name       VARCHAR(255) NOT NULL,
    customer_email      VARCHAR(255) NOT NULL,
    vrm                 VARCHAR(32)  NOT NULL,
    vehicle_description VARCHAR(255) NOT NULL,
    mileage             INTEGER      NOT NULL,
    date_created        TIMESTAMP    NOT NULL
);

CREATE TABLE jobs (
    id                  CHAR(36) PRIMARY KEY,
    quote_id            CHAR(36)      NOT NULL REFERENCES quotes (id),
    job_code            VARCHAR(16)   NOT NULL UNIQUE,
    job_description     VARCHAR(255)  NOT NULL,
    labour_time         NUMERIC(10, 2) NOT NULL,
    labour_rate         NUMERIC(10, 2) NOT NULL,
    overridden_price    NUMERIC(12, 2),
    customer_authorized BOOLEAN       NOT NULL,
    date_created        TIMESTAMP     NOT NULL
);

CREATE TABLE parts (
    id               CHAR(36) PRIMARY KEY,
    job_id           CHAR(36)       NOT NULL REFERENCES jobs (id),
    type             VARCHAR(16)    NOT NULL,
    part_number      VARCHAR(64)    NOT NULL,
    part_description VARCHAR(255)   NOT NULL,
    quantity         INTEGER        NOT NULL,
    unit_cost        NUMERIC(12, 2) NOT NULL,
    date_created     TIMESTAMP      NOT NULL
);

CREATE INDEX idx_jobs_quote_id ON jobs (quote_id);
CREATE INDEX idx_parts_job_id ON parts (job_id);
