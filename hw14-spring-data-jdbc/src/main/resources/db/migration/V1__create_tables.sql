CREATE TABLE client (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE address (
    id        SERIAL PRIMARY KEY,
    street    VARCHAR(255),
    client_id INTEGER  NOT NULL REFERENCES client(id)
);

CREATE TABLE phone (
    id        SERIAL PRIMARY KEY,
    number    VARCHAR(255),
    client_id INTEGER  NOT NULL REFERENCES client(id)
);
