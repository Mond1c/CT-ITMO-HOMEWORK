#!/bin/bash

cd /home/artyom/Documents/itmo/OS/lab3

mkdir test && 
	echo "catalog test was created successfully" >> report && 
	touch test/$(date "+%Y_%H:%M:%S")

ping www.net_nikogo.ru -c 1 || 
	echo $(date "+%Y_%H:%M:%S")" -- couldn't ping net_nikogo.ru" >> report 
