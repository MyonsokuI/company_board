-- ==========================================
-- 1. データベースと専用ユーザーの作成
-- ==========================================
-- ※データベースが既に存在する場合はエラーになるため、
--   手動で作成済みの場合は以下のCREATE DATABASE行をコメントアウトしてください。
CREATE DATABASE company_board_db;

-- 作成したデータベースに接続（psqlなどの場合）
\c company_board_db;

-- 専用ユーザーの作成とパスワード設定
CREATE USER board_user WITH PASSWORD 'password';

-- データベースに対するすべての権限を付与
GRANT ALL PRIVILEGES ON DATABASE company_board_db TO board_user;


-- ==========================================
-- 2. テーブル作成（外部キー制約を考慮した順序）
-- ==========================================

-- 0. 優先度マスタ (priorities)
CREATE TABLE priorities (
    priority_id serial PRIMARY KEY,
    priority varchar(5) NOT NULL UNIQUE
);

-- 1. 社員マスタ (employees)
CREATE TABLE employees (
    employee_id serial PRIMARY KEY,
    employee_no varchar(8) NOT NULL,
    name varchar(100) NOT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. 認証マスタ (users)
CREATE TABLE users (
    user_id serial PRIMARY KEY,
    employee_id int NOT NULL,
    login_id varchar(50) NOT NULL UNIQUE,
    password_hash varchar(255) NOT NULL,
    role varchar(20) NOT NULL, -- 'ADMIN' または 'USER'
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_employee FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- 3. 連絡メモマスタ (notices)
-- ※ React側の入力制限 (title: 最大10文字, body: 最大200文字) に対応するようサイズを調整・維持
CREATE TABLE notices (
    notice_id serial PRIMARY KEY,
    title varchar(50) NOT NULL,
    body text NOT NULL,
    priority int NOT NULL, -- 1:高, 2:中, 3:低 など
    created_by int NOT NULL,
    published_until timestamp, -- React側では 'YYYY-MM-DD' の日付形式でやり取り
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notices_employee FOREIGN KEY (created_by) REFERENCES employees(employee_id)
);

-- テーブルおよびシーケンスの権限を board_user に付与
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO board_user;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO board_user;


-- ==========================================
-- 3. 初期データ・テストデータの登録
-- ==========================================

-- 優先度マスタの初期データ
INSERT INTO priorities (priority_id, priority) VALUES (1, '高');
INSERT INTO priorities (priority_id, priority) VALUES (2, '中');
INSERT INTO priorities (priority_id, priority) VALUES (3, '低');

-- 社員データのサンプル（投稿者：山田太郎）
INSERT INTO employees (employee_id, employee_no, name) VALUES (101, 'EMP0001', '山田太郎');

-- 認証データのサンプル
INSERT INTO users (user_id, employee_id, login_id, password_hash, role) 
VALUES (101, 101, 'yamada', '$2a$10$dummyHashValueForTest........', 'USER');

-- 連絡メモ（お知らせ）のテストデータ（これまでお使いだったデータを網羅）
INSERT INTO notices (notice_id, title, body, priority, published_by, published_until, created_at, created_by) VALUES
(1, '【重要】システム】', '今週末の土曜日 22:00 ～ 日曜日 06:00 の間、サーバーメンテナンスを実施いたします。作業時間中はシステムをご利用いただけません。ご迷惑をおかけしますが、ご理解とご協力をお願いいたします。', 1, 101, '2026-08-30 00:00:00', '2026-08-18 15:24:48.243266', 101),
(2, '社内懇親会', '来週金曜日の18時より、3階会議室にて8月の社内懇親会を開催します。軽食と飲み物を用意しておりますので、奮ってご参加ください。参加の方は水曜日までにSlackにてご連絡ください。', 2, 101, '2026-08-25 00:00:00', '2026-08-18 15:24:48.251858', 101),
(3, 'ランチ情報', '総務部より、新しくオープンしたオフィスビル近くの定食屋さんの情報をまとめました。休憩時間のランチの参考に共有いたします。おすすめメニューは日替わりランチです！', 3, 101, NULL, '2026-08-18 15:24:50.020174', 101),
(4, 'メンテナンス', '今週末の土曜日に定期メンテナンスを実施します。', 1, 101, '2026-08-30 00:00:00', '2026-08-18 14:55:58.760230', 101),
(5, '社内研修', '来月開催される新人研修についての詳細を共有します。', 2, 101, NULL, '2026-08-18 13:55:58.760230', 101),
(17, 'システム保守', '今週末にメンテナンスを行います。', 1, 101, '2026-08-31 00:00:00', '2026-08-19 13:37:36.445219', 101),
(18, 'あいうえお', 'あいうえお', 2, 101, '2026-08-20 00:00:00', '2026-08-19 15:45:27.396056', 101),
(19, 'あいうえお', 'あいうえお', 2, 101, '2026-08-20 00:00:00', '2026-08-19 15:45:39.909308', 101),
(20, 'あいうえお', 'あいうえお', 2, 101, '2026-08-20 00:00:00', '2026-08-19 15:54:25.119650', 101),
(21, 'あいうえお', 'あいうえお', 2, 101, '2026-08-20 00:00:00', '2026-08-19 15:57:10.229686', 101);

-- ==========================================
-- 4. シーケンスの現在値調整（手動ID指定インサート対策）
-- ==========================================
SELECT setval('priorities_priority_id_seq', (SELECT MAX(priority_id) FROM priorities));
SELECT setval('employees_employee_id_seq', (SELECT MAX(employee_id) FROM employees));
SELECT setval('users_user_id_seq', (SELECT MAX(user_id) FROM users));
SELECT setval('notices_notice_id_seq', (SELECT MAX(notice_id) FROM notices));