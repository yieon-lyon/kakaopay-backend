INSERT INTO user (id, login, password, name, email, activated, created_at, updated_at) VALUES (1, 'counselor1', '$2a$11$Rp0.W6cHHZyXkHPHqZARw.pX11JrIf6UpY.EcsV3iEE59jvZC0Kma', '상담사1', 'counselor1@kakaopay.tech', true, '2022-07-23 03:53:26.980539', '2022-07-23 03:53:26.980539');
INSERT INTO user (id, login, password, name, email, activated, created_at, updated_at) VALUES (2, 'counselor2', '$2a$11$Rp0.W6cHHZyXkHPHqZARw.pX11JrIf6UpY.EcsV3iEE59jvZC0Kma', '상담사2', 'counselor2@kakaopay.tech', true, '2022-07-23 03:53:26.980539', '2022-07-23 03:53:26.980539');
INSERT INTO user (id, login, password, name, email, activated, created_at, updated_at) VALUES (3, 'counselor3', '$2a$11$Rp0.W6cHHZyXkHPHqZARw.pX11JrIf6UpY.EcsV3iEE59jvZC0Kma', '상담사3', 'counselor3@kakaopay.tech', true, '2022-07-23 03:53:26.980539', '2022-07-23 03:53:26.980539');
INSERT INTO user (id, login, password, name, email, activated, created_at, updated_at) VALUES (4, 'user', '$2a$11$wsIo9HuCOKvB76iedtFNje03xArh5xB38RupivVnO1cooy8rHOhv2', '고객', 'user@kakaopay.tech', true, '2022-07-23 03:53:26.980539', '2022-07-23 03:53:26.980539');

INSERT INTO authority (name) VALUES ('ROLE_ADMIN');
INSERT INTO authority (name) VALUES ('ROLE_USER');

INSERT INTO user_authority (user_id, authority_name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO user_authority (user_id, authority_name) VALUES (2, 'ROLE_ADMIN');
INSERT INTO user_authority (user_id, authority_name) VALUES (3, 'ROLE_ADMIN');
INSERT INTO user_authority (user_id, authority_name) VALUES (4, 'ROLE_USER');

-- INSERT INTO inquiry (id, user_id, title, created_by, assigner, status, created_at, updated_at) VALUES (1, 4, '전자결재 프로세스가 동작하지 않아요.', '이언철', 1, 'ING', '2022-07-24 03:53:26.980539', '2022-07-24 03:53:26.980539');
-- INSERT INTO inquiry_details (id, inquiry_id, content, created_by, created_at, updated_at) VALUES (1, 1, '전자결재 프로세스 과정의 승인 기능에서 오류가 발생하여 현재 확인중입니다.\n버그가 해결되면 추가 답변으로 안내해드리겠습니다.\n이용에 불편을 드려 죄송합니다.\n감사합니다.', '상담사1', '2022-07-24 03:53:26.980539', '2022-07-24 03:53:26.980539')