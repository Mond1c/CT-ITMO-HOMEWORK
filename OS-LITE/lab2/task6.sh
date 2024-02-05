#!/bin/bash

max_ram=0
max_pid=-1
max_name=""

for pid in $(ps axo pid | tail -n +2)
do
	if [[ -a /proc/$pid/status ]]
	then
		cur_ram=$(cat /proc/$pid/status | grep "VmRSS" | awk '{print $2}')
		if [[ $cur_ram -gt $max_ram ]] 
		then
			max_ram=$cur_ram
			max_pid=$pid
			max_name=$(cat /proc/$pid/status | grep "Name" | awk '{print $2}')
		fi
	fi
done

echo "NAME = $max_name; PID = $max_pid; RAM = $max_ram kB"

