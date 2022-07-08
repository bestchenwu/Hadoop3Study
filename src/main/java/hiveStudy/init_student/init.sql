drop table if exists default.student_tb_txt;
create table if not exists default.student_tb_txt(
  s_no string comment '学号',
  s_name string comment '姓名',
  s_birth string comment '生日',
  s_age bigint  comment '年龄',
  s_sex string comment '性别',
  s_score bigint comment '综合能力得分',
 s_desc string comment '自我介绍'
)
row format delimited
fields terminated by '\t'
location '/mnt/data/bigdata/hive/warehouse/student_tb_txt/';

--将文件上传到该目录
hdfs dfs -put /home/openmldb/logs/hive/init/student_tb_txt/*.txt  /mnt/data/bigdata/hive/warehouse/student_tb_txt/

drop table if exists default.student_sc_tb_txt;
create table if not exists default.student_sc_tb_txt(
  s_no string comment '学号',
  course string comment '课程名',
  op_datetime string comment '操作时间',
  reason  string comment '选课原因'
)
row format delimited
fields terminated by '\t'
location '/mnt/data/bigdata/hive/warehouse/student_sc_tb_txt';

hdfs dfs -put /home/openmldb/logs/hive/init/student_sc_tb_txt/*.txt /mnt/data/bigdata/hive/warehouse/student_sc_tb_txt/

--查询student_tb_txt表  获取每个年龄段最晚出生和最早出生的人的生日，并存入新表:student_stat
drop table if exists student_stat;
create table student_stat(a bigint, b bigint) partitioned by (tp string)
stored as textfile;

--使用如下方式
set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict;
insert into table student_stat partition(tp)
select s_age,max(s_birth) stat,'max' tp
from student_tb_txt
group by s_age
union all
select s_age,min(s_birth) stat, 'min' tp
from student_tb_txt
group by s_age;
