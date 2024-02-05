#!/bin/bash

TIME_SLEEP=20

TEMP=$(for pid in $(ps axo pid | tail -n +2) 
do
	if [[ -a /proc/$pid/io ]] 
	then
		size=$(cat /proc/$pid/io | grep "read_bytes" | awk '{print $2}')
		if [[ -z $size ]]
		then
			size=0
		fi
		echo $pid $size
	fi
done)

sleep $TIME_SLEEP

while read line
do
	pid=$(echo $line | cut -d ' ' -f1)
	prev_size=$(echo $line | cut -d ' ' -f2)
	if [[ -a /proc/$pid/io ]]
	then
		cur_size=$(cat /proc/$pid/io | grep "read_bytes" | awk '{print $2}')
		cmd=$(ps axo pid:1,command:1 | grep -E "^\w*\s*$pid " | cut -d ' ' -f2-)
		if [[ -z $cur_size ]] || [[ -z $cmd ]]
		then
			continue
		fi
		name=$(cat /proc/$pid/status | grep "Name" | awk '{print $2}')
		delta=$(( $cur_size - $prev_size ))
		echo "$pid:$cmd:$delta"
	fi
done <<< $TEMP | sort -t ':' -rnk3 | head -n3

