CREATE TABLE IF NOT EXISTS application (
                             application_id BIGSERIAL,
                             client_id integer,
                             credit_id integer,
                             status varchar,
                             creation_date timestamp,
                             applied_offer jsonb,
                             sign_date timestamp,
                             ses_code varchar,
                             status_history jsonb,
                             PRIMARY KEY (application_id),
                             FOREIGN KEY (client_id) REFERENCES client (client_id),
                             FOREIGN KEY (credit_id) REFERENCES credit (credit_id)
);

CREATE TABLE IF NOT EXISTS client (
                        client_id BIGSERIAL,
                        last_name varchar NOT NULL,
                        first_name varchar NOT NULL,
                        middle_name varchar,
                        birth_date date NOT NULL,
                        email varchar NOT NULL,
                        gender varchar,
                        marital_status varchar,
                        dependent_amount integer,
                        passport jsonb,
                        employment jsonb,
                        account varchar,
                        PRIMARY KEY (client_id)
);

CREATE TABLE IF NOT EXISTS credit (
                        credit_id BIGSERIAL NOT NULL,
                        amount decimal NOT NULL,
                        term int NOT NULL,
                        monthly_payment decimal NOT NULL,
                        rate decimal NOT NULL,
                        psk decimal NOT NULL,
                        payment_schedule jsonb NOT NULL,
                        insurance_enable boolean NOT NULL,
                        salary_client boolean NOT NULL,
                        credit_status varchar NOT NULL,
                        PRIMARY KEY (credit_id)
);

