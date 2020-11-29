-- Create sequences --
CREATE SEQUENCE users_id_seq MINVALUE 0 START 0 INCREMENT 1;

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
