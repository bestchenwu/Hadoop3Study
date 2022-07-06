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