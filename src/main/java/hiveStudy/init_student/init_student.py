#   coding:utf-8
import random
import datetime
import sys
import imp

# reload(sys)
imp.reload(sys)
lastname = u"""赵李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏
水窦章云苏潘葛奚范彭郎鲁韦昌马苗"""
firstname = u"""红尘冷暖岁月清浅仓促间遗落一地如诗的句点不甘愿不决绝掬一份刻骨的思念
系一根心的挂牵在你回眸抹兰轩的底色悄然"""


def create_student_dict(start):
    firstlen = len(firstname)
    lastlen = len(lastname)
    # 创建一个符合正太分布的分数队列
    scorelist = [int(random.normalvariate(100, 50)) for _ in range(1, 5000)]
    # 创建1万条记录
    filename = str(start) + '.txt'
    with open('./' + filename, mode='wr+') as fp:
        for i in range(start * 40000, (start + 1) * 40000):
            firstind = random.randint(1, firstlen - 4)
            model = {"s_no": u"xuehao_" + str(i),
                     "s_name": u"{0}{1}".format(lastname[random.randint(1, lastlen - 1)],
                                                firstname[firstind:firstind + 1]),
                     "s_birth": u"{0}-{1}-{2}".format(random.randint(1991, 2000), '0' + str(random.randint(1, 9)),
                                                      random.randint(10, 28)),
                     "s_age": random.sample([20, 20, 20, 20, 21, 22, 23, 24, 25, 26], 1)[0],
                     "s_sex": str(random.sample(['男', '女'], 1)[0]),
                     "s_score": abs(scorelist[random.randint(1000, 4990)]),
                     "s_desc": u"为程序猿攻城狮队伍补充新鲜血液,"u"为祖国未来科技产业贡献一份自己的力量" * random.randint(1, 20)}
            # 写入模型数据到文件
            fp.write("{0}\t{1}\t{2}\t{3}\t{4}\t{5}\t{6}".format(model['s_no'], model['s_name'], model['s_birth'],
                                                                model['s_age'], model['s_sex'], model['s_score'],
                                                                model['s_desc']))


for i in range(1, 501):
    # starttime = datetime.datetime.now()
    create_student_dict(i)
