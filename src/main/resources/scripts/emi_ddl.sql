CREATE SEQUENCE public.emi_transactions_id_seq
    INCREMENT 1
    START 10
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE public.emi_products_id_seq
    INCREMENT 1
    START 10
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE public.emi_tenures_id_seq
    INCREMENT 1
    START 10
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE public.pre_approved_customers_id_seq
    INCREMENT 1
    START 10
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


-- Table: public.emi_transactions

CREATE TABLE IF NOT EXISTS public.emi_transactions
(
    id integer NOT NULL DEFAULT nextval('emi_transactions_id_seq'::regclass),
    transaction_id character varying(255) COLLATE pg_catalog."default",
    terminal_id character varying(255) COLLATE pg_catalog."default",
    customer_id integer,
    status character varying(255) COLLATE pg_catalog."default",
    product_type character varying(255) COLLATE pg_catalog."default",
    product_sub_type character varying(255) COLLATE pg_catalog."default",
    emi_amount double precision,
    loan_amount double precision,
    interest_rate double precision,
    processing_fee double precision,
    tenure integer,
    tenure_type character varying(255) COLLATE pg_catalog."default",
    invoice_id integer,
    journey_type character varying(255) COLLATE pg_catalog."default",
    additional_data text COLLATE pg_catalog."default",
    created_at timestamp without time zone,
    created_by character varying(255) COLLATE pg_catalog."default",
    updated_at timestamp without time zone,
    updated_by character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT emi_transactions_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;


-- Table: public.emi_products

CREATE TABLE IF NOT EXISTS public.emi_products
(
    id integer NOT NULL DEFAULT nextval('emi_products_id_seq'::regclass),
    product_type character varying(255) COLLATE pg_catalog."default",
    product_sub_type character varying(255) COLLATE pg_catalog."default",
    emi_provider character varying(255) COLLATE pg_catalog."default",
    api character varying(255) COLLATE pg_catalog."default",
    credentials text COLLATE pg_catalog."default",
    callback_url character varying(255) COLLATE pg_catalog."default",
    created_at timestamp without time zone,
    created_by character varying(255) COLLATE pg_catalog."default",
    updated_at timestamp without time zone,
    updated_by character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT emi_products_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;


-- Table: public.emi_tenures

CREATE TABLE IF NOT EXISTS public.emi_tenures
(
    id integer NOT NULL DEFAULT nextval('emi_tenures_id_seq'::regclass),
    product_id integer,
    minimum_transaction_amount integer,
    maximum_transaction_amount integer,
    tenure integer,
    tenure_type character varying(255) COLLATE pg_catalog."default",
    interest_rate double precision,
    processing_fee double precision,
    additional_field text COLLATE pg_catalog."default",
    merchant_fee double precision,
    merchant_fee_type character varying(255) COLLATE pg_catalog."default",
    created_at timestamp without time zone,
    created_by character varying(255) COLLATE pg_catalog."default",
    updated_at timestamp without time zone,
    updated_by character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT emi_tenures_pkey PRIMARY KEY (id),
    CONSTRAINT emi_tenures_id_fkey FOREIGN KEY (product_id)
	REFERENCES public.emi_products (id) MATCH SIMPLE
	ON UPDATE NO ACTION
	ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;



-- Table: public.pre_approved_customer

CREATE TABLE IF NOT EXISTS public.pre_approved_customers
(
    id integer NOT NULL DEFAULT nextval('pre_approved_customers_id_seq'::regclass),
    customer_id integer,
    pre_approved_products text COLLATE pg_catalog."default",
    created_at timestamp without time zone,
    created_by character varying(255) COLLATE pg_catalog."default",
    updated_at timestamp without time zone,
    updated_by character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT pre_approved_customer_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;


