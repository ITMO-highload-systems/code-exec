CREATE TABLE "user"
(
    user_id  SERIAL PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE note
(
    note_id     SERIAL PRIMARY KEY,
    owner       INT REFERENCES "user" (user_id),
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE paragraph
(
    id                  SERIAL PRIMARY KEY,
    note_id             INT REFERENCES note (note_id),
    title               VARCHAR(255),
    next_paragraph_id   INT REFERENCES paragraph (id),
    text                TEXT NOT NULL,
    last_update_user_id INT REFERENCES "user" (user_id),
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    paragraph_type      varchar(255)
);

CREATE TABLE execution_code_result
(
    paragraph_id     INT PRIMARY KEY REFERENCES paragraph (id),
    execution_result text
);

-- Добавляем пользователя
INSERT INTO "user" (email, password)
VALUES ('testuser@example.com', 'password');

-- Добавляем заметку
INSERT INTO note (owner, title, description)
VALUES (1, 'Test Note', 'A sample note for testing.');

-- Добавляем абзац
INSERT INTO paragraph (note_id, title, text, last_update_user_id, paragraph_type)
VALUES (1, 'Sample Paragraph', 'This is a sample text.', 1, 'PLAIN_TEXT_PARAGRAPH');