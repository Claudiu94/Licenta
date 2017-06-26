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

CREATE TABLE Stocks (
	Symbol varchar(255),
	CompanyName varchar(255),
	Currency varchar(255),
	Price varchar(255)
);

INSERT INTO Shares (PersonID, Symbol, Name, Shares)
VALUES(4, "AAPL", "Apple Inc.", 40);

INSERT INTO Shares (PersonID, Symbol, Name, Shares)
VALUES(4, "GOOG", "Alphabet Inc.", 20);

INSERT INTO Shares (PersonID, Symbol, Name, Shares)
VALUES(4, "AMGN", "Amgen Inc.", 60);


alter table Notifications add Email int;
alter table Notifications add App int;
update Notifications set Email=0;
update Notifications set App=0;
alter table Notifications add constraint fk_users foreign key (PersonId) references Users (PersonId);
alter table Portofolios add constraint fk_users_portofolios foreign key (PersonId) references Users (PersonId);
