# webflux-todo

## 使い捨てMySQLサーバの立て方
```
# 起動
docker run --name test-mysql -e MYSQL_ROOT_PASSWORD=mysql -p 33306:3306 -d mysql
# ログイン
mysql -h 127.0.0.1 --port 33306 -uroot -p
```
