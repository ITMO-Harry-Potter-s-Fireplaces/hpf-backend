-- Create sequences --
CREATE SEQUENCE fireplaces_id_seq MINVALUE 0 START 0 INCREMENT 1;
CREATE SEQUENCE claims_id_seq MINVALUE 0 START 0 INCREMENT 1;

-- Create tables --
CREATE TABLE fireplaces(
    id integer DEFAULT nextval('public.fireplaces_id_seq'::regclass) CONSTRAINT fireplaces_pkey PRIMARY KEY,
    lng float NOT NULL,
    lat float NOT NULL,
    description varchar(511),
    owner_id integer NOT NULL
);

CREATE TABLE claims(
    id integer DEFAULT nextval('public.claims_id_seq'::regclass) CONSTRAINT claims_pkey PRIMARY KEY,
    status integer NOT NULL,
    departure_id integer NOT NULL CONSTRAINT dep_claim_fireplace_fk REFERENCES fireplaces(id),
    arrival_id integer NOT NULL CONSTRAINT arr_claim_fireplace_fk REFERENCES fireplaces(id),
    departure_time timestamp NOT NULL,
    user_id integer NOT NULL
);
