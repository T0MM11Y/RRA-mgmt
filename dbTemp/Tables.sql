-- User role definitions
CREATE TABLE role (
    role_id BIGINT PRIMARY KEY, -- Hapus DEFAULT nextval, kita akan define manual
    role_name VARCHAR(255)
);

-- Department/business unit information
CREATE TABLE department (
    department_id BIGINT PRIMARY KEY,
    department_name VARCHAR(100),
    enabled VARCHAR(1) DEFAULT 'Y',
    create_date TIMESTAMP,
    update_date TIMESTAMP,
    bu_tag VARCHAR(10)
);

-- System user accounts
CREATE TABLE account (
    account_id BIGINT PRIMARY KEY, -- Hapus DEFAULT nextval
    user_id VARCHAR(120),
    user_name VARCHAR(120),
    email VARCHAR(255),
    role_id BIGINT REFERENCES role(role_id),
    department_id BIGINT REFERENCES department(department_id),
    approvable VARCHAR(1) DEFAULT 'N',
    enabled VARCHAR(1) DEFAULT 'Y',
    create_date TIMESTAMP,
    create_account BIGINT,
    update_date TIMESTAMP,
    update_account BIGINT,
    mobile VARCHAR(15)
);

-- Navigation menu structure
CREATE TABLE menu (
    menu_id BIGINT PRIMARY KEY, -- Hapus DEFAULT nextval
    menu_name VARCHAR(150),
    order_no INTEGER,
    create_date TIMESTAMP,
    create_account BIGINT,
    update_date TIMESTAMP,
    update_account BIGINT
);

-- Program/submenu items under each menu
CREATE TABLE program (
    program_id BIGINT PRIMARY KEY, -- Hapus DEFAULT nextval
    menu_id BIGINT REFERENCES menu(menu_id),
    order_no INTEGER,
    program_name VARCHAR(150),
    program_uri VARCHAR(255),
    create_date TIMESTAMP,
    create_account BIGINT,
    update_date TIMESTAMP,
    update_account BIGINT
);

-- Role-based program permissions
CREATE TABLE role_permission_program (
    permission_id BIGINT PRIMARY KEY DEFAULT nextval('role_permission_program_seq'),
    role_id BIGINT REFERENCES role(role_id),
    program_id BIGINT REFERENCES program(program_id),
    create_date TIMESTAMP,
    create_account BIGINT
);

-- Individual account program permissions (overrides role permissions)
CREATE TABLE account_permission_program (
    permission_id BIGINT PRIMARY KEY DEFAULT nextval('account_permission_program_seq'),
    account_id BIGINT REFERENCES account(account_id),
    program_id BIGINT REFERENCES program(program_id),
    enabled VARCHAR(1) DEFAULT 'N',
    create_date TIMESTAMP,
    create_account BIGINT
);

-- User action audit trail
CREATE TABLE account_action_history (
    account_action_history_id BIGINT PRIMARY KEY DEFAULT nextval('account_action_history_seq'),
    execute_content TEXT,
    request_id VARCHAR(100),
    account_id BIGINT NOT NULL,
    execute_account_id BIGINT NOT NULL,
    execute_date TIMESTAMP NOT NULL
);

-- User login tracking
CREATE TABLE login_history (
    login_history_id BIGINT PRIMARY KEY DEFAULT nextval('login_history_seq'),
    login_date TIMESTAMP,
    account_id BIGINT REFERENCES account(account_id)
);

-- Regional telecom operators (RRA members)
-- KOLOM 'enabled' DIHAPUS
CREATE TABLE opco (
    opco_id TEXT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    country VARCHAR(100),
    create_date TIMESTAMP,
    create_account BIGINT,
    update_date TIMESTAMP,
    update_account BIGINT
);
COMMENT ON TABLE opco IS 'Operator master table (expandable for future RRA members)';

-- Customer profile data for TWM users
CREATE TABLE user_profile (
    twm_uid VARCHAR(20) PRIMARY KEY,
    aes_twm_uid VARCHAR(100),
    subid VARCHAR(20),
    msisdn VARCHAR(15),
    point_type VARCHAR(20),
    point_uid VARCHAR(20),
    identity_type VARCHAR(20),
    tier VARCHAR(20),
    total_point NUMERIC(22),
    create_date TIMESTAMP,
    update_date TIMESTAMP
);
COMMENT ON TABLE user_profile IS 'Customer profile data. Key for Customer Care search. Staff searches by (msisdn, subid, twm_uid). System joins using user_profile.aes_twm_uid = transaction_record.identity_value.';

-- Customer authentication tokens
CREATE TABLE user_tokens (
    token_seq BIGINT PRIMARY KEY DEFAULT nextval('user_tokens_seq'),
    twm_uid VARCHAR(20) REFERENCES user_profile(twm_uid),
    aes_twm_uid VARCHAR(100),
    user_auth_token VARCHAR(200),
    status VARCHAR(30),
    last_interaction_time TIMESTAMP,
    expired_time TIMESTAMP,
    create_date TIMESTAMP,
    update_date TIMESTAMP
);
COMMENT ON TABLE user_tokens IS 'Customer authentication tokens, linking auth tokens to TWM UID.';

-- Reward transaction records (HOProcessed/COProcessed)
-- KOLOM 'enabled' DIHAPUS
CREATE TABLE transaction_record (
    id BIGINT PRIMARY KEY DEFAULT nextval('transaction_record_seq'),
    report_type TEXT NOT NULL CHECK (report_type IN ('HOProcessed', 'COProcessed')),
    report_month DATE NOT NULL,
    source_file TEXT NOT NULL,
    row_no INTEGER NOT NULL,
    row_hash TEXT NOT NULL,
    transaction_id TEXT NOT NULL,
    transaction_time TIMESTAMPTZ,
    order_id TEXT,
    user_id TEXT,
    catalog_owner TEXT REFERENCES opco(opco_id),
    home_opco TEXT REFERENCES opco(opco_id),
    reward_id TEXT,
    external_reward_id TEXT,
    reward_type TEXT,
    reward_name TEXT,
    reward_quantity INTEGER,
    price_point NUMERIC(20,6),
    price_cash NUMERIC(20,6),
    price_currency TEXT,
    default_margin_percentage NUMERIC(5,2),
    additional_margin_percentage NUMERIC(5,2),
    discount_percentage NUMERIC(5,2),
    catalog_owner_currency_exchange_rate NUMERIC(20,10),
    catalog_owner_point_to_currency_ratio NUMERIC(20,10),
    home_opco_currency_exchange_rate NUMERIC(20,10),
    home_opco_point_to_currency_ratio NUMERIC(20,10),
    transaction_type TEXT CHECK (transaction_type IN ('payment', 'refund')),
    payment_method TEXT,
    payment_point NUMERIC(20,6),
    payment_cash NUMERIC(20,6),
    payment_currency TEXT,
    refund_original_transaction_id TEXT,
    refund_point NUMERIC(20,6),
    refund_cash NUMERIC(20,6),
    refund_currency TEXT,
    co_time TIMESTAMPTZ,
    ho_time TIMESTAMPTZ,
    identity_type TEXT,
    identity_value TEXT,
    ban TEXT,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_account BIGINT,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_account BIGINT,
    CONSTRAINT uq_sourcefile_rowno UNIQUE (source_file, row_no),
    CONSTRAINT uq_row_hash UNIQUE (row_hash)
);
COMMENT ON TABLE transaction_record IS 'Stores all HOProcessed/COProcessed reward transactions from monthly reports.';

COMMIT;