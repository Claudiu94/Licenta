DROP TABLE Users;

CREATE TABLE Users	 (
    PersonID int PRIMARY KEY,
    LastName varchar(255),
    FirstName varchar(255),
    Email varchar(255),
    Alarm varchar(255)
);

INSERT INTO Users (PersonID, LastName, FirstName, Email, Alarm)
VALUES (1, 'Ion', 'Pop', 'email1@yahoo.com', 'Alarm_on...');

INSERT INTO Users (PersonID, LastName, FirstName, Email, Alarm)
VALUES (2, 'Florin', 'Pop', 'email2@yahoo.com', 'Alarm_on...'); 

INSERT INTO Users (PersonID, LastName, FirstName, Email, Alarm)
VALUES (3, 'Claudiu', 'Pop', 'email3@yahoo.com', 'Alarm_on...'); 