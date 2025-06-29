
CREATE EXTENSION IF NOT EXISTS "uuid-ossp"  SCHEMA public VERSION "1.1";

CREATE TABLE IF NOT EXISTS public.items
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    description character varying(1024) COLLATE pg_catalog."default",
    id_cloud character varying(255) COLLATE pg_catalog."default",
    quantity bigint,
    flag_showcase boolean,
    CONSTRAINT items_pkey PRIMARY KEY (id)
) TABLESPACE pg_default;
ALTER TABLE IF EXISTS public.items OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.basket
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    id_item uuid NOT NULL,
    quantity smallint,
    CONSTRAINT basket_pkey PRIMARY KEY (id),
    CONSTRAINT basket_id_item_key UNIQUE (id_item),
    CONSTRAINT basket_id_item_fkey FOREIGN KEY (id_item)
        REFERENCES public.items (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
) TABLESPACE pg_default;
ALTER TABLE IF EXISTS public.basket OWNER to postgres;


CREATE TABLE IF NOT EXISTS public.customers
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    first_name character varying(64) COLLATE pg_catalog."default",
    middle_name character varying(64) COLLATE pg_catalog."default",
    last_name character varying(128) COLLATE pg_catalog."default",
    cell_phone character varying(17) COLLATE pg_catalog."default",
    id_basket uuid NOT NULL,
    CONSTRAINT customers_pkey PRIMARY KEY (id),
    CONSTRAINT customers_id_basket_key UNIQUE (id_basket),
    CONSTRAINT customers_id_basket_fkey FOREIGN KEY (id_basket)
        REFERENCES public.basket (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
) TABLESPACE pg_default;
ALTER TABLE IF EXISTS public.customers OWNER to postgres;

