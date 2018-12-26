alter table oa_student_graduate_paper_task add column guide_teacher_email varchar(50) comment '指导老师邮箱' after guide_teacher;
alter table oa_student_graduate_paper_task add column guide_teacher_phone varchar(50) comment '指导老师联系电话' after guide_teacher_email;
alter table oa_student_graduate_paper_task add column is_view char(1) DEFAULT '0' comment '是否查看 0-未查看 1-已查看';
alter table oa_student_graduate_paper_task add column view_time datetime comment '查看时间' after is_view;
alter table oa_student_graduate_paper_task add column school_department varchar(50) comment '学院（系）';