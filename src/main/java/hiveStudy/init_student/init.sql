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


--进行文件压缩
set hive.merge.mapfiles=true;
set hive.merge.orcfile.stripe.level=true;
set hive.merge.size.per.task=268435456;
set hive.merge.smallfiles.avgsize=16777216;
create table student_tb_orc like student_tb_txt stored as orc;
insert into student_tb_orc
select * from student_tb_txt;

create table student_tb_txt_bigfile like student_tb_txt stored as textfile;
insert into student_tb_txt_bigfile
select * from student_tb_orc;

--重新试验插入速度
insert into table student_stat partition(tp)
select s_age,max(s_birth) stat,'max' tp
from student_tb_txt_bigfile
group by s_age;

insert into table student_stat partition(tp)
select s_age,min(s_birth) stat,'min' tp
from student_tb_txt_bigfile
group by s_age;


--建立分区分桶的表
drop table if exists  student_orc_bucket;
create table if not exists student_orc_bucket(
s_no                    string,
s_name                  string,
s_birth                string,
s_age                  bigint,
s_sex                   string,
s_score                bigint,
s_desc                 string
)
--取s_birth的年份作为分区
partitioned by (birth_year string comment '生日的年份作为分区')
--分成16个桶
clustered BY (s_age) INTO 16 BUCKETS
STORED AS ORC;

set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict;
set hive.enforce.bucketing = true;
insert into table student_orc_bucket
partition(birth_year)
select s_no,s_name,s_birth,s_age,s_sex,s_score,s_desc,year(s_birth) as birth_year
from student_tb_orc;

