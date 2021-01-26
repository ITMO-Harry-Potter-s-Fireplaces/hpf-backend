-- Create sequences --
CREATE SEQUENCE users_id_seq MINVALUE 0 START 0 INCREMENT 1;
CREATE SEQUENCE fireplaces_id_seq MINVALUE 0 START 0 INCREMENT 1;
CREATE SEQUENCE claims_id_seq MINVALUE 0 START 0 INCREMENT 1;
CREATE SEQUENCE auth_logs_id_seq MINVALUE 0 START 0 INCREMENT 1;

-- Create tables --
CREATE TABLE users(
    id integer DEFAULT nextval('public.users_id_seq'::regclass) CONSTRAINT users_pkey PRIMARY KEY,
    email varchar(127) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    role integer NOT NULL,
    name varchar(63),
    surname varchar(63),
    middle_name varchar(63),
    date_birth date,
    active boolean
);

CREATE TABLE fireplaces(
    id integer DEFAULT nextval('public.fireplaces_id_seq'::regclass) CONSTRAINT fireplaces_pkey PRIMARY KEY,
    lat float NOT NULL,
    lng float NOT NULL,
    description varchar(511)
);

CREATE TABLE claims(
    id integer DEFAULT nextval('public.claims_id_seq'::regclass) CONSTRAINT claims_pkey PRIMARY KEY,
    status integer NOT NULL,
    departure_id integer NOT NULL CONSTRAINT dep_claim_fireplace_fk REFERENCES fireplaces(id),
    arrival_id integer NOT NULL CONSTRAINT arr_claim_fireplace_fk REFERENCES fireplaces(id),
    departure_time timestamp NOT NULL,
    user_id integer NOT NULL CONSTRAINT claim_user_fk REFERENCES users(id),
    created timestamp NOT NULL,
    modified timestamp NOT NULL
);

CREATE TABLE auth_logs(
    id integer DEFAULT nextval('public.auth_logs_id_seq'::regclass) CONSTRAINT auth_logs_pkey PRIMARY KEY,
    user_id integer NOT NULL CONSTRAINT auth_log_user_fk REFERENCES users(id),
    date_time timestamp NOT NULL,
    address varchar(127) NOT NULL,
    success boolean
);
