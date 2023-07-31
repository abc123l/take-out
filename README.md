# take-out
This is  a take-out project
# 项目难点
1. 在数据库里保存的id是雪花算法生成的id有19位，而客户端发送的id只有近似到16位（对于数据类型），所以在将对象转换成json格式的时候将id转换成了字符串。
2. ThreadLocal的使用

![image-20230724100007648](https://cdn.jsdelivr.net/gh/abc123l/ImagesForMD@main//image-20230724100007648.png)
