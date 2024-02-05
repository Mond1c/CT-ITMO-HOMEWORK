#!/bin/bash

INPUT=FILE4
OUTPUT=FILE5
ppid=0
sum=0
cnt=0
while read line
do
	cur_ppid=$(echo $line | awk '{print $3}' | cut -d '=' -f 2)
	if [[ $ppid -ne $cur_ppid ]]
	then
		art=$(echo $sum $cnt | awk '{printf "%.2f", $1/$2}')
		echo Average_Running_Children_of_ParentID=$ppid is $art
		sum=0
		cnt=0
		ppid=$cur_ppid
	fi
	art=$(awk '{print $5}' <<< $line | cut -d '=' -f 2)
	sum=$(echo $art $sum | awk '{printf "%.2f", $1+$2}')
	cnt=$(( $cnt + 1 ))
	echo $line
done < $INPUT > $OUTPUT

if [[ 0 -lt $cnt ]]
then
	art=$(echo $sum $cnt | awk '{printf "%.2f", $1/$2}')
	echo Average_Running_Children_ParentID=$ppid is $art
fi >> $OUTPUT

