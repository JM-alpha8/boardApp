-- 1) 사용자 시드 (bcrypt 해시 예시: "password")
INSERT INTO users (email, password, name, role)
VALUES ('seed@local', '{bcrypt}$2a$10$z2wQ2Jm5y2J6k9dTnNwY5eCqQf0h1P3qf0Q1e5p0yS0q7y6e2oL/u', 'Seed User', 'USER');

-- 2) 글 시드: author_id를 위 유저로 지정
INSERT INTO article (title, content, author_id)
SELECT '나는야 첫글이야', '테스트 내용', u.id
FROM users u
WHERE u.email = 'seed@local';