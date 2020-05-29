-- Create sequences --
CREATE SEQUENCE roles_id_seq MINVALUE 0 START 0 INCREMENT 1;
CREATE SEQUENCE users_id_seq MINVALUE 0 START 0 INCREMENT 1;

-- Create tables --
CREATE TABLE roles(
    id integer DEFAULT nextval('public.roles_id_seq'::regclass) CONSTRAINT roles_pkey PRIMARY KEY,
    name varchar(63) UNIQUE NOT NULL
);

CREATE TABLE users(
    id integer DEFAULT nextval('public.users_id_seq'::regclass) CONSTRAINT users_pkey PRIMARY KEY,
    email varchar(127) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    role_id integer NOT NULL CONSTRAINT user_role_fk REFERENCES roles(id),
    name varchar(63),
    surname varchar(63),
    middle_name varchar(63),
    date_birth date,
    active boolean
);
