#!/bin/bash

ps -A | grep -q httpd 
if [ $? -eq 0 ]; then
	service httpd stop
fi  
