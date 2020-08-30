CREATE TABLE users (
  id            SERIAL PRIMARY KEY, -- creates sequence users_id_seq
  version       SMALLINT                      NOT NULL,
  email         CHARACTER VARYING(100) UNIQUE NOT NULL,
  password_hash CHARACTER VARYING(60)         NOT NULL
  -- created_at    TIMESTAMP                     NOT NULL DEFAULT now(),
  --   updated_at TIMESTAMP                     NOT NULL,
);

CREATE TABLE tag (
  id      SERIAL PRIMARY KEY,
  version SMALLINT              NOT NULL,
  user_id INTEGER               NOT NULL REFERENCES users (id),
  name    CHARACTER VARYING(50) NOT NULL,
  --   created_at TIMESTAMP             NOT NULL,
  --   updated_at TIMESTAMP             NOT NULL,
  UNIQUE (user_id, name)
);

-- CREATE TABLE payee (
--   id      SERIAL PRIMARY KEY,
--   user_id INTEGER                NOT NULL REFERENCES users (id),
--   name    CHARACTER VARYING(100) NOT NULL,
--   version SMALLINT               NOT NULL
-- );
--

CREATE TABLE account (
  id           SERIAL PRIMARY KEY,
  version      SMALLINT              NOT NULL,
  created_at   TIMESTAMP             NOT NULL,
  user_id      INTEGER               NOT NULL REFERENCES users (id),
  name         CHARACTER VARYING(30) NOT NULL,
  --   interest_rate NUMERIC(5, 5) NOT NULL,
  opening_balance      NUMERIC(8, 2)         NOT NULL, -- starting balance
  opening_balance_date DATE                  NOT NULL,
  --   interest_direction
  UNIQUE (user_id, name)
);

CREATE TABLE account_interest_rate (
  id                          SERIAL PRIMARY KEY,
  version                     SMALLINT              NOT NULL,
  account_id                  INTEGER               NOT NULL REFERENCES account (id),
  interest_rate               NUMERIC(5, 5)         NOT NULL,
  effective_date              DATE                  NOT NULL,
  expiry_date                 DATE                  NOT NULL,
--   interest_owed               BOOLEAN               NOT NULL, -- true if interest owed, false if interest paid
  interest_calculated_to_date DATE                  NOT NULL --  interest owed/paid to date
);

-- TODO: check constraint to ensure no overlap between effective and expiry

CREATE TABLE account_offset (
  account_id        INTEGER REFERENCES account (id),
  offset_account_id INTEGER REFERENCES account (id),
  PRIMARY KEY (account_id, offset_account_id),
  UNIQUE (account_id) -- ensure we only have one offset
);

CREATE TABLE account_daily_interest (
  id         SERIAL PRIMARY KEY,
  version    SMALLINT      NOT NULL,
  account_id INTEGER       NOT NULL REFERENCES account (id),
  date       DATE          NOT NULL,
  amount     NUMERIC(18, 12) NOT NULL,
  balance    NUMERIC(8, 2) NOT NULL,
  interest_rate NUMERIC(5, 5) NOT NULL,
  UNIQUE (account_id, date)
);

CREATE TABLE transaction (
  id         SERIAL PRIMARY KEY,
  version    SMALLINT      NOT NULL,
  --   user_id  INTEGER       NOT NULL REFERENCES users (id),
  account_id INTEGER       NOT NULL REFERENCES account (id),
  amount     NUMERIC(8, 2) NOT NULL,
  date       DATE          NOT NULL,
  interest_ind BOOLEAN NOT NULL DEFAULT false,
  --   payee_id INTEGER REFERENCES payee (id),
  note       CHARACTER VARYING(255),
  interval_weeks INTEGER NOT NULL DEFAULT 0,
  interval_months INTEGER NOT NULL DEFAULT 0
);

CREATE VIEW account_net_transaction AS
  SELECT
    row_number() OVER () as id,
    t.account_id  AS account_id,
    t.date        AS date,
    SUM(t.amount) AS net_amount
  FROM transaction t
  GROUP BY t.account_id, t.date;

CREATE VIEW account_balance AS
  SELECT
    row_number() OVER () as id,
    a.id  AS account_id,
    a.opening_balance + sum(ant.net_amount) as balance
  FROM account a
  INNER JOIN account_net_transaction ant on ant.account_id = a.id
  -- this would allow updating opening balance and date after transactions are finalised
  -- if performance is an issue
  WHERE ant.date >= a.opening_balance_date
  GROUP BY a.id;

CREATE VIEW account_net_daily_interest AS
  SELECT
    mortgage_interest.account_id,
    mortgage_interest.amount + offset_interest.amount as amount,
    mortgage_interest.balance + offset_interest.balance as balance,
    mortgage_interest.date
  FROM account_daily_interest mortgage_interest
  INNER JOIN account mortgage on mortgage_interest.account_id = mortgage.id
  INNER JOIN account_offset join_table on mortgage.id = join_table.account_id
  INNER JOIN account offset_acc on join_table.offset_account_id = offset_acc.id
  INNER JOIN account_daily_interest offset_interest on offset_acc.id = offset_interest.account_id
    AND offset_interest.date = mortgage_interest.date;

CREATE TABLE transaction_tag (
  transaction_id INTEGER REFERENCES transaction (id),
  tag_id         INTEGER REFERENCES tag (id),
  PRIMARY KEY (transaction_id, tag_id)
);

CREATE TABLE transaction_schedule (
  id         SERIAL PRIMARY KEY,
  version    SMALLINT      NOT NULL,
  account_id INTEGER       NOT NULL REFERENCES account (id),
  amount     NUMERIC(8, 2) NOT NULL,
  start_date DATE          NOT NULL,
  end_date   DATE          NULL,
  note       CHARACTER VARYING(255),
  interval_weeks  INTEGER       NOT NULL,
  interval_months INTEGER NOT NULL
);

CREATE TABLE transaction_schedule_tag (
  transaction_schedule_id INTEGER REFERENCES transaction_schedule (id),
  tag_id                  INTEGER REFERENCES tag (id),
  PRIMARY KEY (transaction_schedule_id, tag_id)
);

CREATE TABLE configuration (
  id          SERIAL PRIMARY KEY,
  version     SMALLINT      NOT NULL,
--   type        CHARACTER VARYING (20),
  key         CHARACTER VARYING (255),
  value       CHARACTER VARYING (255),
  description CHARACTER VARYING (255)
)

/*CREATE TABLE scheduled_transaction (
  id                      SERIAL PRIMARY KEY,
  transaction_schedule_id INTEGER  NOT NULL REFERENCES transaction_schedule (id),
  date                    DATE     NOT NULL,
  version                 SMALLINT NOT NULL
);*/

-- CREATE VIEW payee_tag AS
--   SELECT
--     p.id   AS payee_id,
--     tag.id AS tag_id
--   FROM payee p
--     INNER JOIN transaction t ON t.payee_id = p.id
--     INNER JOIN transaction_tag tt ON tt.transaction_id = t.id
--     INNER JOIN tag ON tag.id = tt.tag_id