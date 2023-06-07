use chatbox;

insert into ta_msg_message(I_group_id, I_member_id, T_content)
values (9, 1, "tin nhan test thu 1");

select * from ta_acc_account;
select * from ta_mbr_member;
select * from ta_grp_group;
select * from ta_grm_groupmembers;
select * from ta_msg_message;

select * from ta_grp_group where I_member_id =1;

SELECT * FROM ta_grp_group WHERE I_member_id = 2 ORDER BY I_id DESC LIMIT 1;

select mb.I_id from (ta_acc_account as acc inner join ta_mbr_member as mb on acc.I_id = mb.I_account_id ) where acc.T_userName = "phanquan";

select mb.T_name, msg.T_content from (ta_msg_message as msg inner join ta_mbr_member as mb on msg.I_member_id = mb.I_id ) where msg.I_group_id = 3;

update ta_msg_message set T_content = "Đã thêm Quan dep trai vào nhóm!" where I_group_id = 3;
  
select mb.T_name from (ta_acc_account as acc inner join ta_mbr_member as mb on acc.I_id = mb.I_account_id )
	where acc.T_userName = "phanquan";

select mb.T_name, msg.T_content from (ta_msg_message as msg inner join ta_mbr_member as mb on msg.I_member_id = mb.I_id ) 
	where msg.I_group_id = 7 ORDER BY msg.I_id;

select grp.I_id, grp.T_name from (ta_grp_group as grp inner join ta_grm_groupmembers as grm on grp.I_id = grm.I_group_id)
 where grm.I_member_id = 3;




SELECT I_id FROM ta_grp_group WHERE T_name = 'group1' AND I_member_id = 1;
 
 
 
 
 
 
DELETE FROM ta_msg_message;
DELETE FROM ta_grm_groupmembers;
DELETE FROM ta_grp_group;
DELETE FROM ta_mbr_member;
DELETE FROM ta_acc_account;


TRUNCATE TABLE ta_grp_group;
select * from ta_acc_account where T_userName="phanquan";