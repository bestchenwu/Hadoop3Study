#   coding:utf-8
import random
import datetime
import sys


def create_student_sc_dict(start):
    filename = str(start) + '.txt'
    with open('/home/openmldb/logs/hive/init/student_sc_tb_txt/' + filename, mode='w+') as fp:
        for i in range(start * 40000, (start + 1) * 40000):
            # random.sample表示从列表里随机取固定个数的元素组成一个新的列表
            course = random.sample([u'数学', u'数学', u'数学', u'数学', u'语文', u'英语', u'化学', u'物理', u'生物'], 1)[0]
            model = {
                "s_no": u"xuehao_no_" + str(i),
                "course": u"{0}".format(course),
                "op_datetime": datetime.datetime.now().strftime("%Y-%m-%d"),
                "reason": u"我非常非常非常非常非常非常非常非常非常非常喜爱{0}".format(course)
            }
            fp.write(
                "{0}\t{1}\t{2}\t{3}\n".format(model['s_no'], model['course'], model['op_datetime'], model['reason']))


for i in range(1, 501):
    create_student_sc_dict(i)
