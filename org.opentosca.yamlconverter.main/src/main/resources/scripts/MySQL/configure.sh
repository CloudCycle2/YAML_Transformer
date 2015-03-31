#!/bin/bash

service mysqld start

#set root password
mysqladmin -u root password $RootPassword