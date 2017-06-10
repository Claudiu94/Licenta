-- DROP TABLE Users;

-- CREATE TABLE Users	 (
--     PersonID int PRIMARY KEY,
--     LastName varchar(255),
--     FirstName varchar(255),
--     Username varchar(255) not null UNIQUE,
--     Email varchar(255),
--     Password varchar(255)
-- );

-- INSERT INTO Users (PersonID, LastName, FirstName, Email, Username, Password)
-- VALUES (1, 'Ion', 'Pop', 'email1@yahoo.com', 'user1', 'pass...');

-- INSERT INTO Users (PersonID, LastName, FirstName, Email, Username, Password)
-- VALUES (2, 'Florin', 'Pop', 'email2@yahoo.com', 'user2', 'pass...'); 

-- INSERT INTO Users (PersonID, LastName, FirstName, Email, Username, Password)
-- VALUES (3, 'Claudiu', 'Pop', 'email3@yahoo.com', 'user3', 'pass...');

CREATE TABLE Shares (
	PersonID int,
	Symbol varchar(255),
	Name varchar(255),
	Shares int,
	CONSTRAINT FK_PersonId FOREIGN KEY (PersonID)
    REFERENCES Users(PersonID)
);

INSERT INTO Shares (PersonID, Symbol, Name, Shares)
VALUES(4, "AAPL", "Apple Inc.", 40);

INSERT INTO Shares (PersonID, Symbol, Name, Shares)
VALUES(4, "GOOG", "Alphabet Inc.", 20);

INSERT INTO Shares (PersonID, Symbol, Name, Shares)
VALUES(4, "AMGN", "Amgen Inc.", 60);