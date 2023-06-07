
use chatbox;

CREATE TABLE `TA_ACC_Account` (
	`I_id` int primary key auto_increment,
	`T_userName` char(30) unique,
	`T_password` char(30)
);
CREATE TABLE `TA_MBR_Member` (
	`I_id` int primary key auto_increment,
	`T_name` varchar(50) not null,
	`I_account_id` int unique
);
CREATE TABLE `TA_GRP_Group` (
	`I_id` int primary key auto_increment,
	`T_name` varchar(50) not null,
    `I_member_id` int
);

CREATE TABLE TA_GRP_Files (
    I_id INT PRIMARY KEY AUTO_INCREMENT,
    T_file_name VARCHAR(255),
    B_file_data LONGBLOB,
    I_group_id INT
);
alter table TA_GRP_Files add column I_member_id int;
ALTER TABLE TA_GRP_Files ADD FOREIGN KEY (I_group_id) REFERENCES TA_GRP_Group (I_id);
ALTER TABLE TA_GRP_Files ADD FOREIGN KEY (I_member_id) REFERENCES TA_MBR_Member (I_id);

ALTER TABLE TA_GRP_Group ADD FOREIGN KEY (I_member_id) REFERENCES TA_MBR_Member (I_id);

CREATE TABLE `TA_MSG_Message` (
	`I_id` int primary key auto_increment,
	`I_group_id` int,
	`I_member_id` int,
	`T_content` text
);

CREATE TABLE `TA_GRM_GroupMembers` (

	`I_id` int primary key auto_increment,
	`I_group_id` int,
	`I_member_id` int
);

ALTER TABLE TA_GRM_GroupMembers ADD FOREIGN KEY (I_group_id) REFERENCES TA_GRP_Group (I_id);
ALTER TABLE TA_GRM_GroupMembers ADD FOREIGN KEY (I_member_id) REFERENCES TA_MBR_Member (I_id);

ALTER TABLE TA_MSG_Message ADD FOREIGN KEY (I_member_id) REFERENCES TA_MBR_Member (I_id);
ALTER TABLE TA_MSG_Message ADD FOREIGN KEY (I_group_id) REFERENCES TA_GRP_Group (I_id);
ALTER TABLE TA_MBR_Member  ADD FOREIGN KEY (I_account_id) REFERENCES TA_ACC_Account (I_id);