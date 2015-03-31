#!/bin/bash

httpdControl='service httpd'
httpdConf='/etc/httpd/conf/httpd.conf'

#set initial php config
cp /etc/php.ini /usr/local/lib/php.ini

echo "LoadModule php5_module /usr/lib64/httpd/modules/libphp5.so">>$httpdConf
echo "AddType application/x-tar .tgz">>$httpdConf
echo "AddType application/x-httpd-php .php .phtml">>$httpdConf
echo "AddType application/x-httpd-php-source .phps">>$httpdConf

#chcon -t textrel_shlib_t /usr/lib64/httpd/modules/libphp5.so
semanage fcontext -a -t textrel_shlib_t /usr/lib/httpd/modules/libphp5.so

### only for testing the php module
echo "<?php phpinfo(); ?>" > /var/www/html/test.php


# activate changes through restarting apache
ps -A | grep -q httpd 
if [ $? -eq 1 ]; then
    echo "httpd is currently stopped, is getting started, and stopped again"
    service httpd start
    
    if [ $? -ne 0 ]; then
    	echo "killing httpd processes"
    	ps -ef | grep httpd | grep -v grep | awk '{print $2}' | xargs kill -9
    	service httpd start
    fi	
        
    ps -A | grep -q httpd 
	if [ $? -eq 0 ]; then
        service httpd stop
    fi    
else
    echo "httpd is beeing restarted, and stopped again"
    service httpd restart
    if [ $? -ne 0 ]; then
    	echo "killing httpd processes"
    	ps -ef | grep httpd | grep -v grep | awk '{print $2}' | xargs kill -9
    	service httpd start
    	
    fi	
    
	ps -A | grep -q httpd 
	if [ $? -eq 0 ]; then
        service httpd stop
    fi 
fi