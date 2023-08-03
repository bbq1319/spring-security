CREATE TABLE USER
(
    id       integer      NOT NULL,
    username VARCHAR(20)  NOT NULL,
    password VARCHAR(255) NOT NULL,
    name     VARCHAR(20)  NOT NULL,
    primary key (id)
);