DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS customer;

DROP SEQUENCE IF EXISTS customer_seq;
DROP SEQUENCE IF EXISTS tag_seq;

CREATE SEQUENCE customer_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE SEQUENCE tag_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE TABLE tag(
    id bigint NOT NULL DEFAULT NEXTVAL('tag_seq'),
    label character varying (255),
    CONSTRAINT tag_pk_id PRIMARY KEY (id)
);

CREATE TABLE customer(
    id bigint NOT NULL DEFAULT NEXTVAL('customer_seq'),
    first_name character varying (255),
    last_name character varying (255),
    email character varying (255),
    type character varying(255),
    onboarded_on timestamp without time zone,
    contacted_on timestamp without time zone,
    created_on timestamp without time zone,
    CONSTRAINT customer_pk_id PRIMARY KEY (id),
    CONSTRAINT email_uq_name UNIQUE (email)
);

CREATE TABLE customer_tags (
    customer_id bigint NOT NULL,
    tag_id bigint NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    PRIMARY KEY (customer_id, tag_id)
);

CREATE TABLE customer_attributes (
    customer_id bigint NOT NULL,
    name character varying (255),
    value character varying (255),
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (customer_id, name)
);
