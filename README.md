# server-chat
This is the project server
To work, you will need:
database (I used MySQL)
Scripts:
CREATE a SCHEMA `schema_for_server` ;

Table for storing messages:
CREATE A TABLE 'schema_for_server'.`message' (
'idmessage` INT NOT NULL AUTO_INCREMENT,
'user' VARCHAR(256) NOT NULL,
`text` VARCHAR(256) NOT NULL,
PRIMARY KEY ('idmessage'))

User Data table
CREATE A TABLE `schema_for_server'.`users' (
`name` VARCHAR(265) NOT NULL,
`password` VARCHAR(256) NOT NULL,
PRIMARY KEY (`name'))
