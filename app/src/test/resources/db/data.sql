INSERT INTO app.avatar(id, created_at, updated_at, active, original_filename, content_type, content_size, changed_stem) VALUES
(RANDOM_UUID(), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'FALSE', 'sample1.png', 'image/png', 1L, 'abcdefg'),
(RANDOM_UUID(), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'FALSE', 'sample1.png', 'image/png', 1L, 'hijklmn'),
(RANDOM_UUID(), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'FALSE', 'sample1.png', 'image/png', 1L, 'opqrstu');

INSERT INTO app.profile(id, created_at, updated_at, active, personal_number, last_name, first_name, last_name_kana, first_name_kana, gender, date_of_birth, birthplace, memo, avatar_id) VALUES
(RANDOM_UUID(), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'FALSE', '001', '鈴木', '治', 'スズキ', 'オサム', 'MALE', DATE '1980-04-10', 'TOKYO', '趣味は読書です。', null),
(RANDOM_UUID(), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'TRUE', '002', '柴田', '加奈子', 'シバタ', 'カナコ', 'FEMALE', DATE '1983-02-09', 'KANAGAWA', '趣味は映画鑑賞です。', null),
(RANDOM_UUID(), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'FALSE', '003', '山本', '鉄二', 'ヤマモト', 'テツジ', 'MALE', DATE '1974-05-09', 'OKINAWA', '趣味は映画鑑賞です。', null),
(RANDOM_UUID(), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'FALSE', '004', '御手洗', '治', 'ミタライ', 'オサム', 'MALE', DATE '1990-05-10', 'KANAGAWA', '趣味は映画鑑賞です。', null),
(RANDOM_UUID(), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'TRUE', '005', '柴田', '恭平', 'シバタ', 'キョウヘイ', 'MALE', DATE '2000-02-07', 'CHIBA', '趣味は映画鑑賞です。', null);

INSERT INTO app.sequence(name, current_value, created_at, updated_at) VALUES
('PERSONAL_NUMBER', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);