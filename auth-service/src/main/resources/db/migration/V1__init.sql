CREATE TYPE role_enum AS ENUM ('USER', 'ADMIN');

CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    username   VARCHAR(50) UNIQUE  NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(50),
    last_name  VARCHAR(50),
    role       VARCHAR(50)         NOT NULL
);

CREATE TABLE refresh_tokens
(
    id         UUID PRIMARY KEY,
    user_id    UUID                        NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_refresh_token_user_id ON refresh_tokens (user_id);

INSERT INTO users (id, username, password, email, first_name, last_name, role)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'admin', '$2a$14$ygS/eP43CRg5a/chM9vDZu.Src07WpgFHctHYiY/VhFWQycyo0pXW', 'admin@example.com', 'Admin', 'User', 'ADMIN'),
    ('22222222-2222-2222-2222-222222222222', 'john_doe', '$2a$12$TCIm9MTwX85Pjsz26..PJ.hluh9zuoWUiuv6HdvfJ5oEcB7pDOo/K', 'user@example.com', 'John', 'Doe', 'USER');

INSERT INTO refresh_tokens (id, user_id, created_at, expires_at)
VALUES (
           '33333333-3333-3333-3333-333333333333',
           '22222222-2222-2222-2222-222222222222',
           NOW(),
           NOW() + INTERVAL '30 days'
       );
