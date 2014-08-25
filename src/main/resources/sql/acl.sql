DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- ACL_CLASS

CREATE TABLE acl_class (
    id bigint NOT NULL,
    class character varying(255) NOT NULL
);

CREATE SEQUENCE acl_class_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE acl_class_seq OWNED BY acl_class.id;
ALTER TABLE ONLY acl_class ALTER COLUMN id SET DEFAULT nextval('acl_class_seq'::regclass);
ALTER TABLE ONLY acl_class ADD CONSTRAINT acl_class_pkey PRIMARY KEY (id);


-- ACL_ENTRY

CREATE TABLE acl_entry (
    id bigint NOT NULL,
    acl_object_identity bigint NOT NULL,
    ace_order integer NOT NULL,
    sid bigint NOT NULL,
    mask integer NOT NULL,
    granting boolean DEFAULT false NOT NULL,
    audit_success boolean DEFAULT true NOT NULL,
    audit_failure boolean DEFAULT false NOT NULL
);

CREATE SEQUENCE acl_entry_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE acl_entry_seq OWNED BY acl_entry.id;
ALTER TABLE ONLY acl_entry ALTER COLUMN id SET DEFAULT nextval('acl_entry_seq'::regclass);
ALTER TABLE ONLY acl_entry ADD CONSTRAINT acl_entry_pkey PRIMARY KEY (id);

-- ACL_OBJECT_IDENTITY

CREATE TABLE acl_object_identity (
    id bigint NOT NULL,
    object_id_class bigint NOT NULL,
    object_id_identity bigint NOT NULL,
    parent_object bigint,
    owner_sid bigint,
    entries_inheriting boolean DEFAULT false NOT NULL
);

CREATE SEQUENCE acl_object_identity_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE acl_object_identity_seq OWNED BY acl_object_identity.id;
ALTER TABLE ONLY acl_object_identity ALTER COLUMN id SET DEFAULT nextval('acl_object_identity_seq'::regclass);
ALTER TABLE ONLY acl_object_identity ADD CONSTRAINT acl_object_identity_pkey PRIMARY KEY (id);

-- ACL_SID

CREATE TABLE acl_sid (
    id bigint NOT NULL,
    sid character varying(100) NOT NULL,
    principal boolean DEFAULT false NOT NULL
);

CREATE SEQUENCE acl_sid_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
ALTER SEQUENCE acl_sid_seq OWNED BY acl_sid.id;
ALTER TABLE ONLY acl_sid ALTER COLUMN id SET DEFAULT nextval('acl_sid_seq'::regclass);
ALTER TABLE ONLY acl_sid ADD CONSTRAINT acl_sid_pkey PRIMARY KEY (id);

-- DATA

INSERT INTO acl_class (id, class) VALUES (1, 'net.remisan.security.model.User');
INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (1, 1, 0, 1, 31, true, false, false);
INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (1, 1, 1, NULL, 1, true);
INSERT INTO acl_sid (id, sid, principal) VALUES (1, 'admin', true);

-- SEQUENCES

SELECT pg_catalog.setval('acl_class_seq', 1, true);
SELECT pg_catalog.setval('acl_entry_seq', 1, true);
SELECT pg_catalog.setval('acl_object_identity_seq', 1, true);
SELECT pg_catalog.setval('acl_sid_seq', 1, true);