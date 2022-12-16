INSERT INTO
    "User_status" (id, name)
VALUES
       (1, 'ACTIVE'), (2, 'BANNED'), (3, 'DELETED')
ON CONFLICT (id)
    DO UPDATE SET name = Excluded.name;

INSERT INTO
    "Role_of_User" (id, name)
    VALUES
       (1, 'ADMINISTRATOR'), (2, 'READER'), (3, 'AUTHOR')
ON CONFLICT (id)
    DO UPDATE SET id = Excluded.id;

INSERT INTO
    "User" (id, username, email, status_id, password)
     VALUES
        (0, 'administrator', 'maikl.1997@mail.ru', 1, '$2a$10$m/GwtXT2pD5VeGgpdGkNLeZVg9C0NG/sRhiPkkvKXrZj6wXtiUJze')
ON CONFLICT (id)
    DO NOTHING;

INSERT INTO
    "User_Role" (user_id, role_id)
VALUES
    (0, 1), (0, 2), (0, 3)
ON CONFLICT (user_id, role_id)
    DO NOTHING;

INSERT INTO
    "Book_status" (id, name)
VALUES
    (0, 'Announce'), (1, 'Written'), (2, 'Finished'), (3, 'Hidden')
ON CONFLICT (id)
    DO NOTHING;

INSERT INTO
    "Wallet" (id, balance)
VALUES
    (0, 100000)
ON CONFLICT (id)
    DO NOTHING;