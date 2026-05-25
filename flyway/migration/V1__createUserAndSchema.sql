DO $$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'app_user') THEN
    CREATE USER app_user WITH PASSWORD 'pass456';
  END IF;
END
$$;

CREATE SCHEMA IF NOT EXISTS app;

--　スキーマを使用できる権限を付与
GRANT USAGE ON SCHEMA app TO app_user;

-- 今後作られるテーブル、シーケンスに自動で権限付与
ALTER DEFAULT PRIVILEGES IN SCHEMA app GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO app_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA app GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO app_user;

-- PostgreSQLでデータを暗号化・ハッシュ化するための標準的な拡張機能(今回は必要ない)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";