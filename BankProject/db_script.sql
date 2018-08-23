DROP DATABASE IF EXISTS BankDBTest;
CREATE DATABASE BankDBTest;

CREATE TABLE IF NOT EXISTS `BankDBTest`.`Users` (
 `Id` INT NOT NULL AUTO_INCREMENT,
 `Passport` VARCHAR(30) UNIQUE NOT NULL,
 `Password` VARCHAR(30) NOT NULL,
 `First_name` VARCHAR(40),
 `Last_name` VARCHAR(40),
 `Phone_number` VARCHAR(20),
 PRIMARY KEY (`Id`));

CREATE TABLE IF NOT EXISTS `BankDBTest`.`Accounts` (
 `Id` INT NOT NULL AUTO_INCREMENT,
 `Balance` DECIMAL(20,5) NOT NULL CHECK (Balance>0),
 PRIMARY KEY (`Id`));

CREATE TABLE IF NOT EXISTS `BankDBTest`.`User_Accounts` (
 `User_id` INT NOT NULL,
 `Account_Id` INT NOT NULL,
 FOREIGN KEY (`User_id`) REFERENCES Users(`Id`) ON DELETE CASCADE,
 FOREIGN KEY (`Account_Id`) REFERENCES Accounts(`Id`) ON DELETE CASCADE);


CREATE TABLE IF NOT EXISTS `BankDBTest`.`Operations` (
 `Id` INT NOT NULL AUTO_INCREMENT,
 `Sender_User_Id` INT NOT NULL,
 `Sender_Bank_Id` INT NOT NULL,
 `Receiver_User_Id` INT NOT NULL,
 `Receiver_Bank_Id` INT NOT NULL,
 `Amount` DECIMAL(20,5) NOT NULL CHECK (Amount>0),
 `Operation_date` TIMESTAMP,
 PRIMARY KEY (`Id`));

USE BankDBTest;
 INSERT INTO Users(passport,password,first_name,last_name,phone_number) VALUES ('1234123456','1234','JOHN','DOE','4321'),('5678567890','5678','ALICE','KAY','8765');
 INSERT INTO Accounts(balance) VALUES ('500.0'),('700.0');
 INSERT INTO User_Accounts(user_id, account_id) VALUES ('500.0'),('700.0');