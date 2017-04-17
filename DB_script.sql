DROP TABLE Users;

CREATE TABLE Users	 (
    PersonID int PRIMARY KEY,
    LastName varchar(255),
    FirstName varchar(255),
    Username varchar(255) not null UNIQUE,
    Email varchar(255),
    Password varchar(255)
);

INSERT INTO Users (PersonID, LastName, FirstName, Email, Username, Password)VALUES (1, 'Ion', 'Pop', 'email1@yahoo.com', 'user1', 'pass...');

INSERT INTO Users (PersonID, LastName, FirstName, Email, Username, Password)
VALUES (2, 'Florin', 'Pop', 'email2@yahoo.com', 'user2', 'pass...'); 

INSERT INTO Users (PersonID, LastName, FirstName, Email, Username, Password)
VALUES (3, 'Claudiu', 'Pop', 'email3@yahoo.com', 'user3', 'pass...'); 