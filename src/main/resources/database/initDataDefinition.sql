CREATE TABLE IF NOT EXISTS public."User_status"
(
    id bigserial NOT NULL,
    name character varying(15) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "PK_User_status_Id" PRIMARY KEY (id),
    CONSTRAINT "UQ_User_status_Name" UNIQUE (name)
    );

CREATE TABLE IF NOT EXISTS public."Role_of_User"
(
    id bigserial NOT NULL,
    name character varying(15) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "PK_User_role_Id" PRIMARY KEY (id),
    CONSTRAINT "UQ_User_role_Name" UNIQUE (name)
    );

CREATE TABLE IF NOT EXISTS public."User"
(
    id bigserial NOT NULL,
    nickname character varying(25) COLLATE pg_catalog."default" NOT NULL,
    email character varying(50) COLLATE pg_catalog."default" NOT NULL,
    status_id bigint NOT NULL DEFAULT 1,
    password character varying(30) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "PK_User_Id" PRIMARY KEY (id),
    CONSTRAINT "UQ_User_Email" UNIQUE (email),
    CONSTRAINT "UQ_User_Nickname" UNIQUE (nickname),
    CONSTRAINT "FK_User_has_User_status" FOREIGN KEY (status_id)
    REFERENCES public."User_status" (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE NO ACTION,
    CONSTRAINT "CK_User_Email" CHECK (email::text ~ '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$'::text),
    CONSTRAINT "CK_User_Nickname" CHECK (nickname::text ~ '[A-Za-z0-9]{3,25}'::text),
    CONSTRAINT "CK_User_Password_strength" CHECK (password::text ~ '^[A-Za-z0-9#&%-\._]{8,30}$'::text)
    );

CREATE TABLE public."Wallet"
(
    id bigint NOT NULL,
    balance bigint NOT NULL DEFAULT 0,
    CONSTRAINT "PK_Wallet_Id" PRIMARY KEY (id),
    CONSTRAINT "FK_Wallet_belongs_User" FOREIGN KEY (id)
    REFERENCES public."User" (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    CONSTRAINT "CH_Wallet_Balance_not_negative" CHECK (balance >= 0)
);

CREATE TABLE IF NOT EXISTS public."User_Role"
(
    id bigserial NOT NULL,
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    CONSTRAINT "PK_User_Role_Id" PRIMARY KEY (id),
    CONSTRAINT "FK_User_has_User_role" FOREIGN KEY (user_id)
    REFERENCES public."User" (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE NO ACTION,
    CONSTRAINT "FK_User_role_Occupies_Role" FOREIGN KEY (role_id)
    REFERENCES public."Role_of_User" (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE NO ACTION
    );

CREATE TABLE IF NOT EXISTS public."Universe"
(
    id bigserial NOT NULL,
    name character varying(100) COLLATE pg_catalog.default,
    creator_id bigint NOT NULL,
    CONSTRAINT PK_Universe_Id PRIMARY KEY (id),
    CONSTRAINT FK_Universe_created_by_User_Id FOREIGN KEY (creator_id)
    REFERENCES "User" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT CK_Universe_name CHECK (name::text ~ '^[]{A-Za-z0-9 А-Яа-я[punct]}$'::text)
    );

CREATE TABLE IF NOT EXISTS public."Book_cycle"
(
    id bigserial NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    creator_id bigint NOT NULL,
    universe_id bigint NOT NULL,
    CONSTRAINT "PK_Book_cycle_Id" PRIMARY KEY (id),
    CONSTRAINT "FK_Book_cycle_Created_by_User_Id" FOREIGN KEY (creator_id)
    REFERENCES "User" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "FK_Book_cycle_Refers_to_Universe_Id" FOREIGN KEY (universe_id)
    REFERENCES "Universe" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "CH_Book_cycle_Name" CHECK (name::text ~ '^[]{A-Za-z0-9 А-Яа-я[:punct:]}$'::text)
    );

CREATE TABLE IF NOT EXISTS public."Book_status"
(
    id bigserial NOT NULL,
    name character varying(15) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "PK_Book_status_Id" PRIMARY KEY (id),
    CONSTRAINT "UQ_Book_status_Name" UNIQUE (name),
    CONSTRAINT "CH_Book_status_Name" CHECK (name::text ~ '^[A-Za-z0-9 А-Яа-я[:punct:]]{3,15}$'::text)
    );

CREATE TABLE IF NOT EXISTS public."Book"
(
    id bigserial NOT NULL,
    book_cycle_id bigint NOT NULL,
    creator_id bigint NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    book_status_id bigint NOT NULL DEFAULT 0,
    CONSTRAINT "PK_Book_Id" PRIMARY KEY (id),
    CONSTRAINT "FK_Book_Created_by_User_Id" FOREIGN KEY (creator_id)
    REFERENCES public."User" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "FK_Book_Refers_to_Book_cycle_Id" FOREIGN KEY (book_cycle_id)
    REFERENCES public."Book_cycle" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "FK_Book_has_Book_status_Id" FOREIGN KEY (book_status_id)
    REFERENCES public."Book_status" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "CK_Book_Name" CHECK (name::text ~ '^[A-Za-z0-9 А-Яа-я[:punct:]]{1,100}$'::text)
    );

CREATE TABLE IF NOT EXISTS public."Chapter"
(
    id bigserial NOT NULL,
    book_id bigint NOT NULL,
    creator_id bigint NOT NULL,
    name character varying(128) NOT NULL,
    number bigint NOT NULL,
    CONSTRAINT "PK_Chapter_Id" PRIMARY KEY (id),
    CONSTRAINT "FK_Chapter_Created_by_User_Id" FOREIGN KEY (creator_id)
    REFERENCES public."User" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "FK_Chapter_Refers_to_Book_Id" FOREIGN KEY (book_id)
    REFERENCES public."Book" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "CK_Chapter_Name" CHECK (name::text ~ '^[A-Za-z0-9 А-Яа-я[:punct:]]{1,128}$'::text)
    );

CREATE TABLE IF NOT EXISTS public."Section"
(
    id bigserial NOT NULL,
    chapter_id bigint NOT NULL,
    creator_id bigint NOT NULL,
    number bigint NOT NULL,
    content character varying(4096) NOT NULL,
    CONSTRAINT "PK_Section_Id" PRIMARY KEY (id),
    CONSTRAINT "FK_Section_Created_by_User_Id" FOREIGN KEY (creator_id)
    REFERENCES public."User" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "FK_Section_Occupies_Chapter" FOREIGN KEY (chapter_id)
    REFERENCES public."Chapter" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    );

CREATE TABLE IF NOT EXISTS public."Tag"
(
    id bigserial NOT NULL,
    name character varying(64) NOT NULL,
    CONSTRAINT "PK_Tag_Id" PRIMARY KEY (id),
    CONSTRAINT "CK_Tag_Name" CHECK (name::text ~ '^[A-Za-zА-Яа-я]{1,64}$'::text)
    );

CREATE TABLE IF NOT EXISTS public."Book_Tag"
(
    id bigserial NOT NULL,
    tag_id bigint NOT NULL,
    book_id bigint NOT NULL,
    CONSTRAINT "PK_Book_Tag_Id" PRIMARY KEY (id),
    CONSTRAINT "FK_Book_have_Tag" FOREIGN KEY (tag_id)
    REFERENCES public."Tag" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT "FK_Tag_Occupies_Book" FOREIGN KEY (book_id)
    REFERENCES public."Book" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    );