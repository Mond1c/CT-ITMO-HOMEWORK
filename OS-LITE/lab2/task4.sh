#!/bin/bash

OUTPUT=FILE4

for pid in $(ps axo pid | tail -n +2)
do
	if [[ -a /proc/$pid/status ]]
	then
		ppid=$(cat /proc/$pid/status | grep "PPid" | awk '{print $NF}')
		sum=$(cat /proc/$pid/sched | grep "sum_exec_runtime" | awk '{print $NF}')
		cnt=$(cat /proc/$pid/sched | grep "nr_switches" | awk '{print $NF}')
		art=$(echo $sum $cnt | awk '{printf "%.2f", $1/$2}')

		RESULT=""
		RESULT+="ProcessID="$pid
		RESULT+=" : "
		RESULT+="Parent_ProccessID="$ppid
		RESULT+=" : "
		RESULT+="Average_Running_Time="$art
		echo $RESULT
	fi
done | sort -Vk2 > $OUTPUT

