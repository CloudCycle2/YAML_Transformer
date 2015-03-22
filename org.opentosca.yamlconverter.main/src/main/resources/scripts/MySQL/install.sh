#!/bin/bash

echo "chkconfig mysqld"
chkconfig mysqld on

mycnf='/etc/my.cnf'

#inserting data into /etc/my.cnf

sed -i '
/\[mysqld\]/ a\
log-warnings=2\
log-error=/var/log/mysqld.lo\
pid-file=/var/run/mysqld/mysqld.pid\
default-storage-engine=innodb\
' $mycnf

/usr/bin/mysql_install_db
