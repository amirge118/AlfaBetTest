CREATE SCHEMA IF NOT EXISTS my_schema;

CREATE TABLE IF NOT EXISTS my_schema.locations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS my_schema.events (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date TIMESTAMP,
    popularity INT,
    creation_time TIMESTAMP,
    location_id INT,
    FOREIGN KEY (location_id) REFERENCES my_schema.locations(id)
);

CREATE TABLE IF NOT EXISTS my_schema.subscribers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    mail VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS my_schema.subscriptions (
    id SERIAL PRIMARY KEY,
    subscriber_id int,
    event_id int,
    FOREIGN KEY (subscriber_id) REFERENCES my_schema.subscribers(id),
    FOREIGN KEY (event_id) REFERENCES my_schema.events(id)
);

