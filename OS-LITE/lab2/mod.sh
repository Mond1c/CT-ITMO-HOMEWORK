#!/bin/bash

processes=$(ps axo user,pid,stat --no-headers --sort stime | grep "R")

last_user="/"
while read line
do
	cur_user=$(echo "$processes" | cut -d ' ' -f1)
	if [[ $cur_user != $last_user ]]
	then
		echo $line
		last_user=$cur_user
	fi
done <<< $processes

