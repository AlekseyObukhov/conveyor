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
                        last_name varchar,
                        first_name varchar,
                        middle_name varchar,
                        birth_date date,
                        email varchar,
                        gender varchar,
                        marital_status varchar,
                        dependent_amount integer,
                        passport jsonb,
                        employment jsonb,
                        account varchar,
                        PRIMARY KEY (client_id)
);


CREATE TABLE IF NOT EXISTS credit (
                        credit_id BIGSERIAL,
                        amount decimal,
                        term int,
                        monthly_payment decimal,
                        rate decimal,
                        psk decimal,
                        payment_schedule jsonb,
                        insurance_enable boolean,
                        salary_client boolean,
                        credit_status varchar,
                        PRIMARY KEY (credit_id)
);

