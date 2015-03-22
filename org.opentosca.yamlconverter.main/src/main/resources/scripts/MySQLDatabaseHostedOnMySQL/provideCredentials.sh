#!/bin/bash

MySQLAdminUser=root
MySQLAdminPwd=$Target_RootPassword
DBName=$Source_DBName

# file to pass admin credentials to SugarCRM DB Node
# this is used due to lack of parameter passing in TOSCA
CredentialsDir=/tmp/tosca/$DBName
CredentialsFile=$CredentialsDir/mysql_access

mkdir -p $CredentialsDir
touch $CredentialsFile
chmod 600 $CredentialsFile
echo "MySQLAdminUser=$MySQLAdminUser" > $CredentialsFile
echo "MySQLAdminPwd=$MySQLAdminPwd" >> $CredentialsFile

