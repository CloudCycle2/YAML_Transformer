#/bin/sh

iptables='/etc/sysconfig/iptables'

BalanceMemberIp=${Target_PublicIP}

ls /etc/httpd/conf.d/ | grep -q mod_proxy_balancer.conf 
if [ $? -ne 0 ]; then

echo """
<Proxy balancer://mycluster>
#AddBalancerMember
BalancerMember http://$BalanceMemberIp:$Target_httpdport/ route=server1
</Proxy>

ProxyPass / balancer://mycluster/ stickysession=BALANCEID

#AddProxyPassReverse
ProxyPassReverse / http://$BalanceMemberIp:$Target_httpdport/

#Setting Session cookie
Header add Set-Cookie \"BALANCEID=balancer.%{BALANCER_WORKER_ROUTE}e; path=/;\" env=BALANCER_ROUTE_CHANGED
Header add X-Var \"BALANCER_ROUTE_CHANGED=%{BALANCER_ROUTE_CHANGED}e\" env=BALANCER_ROUTE_CHANGED
Header add X-Var \"BALANCER_WORKER_ROUTE=%{BALANCER_WORKER_ROUTE}e\" env=BALANCER_WORKER_ROUTE
Header add X-Var \"BALANCER_SESSION_ROUTE=%{BALANCER_SESSION_ROUTE}e\" env=BALANCER_SEEION_ROUTE""">>/etc/httpd/conf.d/mod_proxy_balancer.conf

else
#check how many many balanced members are already there and give next free route
#maximum of apache server is 10
for number in 2 3 4 5 6 7 8 9 10
do
	cat /etc/httpd/conf.d/mod_proxy_balancer.conf | grep -q "route=server$number"
	if [ $? -ne 0 ]; then
		serverNumber=$number

## add BalancerMember
sed -i "/#AddBalancerMember/a\BalancerMember http://$BalanceMemberIp:$Target_httpdport/ route=server$serverNumber" /etc/httpd/conf.d/mod_proxy_balancer.conf
sed -i "/#AddProxyPassReverse/a\ProxyPassReverse / http://$BalanceMemberIp:$Target_httpdport/" /etc/httpd/conf.d/mod_proxy_balancer.conf

break
	fi
done

fi

#set the balancer-manager for managing the balancer over WebUI
cat /etc/httpd/conf/httpd.conf | grep -q "SetHandler balancer-manager"
if [ $? == 1 ]; then
echo """
<Location /balancer-manager>
	SetHandler balancer-manager
	#Order Deny,Allow
	#Deny from all
	#Allow from .example.com
</Location> """ >> /etc/httpd/conf/httpd.conf
fi


echo "ConfigureLoadBalancer httpdport: $Target_httpdport" 
# open firewall for LoadBalancing Port
#iptables-save | grep -q "OUTPUT -p tcp --sport $Target_httpdport -j ACCEPT"
#if [ $? -eq 1 ]; then
#	iptables -I OUTPUT -p tcp --sport $Target_httpdport -j ACCEPT
#fi

#iptables-save | grep -q "INPUT -p tcp --dport $Target_httpdport -j ACCEPT"
#if [ $? -eq 1 ]; then
#	iptables -I INPUT -p tcp --dport $Target_httpdport -j ACCEPT
#fi

iptables-save | grep -q "INPUT -p tcp --sport $Target_httpdport -j ACCEPT"
if [ $? -eq 1 ]; then
	iptables -I INPUT -p tcp --sport $Target_httpdport -j ACCEPT
fi

iptables-save | grep -q "OUTPUT -p tcp --dport $Target_httpdport -j ACCEPT"
if [ $? -eq 1 ]; then
	iptables -I OUTPUT -p tcp --dport $Target_httpdport -j ACCEPT
fi
	
	iptables-save > $iptables
	iptables-restore < $iptables


# make sure apache is running,
ps -A | grep -q httpd 
if [ $? -eq 1 ]; then
    echo "httpd is currently stopped, is getting started"
    service httpd start
    if [ $? -ne 0 ]; then
    	echo "killing httpd processes"
    	ps -ef | grep httpd | grep -v grep | awk '{print $2}' | xargs kill -9
    	service httpd start
    fi	
else
    echo "is beeing restartet"
    service httpd restart
    if [ $? -ne 0 ]; then
    	ps -ef | grep httpd | grep -v grep | awk '{print $2}' | xargs kill -9
    	service httpd start
    fi	
fi
