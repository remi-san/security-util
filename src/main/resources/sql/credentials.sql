DROP SCHEMA IF EXISTS credentials CASCADE;
CREATE SCHEMA credentials;

-- ROLES

CREATE TABLE credentials.roles (
    role_id integer NOT NULL,
    role_name character varying(20) NOT NULL
);

CREATE SEQUENCE credentials.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE credentials.roles_id_seq OWNED BY credentials.roles.role_id;

-- USER_ROLES

CREATE TABLE credentials.user_roles (
    user_id integer NOT NULL,
    role_id integer NOT NULL
);

-- USERS

CREATE TABLE credentials.users (
    user_id integer NOT NULL,
    user_login character varying(50) NOT NULL,
    user_email character varying(255) NOT NULL,
    user_password character varying(50) NOT NULL,
    user_enabled boolean DEFAULT false,
    user_activation_token character varying(50),
    user_creation_date timestamp with time zone,
    user_account_expiration_date timestamp with time zone,
    user_credentials_expiration_date timestamp with time zone,
    user_locking_date timestamp with time zone
);

CREATE SEQUENCE credentials.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE credentials.users_id_seq OWNED BY credentials.users.user_id;

-- SERIALS

ALTER TABLE ONLY credentials.roles ALTER COLUMN role_id SET DEFAULT nextval('credentials.roles_id_seq'::regclass);

ALTER TABLE ONLY credentials.users ALTER COLUMN user_id SET DEFAULT nextval('credentials.users_id_seq'::regclass);

-- PRIMARY KEYS

ALTER TABLE ONLY credentials.roles ADD CONSTRAINT roles_pkey PRIMARY KEY (role_id);

ALTER TABLE ONLY credentials.user_roles ADD CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id);

ALTER TABLE ONLY credentials.users ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


-- UNIQUE KEYS

ALTER TABLE ONLY credentials.roles ADD CONSTRAINT roles_name_key UNIQUE (role_name);

ALTER TABLE ONLY credentials.users ADD CONSTRAINT users_login_key UNIQUE (user_login);

ALTER TABLE ONLY credentials.users ADD CONSTRAINT users_email_key UNIQUE (user_email);

ALTER TABLE ONLY credentials.users ADD CONSTRAINT users_token_key UNIQUE (user_activation_token);

-- FOREIGN KEYS

ALTER TABLE ONLY credentials.user_roles
    ADD CONSTRAINT user_roles_role_id_fkey FOREIGN KEY (role_id) REFERENCES credentials.roles(role_id);

ALTER TABLE ONLY credentials.user_roles
    ADD CONSTRAINT user_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES credentials.users(user_id);
    
-- DATA

INSERT INTO credentials.roles (role_id, role_name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO credentials.roles (role_id, role_name) VALUES (2, 'ROLE_MODERATOR');
INSERT INTO credentials.roles (role_id, role_name) VALUES (3, 'ROLE_USER');
INSERT INTO credentials.roles (role_id, role_name) VALUES (4, 'ROLE_VISITOR');

INSERT INTO credentials.users
    (user_id, user_login, user_email, user_password, user_enabled, user_activation_token)
    VALUES (1, 'admin', 'admin@dummy.com', 'ceb4f32325eda6142bd65215f4c0f371', true, 'bfb8e810-c22d-11e3-8a33-0800200c9a66');

INSERT INTO credentials.user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO credentials.user_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO credentials.user_roles (user_id, role_id) VALUES (1, 3);

SELECT pg_catalog.setval('credentials.roles_id_seq', 4, true);
SELECT pg_catalog.setval('credentials.users_id_seq', 1, true);