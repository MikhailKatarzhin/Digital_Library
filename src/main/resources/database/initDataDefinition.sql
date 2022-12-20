CREATE TABLE IF NOT EXISTS "User_status"
(
    id bigserial NOT NULL,
    name character varying(15) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "PK_User_status_Id" PRIMARY KEY (id),
    CONSTRAINT "UQ_User_status_Name" UNIQUE (name)
    );

CREATE TABLE IF NOT EXISTS "Role_of_User"
(
    id bigserial NOT NULL,
    name character varying(15) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "PK_User_role_Id" PRIMARY KEY (id),
    CONSTRAINT "UQ_User_role_Name" UNIQUE (name)
    );

CREATE TABLE IF NOT EXISTS "User"
(
    id bigserial NOT NULL,
    username character varying(25) COLLATE pg_catalog."default" NOT NULL,
    email character varying(100) COLLATE pg_catalog."default" NOT NULL,
    status_id bigint NOT NULL DEFAULT 1,
    password character varying(60) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "PK_User_Id" PRIMARY KEY (id),
    CONSTRAINT "UQ_User_Email" UNIQUE (email),
    CONSTRAINT "UQ_User_Username" UNIQUE (username),
    CONSTRAINT "FK_User_has_User_status" FOREIGN KEY (status_id)
    REFERENCES "User_status" (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE NO ACTION,
    CONSTRAINT "CK_User_Email" CHECK (email::text ~ '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$'::text),
    CONSTRAINT "CK_User_Username" CHECK (username::text ~ '[A-Za-z0-9 А-Яа-яЁё]{3,25}'::text),
    CONSTRAINT "CK_User_Password_strength" CHECK (password::text ~ '^[A-Za-z0-9#$&\/%-\._]{8,60}$'::text)
    );

CREATE TABLE IF NOT EXISTS "Wallet"
(
    id bigserial NOT NULL,
    user_id bigint NOT NULL,
    balance bigint NOT NULL DEFAULT 0,
    CONSTRAINT "PK_Wallet_Id" PRIMARY KEY (id),
    CONSTRAINT "FK_Wallet_belongs_User" FOREIGN KEY (user_id)
    REFERENCES "User" (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    CONSTRAINT "CH_Wallet_Balance_not_negative" CHECK (balance >= 0)
);

CREATE TABLE IF NOT EXISTS "User_Role"
(
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    CONSTRAINT "PK_User_Role" PRIMARY KEY (user_id, role_id),
    CONSTRAINT "FK_User_has_User_role" FOREIGN KEY (user_id)
    REFERENCES "User" (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE NO ACTION,
    CONSTRAINT "FK_User_role_Occupies_Role" FOREIGN KEY (role_id)
    REFERENCES "Role_of_User" (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE NO ACTION
    );

CREATE TABLE IF NOT EXISTS "Universe"
(
    id bigserial NOT NULL,
    name character varying(100) COLLATE pg_catalog.default,
    creator_id bigint NOT NULL,
    description character varying(1024) NOT NULL default '',
    CONSTRAINT PK_Universe_Id PRIMARY KEY (id),
    CONSTRAINT FK_Universe_created_by_User_Id FOREIGN KEY (creator_id)
    REFERENCES "User" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT CK_Universe_name CHECK (name::text ~ '^[]{A-Za-z0-9 А-Яа-яЁё[punct]}$'::text),
    CONSTRAINT "CK_Book_Description" CHECK (description::text ~ '^[A-Za-z0-9 А-Яа-яЁё[:punct:]]{0,1024}$'::text)
    );

CREATE TABLE IF NOT EXISTS "Book_cycle"
(
    id bigserial NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    creator_id bigint NOT NULL,
    universe_id bigint NOT NULL,
    description character varying(1024) NOT NULL default '',
    CONSTRAINT "PK_Book_cycle_Id" PRIMARY KEY (id),
    CONSTRAINT "FK_Book_cycle_Created_by_User_Id" FOREIGN KEY (creator_id)
    REFERENCES "User" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "FK_Book_cycle_Refers_to_Universe_Id" FOREIGN KEY (universe_id)
    REFERENCES "Universe" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "CH_Book_cycle_Name" CHECK (name::text ~ '^[]{A-Za-z0-9 А-Яа-яЁё[:punct:]}$'::text),
    CONSTRAINT "CK_Book_Description" CHECK (description::text ~ '^[A-Za-z0-9 А-Яа-яЁё[:punct:]]{0,1024}$'::text)
    );

CREATE TABLE IF NOT EXISTS "Book_status"
(
    id bigserial NOT NULL,
    name character varying(15) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "PK_Book_status_Id" PRIMARY KEY (id),
    CONSTRAINT "UQ_Book_status_Name" UNIQUE (name),
    CONSTRAINT "CH_Book_status_Name" CHECK (name::text ~ '^[A-Za-z0-9 А-Яа-яЁё[:punct:]]{3,15}$'::text)
    );

CREATE TABLE IF NOT EXISTS "Book"
(
    id bigserial NOT NULL,
    book_cycle_id bigint,
    creator_id bigint NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    book_status_id bigint NOT NULL DEFAULT 0,
    cost bigint NOT NULL default 0,
    year_of_creation smallint,
    year_of_upload smallint,
    description character varying(1024) NOT NULL default '',
    CONSTRAINT "PK_Book_Id" PRIMARY KEY (id),
    CONSTRAINT "FK_Book_Created_by_User_Id" FOREIGN KEY (creator_id)
    REFERENCES "User" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "FK_Book_Refers_to_Book_cycle_Id" FOREIGN KEY (book_cycle_id)
    REFERENCES "Book_cycle" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "FK_Book_has_Book_status_Id" FOREIGN KEY (book_status_id)
    REFERENCES "Book_status" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "CH_Book_Year_of_creation" CHECK ("Book".year_of_creation >= -10000 AND "Book".year_of_creation <= 2100),
    CONSTRAINT "CH_Book_Year_of_upload" CHECK ("Book".year_of_upload >= 2022 AND "Book".year_of_upload <= 2100),
    CONSTRAINT "CH_Cost" CHECK ("Book".cost >= 0 AND "Book".cost < 10000),
    CONSTRAINT "CK_Book_Name" CHECK (name::text ~ '^[A-Za-z0-9 А-Яа-яЁё[:punct:]]{1,100}$'::text),
    CONSTRAINT "CK_Book_Description" CHECK (description::text ~ '^[A-Za-z0-9 А-Яа-яЁё[:punct:]]{0,1024}$'::text)
    );

CREATE TABLE IF NOT EXISTS "Chapter"
(
    id bigserial NOT NULL,
    book_id bigint NOT NULL,
    creator_id bigint NOT NULL,
    name character varying(128) NOT NULL,
    number bigint NOT NULL,
    CONSTRAINT "PK_Chapter_Id" PRIMARY KEY (id),
    CONSTRAINT "FK_Chapter_Created_by_User_Id" FOREIGN KEY (creator_id)
    REFERENCES "User" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "FK_Chapter_Refers_to_Book_Id" FOREIGN KEY (book_id)
    REFERENCES "Book" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "CK_Chapter_Name" CHECK (name::text ~ '^[A-Za-z0-9 А-Яа-яЁё[:punct:]]{1,128}$'::text)
    );

CREATE TABLE IF NOT EXISTS "Section"
(
    id bigserial NOT NULL,
    chapter_id bigint NOT NULL,
    creator_id bigint NOT NULL,
    number bigint NOT NULL,
    content character varying(8192) NOT NULL,
    CONSTRAINT "PK_Section_Id" PRIMARY KEY (id),
    CONSTRAINT "FK_Section_Created_by_User_Id" FOREIGN KEY (creator_id)
    REFERENCES "User" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "FK_Section_Occupies_Chapter" FOREIGN KEY (chapter_id)
    REFERENCES "Chapter" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    );

CREATE TABLE IF NOT EXISTS "Tag"
(
    id bigserial NOT NULL,
    name character varying(64) NOT NULL,
    CONSTRAINT "PK_Tag_Id" PRIMARY KEY (id),
    CONSTRAINT "UQ_Tag_Name" UNIQUE (name),
    CONSTRAINT "CK_Tag_Name" CHECK (name::text ~ '^[A-Za-zА-Яа-яЁё]{2,64}$'::text)
    );

CREATE TABLE IF NOT EXISTS "Book_Tag"
(
    tag_id bigint NOT NULL,
    book_id bigint NOT NULL,
    CONSTRAINT "PK_Book_Tag" PRIMARY KEY (book_id, tag_id),
    CONSTRAINT "FK_Book_have_Tag" FOREIGN KEY (tag_id)
    REFERENCES "Tag" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "FK_Tag_Occupies_Book" FOREIGN KEY (book_id)
    REFERENCES "Book" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    );

CREATE TABLE IF NOT EXISTS "User_Book"
(
    user_id bigint NOT NULL,
    book_id bigint NOT NULL,
    CONSTRAINT "PK_User_Book" PRIMARY KEY (book_id, user_id),
    CONSTRAINT "FK_Book_belongs_to_User" FOREIGN KEY (user_id)
        REFERENCES "User" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT "FK_User_has_Book" FOREIGN KEY (book_id)
        REFERENCES "Book" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);