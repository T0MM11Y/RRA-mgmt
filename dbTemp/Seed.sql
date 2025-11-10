-- Insert RRA member operators (Kolom 'enabled' dihapus)
INSERT INTO opco (opco_id, name, country, create_date, create_account, update_date, update_account)
VALUES
('STM', 'Singtel', 'Singapore', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
('AIS', 'AIS', 'Thailand', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
('GLB', 'Globe', 'Philippines', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
('OTS', 'Optus', 'Australia', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
('TWM', 'Taiwan Mobile', 'Taiwan', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
('HKT', 'HKT', 'Hong Kong', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
('KDDI', 'KDDI', 'Japan', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
('TSE', 'Telkomsel', 'Indonesia', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1);

-- Insert system roles (HANYA 4 ROLE DARI GAMBAR)
INSERT INTO role (role_id, role_name)
VALUES
(1, 'ROLE_SYSTEM_ADMIN'),
(21, 'ROLE_PRODUCT_PM'),
(22, 'ROLE_ACCOUNTANT'),
(100, 'ROLE_CUSTOMER_CARE');

-- Insert navigation menus
INSERT INTO menu (menu_id, menu_name, order_no, create_date, create_account, update_date, update_account)
VALUES
(1, 'Home', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
(2, 'WanderJoy Portal', 2, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
(3, 'WanderJoy Web', 3, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
(4, 'Customer Care', 4, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
(5, 'Report', 5, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
(6, 'Admin', 6, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1);

-- Insert program/submenu items
INSERT INTO program (program_id, menu_id, order_no, program_name, program_uri, create_date, create_account, update_date, update_account)
VALUES
(101, 1, 1, 'Home', '/', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
(201, 2, 1, 'WanderJoy Portal', 'https://portal.reward-plus.com/welcome', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
(301, 3, 1, 'WanderJoy Web', 'https://www.wanderjoy.com/', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
(402, 4, 1, 'Customer Care', '/customer/care', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
(501, 5, 1, 'Settlement Inquiry', '/report/settlement', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),
(601, 6, 1, 'Account Management', '/account', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1);

-- Departments (HANYA YANG DIBUTUHKAN AKUN TERKAIT)
INSERT INTO department (department_id, department_name, enabled, create_date, update_date, bu_tag)
VALUES
(1, '服務整合暨管理部', 'Y', TO_TIMESTAMP('31-MAR-21 04:48:39 PM', 'DD-MON-YY HH12:MI:SS AM'), TO_TIMESTAMP('31-MAR-21 04:50:42 PM', 'DD-MON-YY HH12:MI:SS AM'), 'CBG'),
(5, '帳務處', 'Y', TO_TIMESTAMP('31-MAR-21 04:51:48 PM', 'DD-MON-YY HH12:MI:SS AM'), TO_TIMESTAMP('31-MAR-21 05:10:30 PM', 'DD-MON-YY HH12:MI:SS AM'), NULL),
(10, '客戶開發暨維繫處2_ALMD', 'Y', TO_TIMESTAMP('21-APR-21 10:21:41 AM', 'DD-MON-YY HH12:MI:SS AM'), TO_TIMESTAMP('23-JUN-22 05:56:53 PM', 'DD-MON-YY HH12:MI:SS AM'), 'CBG');

-- Accounts (HANYA 4 AKUN PERWAKILAN ROLE)
INSERT INTO account (account_id, user_id, user_name, email, role_id, department_id, approvable, enabled, create_date, create_account, update_date, update_account, mobile)
VALUES
(1, 'YvetteYang', 'Yvette Yang', 'YvetteYang@taiwanmobile.com', 1, 1, 'Y', 'Y', TO_TIMESTAMP('09-APR-21 02:26:03 PM', 'DD-MON-YY HH12:MI:SS AM'), 1, TO_TIMESTAMP('01-FEB-24 04:52:18 PM', 'DD-MON-YY HH12:MI:SS AM'), 65, NULL),
(61, 'VDAliceChang', 'VDAlice Chang', 'VDAliceChang@taiwanmobile.com', 100, 1, 'Y', 'Y', TO_TIMESTAMP('08-JUN-22 09:45:46 AM', 'DD-MON-YY HH12:MI:SS AM'), 1, TO_TIMESTAMP('15-NOV-22 01:59:27 PM', 'DD-MON-YY HH12:MI:SS AM'), 1, NULL),
(65, 'KevinShih', 'KevinShih', 'KevinShih@taiwanmobile.com', 21, 10, 'Y', 'Y', TO_TIMESTAMP('10-NOV-23 10:15:00 AM', 'DD-MON-YY HH12:MI:SS AM'), 1, TO_TIMESTAMP('02-JAN-25 04:13:40 PM', 'DD-MON-YY HH12:MI:SS AM'), 65, '0935500183'),
(69, 'pennyhuang', 'penny huang', 'pennyhuang@taiwanmobile.com', 22, 5, 'N', 'Y', TO_TIMESTAMP('20-APR-23 04:05:55 PM', 'DD-MON-YY HH12:MI:SS AM'), 1, NULL, NULL, NULL);

-- Role-based permissions (SESUAI permintaan yvett)
INSERT INTO role_permission_program (permission_id, role_id, program_id, create_date, create_account)
VALUES
-- ROLE_SYSTEM_ADMIN (ID 1) -> Home, Acct Mgmt.
(NEXTVAL('role_permission_program_seq'), 1, 101, CURRENT_TIMESTAMP, 1),
(NEXTVAL('role_permission_program_seq'), 1, 601, CURRENT_TIMESTAMP, 1),

-- ROLE_CUSTOMER_CARE (ID 100) -> Home, Customer Care
(NEXTVAL('role_permission_program_seq'), 100, 101, CURRENT_TIMESTAMP, 1),
(NEXTVAL('role_permission_program_seq'), 100, 402, CURRENT_TIMESTAMP, 1),

-- ROLE_PRODUCT_PM (ID 21) -> Home, Customer Care, Reports
(NEXTVAL('role_permission_program_seq'), 21, 101, CURRENT_TIMESTAMP, 1),
(NEXTVAL('role_permission_program_seq'), 21, 402, CURRENT_TIMESTAMP, 1),
(NEXTVAL('role_permission_program_seq'), 21, 501, CURRENT_TIMESTAMP, 1),

-- ROLE_ACCOUNTANT (ID 22) -> Home, Reports
(NEXTVAL('role_permission_program_seq'), 22, 101, CURRENT_TIMESTAMP, 1),
(NEXTVAL('role_permission_program_seq'), 22, 501, CURRENT_TIMESTAMP, 1);

-- Grant WanderJoy Portal (program_id 201) and WanderJoy Web (program_id 301) to ALL roles
INSERT INTO role_permission_program (permission_id, role_id, program_id, create_date, create_account) VALUES
    (NEXTVAL('role_permission_program_seq'), 1, 201, CURRENT_TIMESTAMP, 1),  -- SYSTEM_ADMIN -> WanderJoy Portal
    (NEXTVAL('role_permission_program_seq'), 1, 301, CURRENT_TIMESTAMP, 1),  -- SYSTEM_ADMIN -> WanderJoy Web
    (NEXTVAL('role_permission_program_seq'), 21, 201, CURRENT_TIMESTAMP, 1), -- PRODUCT_PM -> WanderJoy Portal
    (NEXTVAL('role_permission_program_seq'), 21, 301, CURRENT_TIMESTAMP, 1), -- PRODUCT_PM -> WanderJoy Web
    (NEXTVAL('role_permission_program_seq'), 22, 201, CURRENT_TIMESTAMP, 1), -- ACCOUNTANT -> WanderJoy Portal
    (NEXTVAL('role_permission_program_seq'), 22, 301, CURRENT_TIMESTAMP, 1), -- ACCOUNTANT -> WanderJoy Web
    (NEXTVAL('role_permission_program_seq'), 100, 201, CURRENT_TIMESTAMP, 1),-- CUSTOMER_CARE -> WanderJoy Portal
    (NEXTVAL('role_permission_program_seq'), 100, 301, CURRENT_TIMESTAMP, 1);-- CUSTOMER_CARE -> WanderJoy Web
-- ============================================
-- Sample transaction records (aligned with Yvette's specification)
-- ============================================
INSERT INTO transaction_record (
    report_type, report_month, source_file, row_no, row_hash,
    transaction_id, transaction_time, order_id, user_id,
    catalog_owner, home_opco, reward_id, external_reward_id,
    reward_type, reward_name, reward_quantity,
    price_point, price_cash, price_currency,
    default_margin_percentage, additional_margin_percentage, discount_percentage,
    catalog_owner_currency_exchange_rate, catalog_owner_point_to_currency_ratio,
    home_opco_currency_exchange_rate, home_opco_point_to_currency_ratio,
    transaction_type, payment_method, payment_point, payment_cash, payment_currency,
    refund_original_transaction_id, refund_point, refund_cash, refund_currency,
    co_time, ho_time, identity_type, identity_value, ban,
    create_date, create_account, update_date, update_account
) VALUES
-- User: twm_uid_001 (Tier3)
('HOProcessed', '2025-08-01', 'Monthly_TWM_2025-08.csv', 1, 'hash_TWM_001',
 'txn_001', '2025-08-15 12:00:00+00', 'order_001', 'u_twm_1',
 'STM', 'TWM', 'rw_001', 'ext_rw_001', 'coupon', 'Coffee Bean eVoucher', 1,
 120, 0, 'SGD', 5.00, 0.50, 10.00, 32.81, 1.28, 1.27, 32.81,
 'payment', 'point', 120, 0, 'SGD', NULL, NULL, NULL, NULL,
 '2025-08-15 13:00:00+00', '2025-08-15 14:00:00+00', 'phone', 'enc_val_001', 'TWM123100',
 CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),

('HOProcessed', '2025-09-01', 'Monthly_TWM_2025-09.csv', 2, 'hash_TWM_002',
 'txn_002', '2025-09-10 09:20:00+00', 'order_002', 'u_twm_1',
 'STM', 'TWM', 'rw_002', 'ext_rw_002', 'coupon', 'Bubble Tea Voucher', 1,
 30, 1.50, 'SGD', 5.00, 0.50, 0.00, 32.81, 1.28, 1.27, 32.81,
 'payment', 'credit_card+point', 30, 1.50, 'SGD', NULL, NULL, NULL, NULL,
 '2025-09-10 10:20:00+00', '2025-09-10 11:20:00+00', 'phone', 'enc_val_001', 'TWM123100',
 CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),

('HOProcessed', '2025-10-01', 'Monthly_TWM_2025-10.csv', 3, 'hash_TWM_003',
 'txn_003', '2025-10-05 11:10:00+00', 'order_003', 'u_twm_1',
 'STM', 'TWM', 'rw_003', 'ext_rw_003', 'coupon', 'Digital Gift Card', 1,
 0, 25.00, 'SGD', 5.00, 0.50, 5.00, 32.81, 1.28, 1.27, 32.81,
 'payment', 'credit_card', 0, 25.00, 'SGD', NULL, NULL, NULL, NULL,
 '2025-10-05 12:10:00+00', '2025-10-05 13:10:00+00', 'phone', 'enc_val_001', 'TWM123100',
 CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),

('HOProcessed', '2025-11-01', 'Monthly_TWM_2025-11.csv', 4, 'hash_TWM_004',
 'txn_004', '2025-11-02 15:30:00+00', 'order_004', 'u_twm_1',
 'STM', 'TWM', 'rw_004', 'ext_rw_004', 'coupon', 'Free Data Booster', 1,
 0, 0, 'SGD', 5.00, 0.50, 0.00, 32.81, 1.28, 1.27, 32.81,
 'payment', 'free', 0, 0, 'SGD', NULL, NULL, NULL, NULL,
 '2025-11-02 16:30:00+00', '2025-11-02 17:30:00+00', 'phone', 'enc_val_001', 'TWM123100',
 CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),

-- User: twm_uid_002 (Tier2)
('HOProcessed', '2025-09-01', 'Monthly_TWM_2025-09.csv', 5, 'hash_TWM_005',
 'txn_005', '2025-09-15 10:40:00+00', 'order_005', 'u_twm_2',
 'GLB', 'TWM', 'rw_005', 'ext_rw_005', 'coupon', 'Movie Voucher', 1,
 200, 0, 'SGD', 5.00, 0.50, 5.00, 32.81, 1.28, 1.27, 32.81,
 'payment', 'point', 200, 0, 'SGD', NULL, NULL, NULL, NULL,
 '2025-09-15 11:40:00+00', '2025-09-15 12:40:00+00', 'phone', 'enc_val_002', 'TWM123101',
 CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),

('HOProcessed', '2025-10-01', 'Monthly_TWM_2025-10.csv', 6, 'hash_TWM_006',
 'txn_006', '2025-10-12 11:25:00+00', 'order_006', 'u_twm_2',
 'GLB', 'TWM', 'rw_006', 'ext_rw_006', 'coupon', 'Theme Park Ticket', 2,
 0, 75.00, 'SGD', 5.00, 0.50, 15.00, 32.81, 1.28, 1.27, 32.81,
 'payment', 'credit_card', 0, 75.00, 'SGD', NULL, NULL, NULL, NULL,
 '2025-10-12 12:25:00+00', '2025-10-12 13:25:00+00', 'phone', 'enc_val_002', 'TWM123101',
 CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),

('HOProcessed', '2025-12-01', 'Monthly_TWM_2025-12.csv', 7, 'hash_TWM_007',
 'txn_007', '2025-12-05 17:00:00+00', 'order_007', 'u_twm_2',
 'GLB', 'TWM', 'rw_007', 'ext_rw_007', 'coupon', 'Holiday Gift Pack', 1,
 80, 1.00, 'SGD', 5.00, 0.50, 5.00, 32.81, 1.28, 1.27, 32.81,
 'payment', 'credit_card+point', 50, 1.00, 'SGD', NULL, NULL, NULL, NULL,
 '2025-12-05 18:00:00+00', '2025-12-05 19:00:00+00', 'phone', 'enc_val_002', 'TWM123101',
 CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),

-- User: twm_uid_003 (Tier1)
('HOProcessed', '2025-10-01', 'Monthly_TWM_2025-10.csv', 8, 'hash_TWM_008',
 'txn_008', '2025-10-22 10:45:00+00', 'order_008', 'u_twm_3',
 'STM', 'TWM', 'rw_008', 'ext_rw_008', 'coupon', 'Music Streaming Pass', 1,
 40, 0, 'SGD', 5.00, 0.50, 10.00, 32.81, 1.28, 1.27, 32.81,
 'payment', 'point', 40, 0, 'SGD', NULL, NULL, NULL, NULL,
 '2025-10-22 11:45:00+00', '2025-10-22 12:45:00+00', 'phone', 'enc_val_003', 'TWM123102',
 CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),

('HOProcessed', '2025-11-01', 'Monthly_TWM_2025-11.csv', 9, 'hash_TWM_009',
 'txn_009', '2025-11-18 17:40:00+00', 'order_009', 'u_twm_3',
 'STM', 'TWM', 'rw_009', 'ext_rw_009', 'coupon', 'Online Course Credit', 1,
 0, 49.90, 'SGD', 5.00, 0.50, 5.00, 32.81, 1.28, 1.27, 32.81,
 'payment', 'credit_card', 0, 49.90, 'SGD', NULL, NULL, NULL, NULL,
 '2025-11-18 18:40:00+00', '2025-11-18 19:40:00+00', 'phone', 'enc_val_003', 'TWM123102',
 CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1),

('HOProcessed', '2025-12-01', 'Monthly_TWM_2025-12.csv', 10, 'hash_TWM_010',
 'txn_010', '2025-12-24 15:05:00+00', 'order_010', 'u_twm_3',
 'STM', 'TWM', 'rw_010', 'ext_rw_010', 'coupon', 'New Year Lucky Draw Entry', 1,
 0, 0, 'SGD', 5.00, 0.50, 0.00, 32.81, 1.28, 1.27, 32.81,
 'payment', 'free', 0, 0, 'SGD', NULL, NULL, NULL, NULL,
 '2025-12-24 16:05:00+00', '2025-12-24 17:05:00+00', 'phone', 'enc_val_003', 'TWM123102',
 CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1);

-- ============================================
-- Sample user profiles (aligned with transaction data)
-- ============================================
INSERT INTO user_profile (
    twm_uid, aes_twm_uid, subid, msisdn,
    point_type, point_uid, identity_type, tier, total_point,
    create_date, update_date
) VALUES
('twm_uid_001', 'enc_val_001', 'subid_001', '886912345000', 'default', 'point_uid_001', 'phone', 'Tier3', 50000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('twm_uid_002', 'enc_val_002', 'subid_002', '886912345001', 'default', 'point_uid_002', 'phone', 'Tier2', 25000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('twm_uid_003', 'enc_val_003', 'subid_003', '886912345002', 'default', 'point_uid_003', 'phone', 'Tier1', 5000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Login History (HANYA UNTUK AKUN YANG MASIH ADA: 1 dan 65)
INSERT INTO login_history (login_history_id, login_date, account_id)
VALUES
(581, TO_TIMESTAMP('14-MAR-24 03:33:45 PM', 'DD-MON-YY HH12:MI:SS AM'), 1),
(3761, TO_TIMESTAMP('04-JUL-25 11:34:40 AM', 'DD-MON-YY HH12:MI:SS AM'), 1),
(3941, TO_TIMESTAMP('18-AUG-25 02:14:51 PM', 'DD-MON-YY HH12:MI:SS AM'), 1),
(4061, TO_TIMESTAMP('25-AUG-25 08:48:33 AM', 'DD-MON-YY HH12:MI:SS AM'), 65);

COMMIT;