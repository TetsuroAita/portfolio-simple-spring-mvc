CREATE SCHEMA app;

CREATE TABLE app.avatar (
    id UUID NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    active BOOLEAN NOT NULL,
    original_filename VARCHAR(200) NOT NULL,
    content_type VARCHAR(30) NOT NULL,
    content_size BIGINT NOT NULL,
    changed_stem VARCHAR(200) NOT NULL UNIQUE
);

CREATE TABLE app.profile (
    id UUID NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    active BOOLEAN NOT NULL,
    personal_number VARCHAR(3) NOT NULL,
    last_name VARCHAR(10) NOT NULL,
    first_name VARCHAR(10) NOT NULL,
    last_name_kana VARCHAR(10) NOT NULL,
    first_name_kana VARCHAR(10) NOT NULL,
    gender VARCHAR(6) NOT NULL,
    date_of_birth DATE NOT NULL,
    birthplace VARCHAR(9) NOT NULL,
    memo VARCHAR(200) NOT NULL,
    avatar_id UUID UNIQUE,
    CONSTRAINT fk_prfile_avatar FOREIGN KEY (avatar_id) REFERENCES avatar(id)
);


CREATE TABLE app.sequence (
    name VARCHAR(50) NOT NULL PRIMARY KEY,
    current_value BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);