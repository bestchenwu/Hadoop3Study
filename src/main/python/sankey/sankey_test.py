# -*- coding:GBK -*-
# 参考链接https://zhuanlan.zhihu.com/p/80410924
# 导入相关库
import pandas as pd
from pyecharts.charts import Page, Sankey
from pyecharts import options as opts
import sys
import os
preg_mongth2 = sys.argv[1]
mall_flag = sys.argv[2]
# 读取csv文件
data = pd.read_csv(r'sample.csv',encoding='gbk',header=None,skiprows=1)
data.columns=['preg','mall_flag','source','target','uv']
#print(data)
filter_data1 = data[data['preg'] == preg_mongth2]
# filter_data = filter_data1[filter_data1[1] == mall_flag]
# #filter_data.drop(['preg_month2','mall_flag'],axis=1)
#print(filter_data1)
filter_data2 = filter_data1.drop(columns=['preg','mall_flag'])
#print(filter_data2)
# 生成nodes
nodes = []
all_path = set()
for element in filter_data2['source'].unique():
    all_path.add(element)
for element in filter_data2['target'].unique():
    all_path.add(element)
#print(all_path)
# for element in filter_data1[]
for i in all_path:
    dic = {}
    dic['name'] = i
    nodes.append(dic)
#print(nodes)
# # 生成links
links = []
for i in filter_data2.values:
    dic = {}
    dic['source'] = i[0]
    dic['target'] = i[1]
    dic['value'] = i[2]
    links.append(dic)
#print(links)

# pyecharts 所有方法均支持链式调用。
c = (
    Sankey()
        .add(
        "费用/元",
        nodes,
        links,
        linestyle_opt=opts.LineStyleOpts(opacity=0.2, curve=0.5, color="source",type_="dotted"),
        label_opts=opts.LabelOpts(position="right",),
    )
        .set_global_opts(title_opts=opts.TitleOpts(title="桑基图"))
)
# 输出html可视化结果
c.render('result.html')