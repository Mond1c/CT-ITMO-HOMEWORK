#!/bin/bash

echo $$ > .pid

RES=0
MODE=""

usr1()
{
	echo "SUM mode"
	MODE="sum"
}

usr2()
{
	echo "MUL mode"
	MODE="mul"
}

quit()
{
	MODE="quit"
}

trap "usr1" USR1
trap "usr2" USR2
trap "quit" SIGTERM

while true
do
	case $MODE in
		"sum")
			RES=$(($RES+2))
			echo $RES
			;;
		"mul")
			RES=$(($RES*2))
			echo $RES
			;;
		"quit")
			echo "STOPPED BY SIGTERM"
			exit
			;;
	esac
	sleep 1
done
