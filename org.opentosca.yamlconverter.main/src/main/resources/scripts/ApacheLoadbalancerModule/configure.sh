#/bin/sh

#check if modules are loaded
cat /etc/httpd/conf/httpd.conf | grep -q "LoadModule proxy_balancer_module" 
if [ $? == 1 ]; then

sed -i ':begin;$!N;s/# LoadModule foo_module\n#/a\
LoadModule proxy_module modules/mod_proxy.so\
LoadModule proxy_balancer_module modules/mod_proxy_balancer.so\
LoadModule proxy_ftp_module modules/mod_proxy_ftp.so\
LoadModule proxy_http_module modules/mod_proxy_http.so\
LoadModule proxy_connect_module modules/mod_proxy_connect.so\
' /etc/httpd/conf/httpd.conf
fi