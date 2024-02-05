#!/bin/bash

quit()
{
	killall tail
	kill $(cat .pid)
	exit
}

MODE=sum
ANSWER=0

(tail -f pipe) |
while true; do
	read LINE;
	if [[ "$LINE" == "*" ]]
	then
		echo "MUL mode"
		MODE=mul
	elif [[ "$LINE" == "+" ]]
	then
		echo "SUM mode"
		MODE=sum
	elif [[ "$LINE" == "QUIT" ]]
	then
		echo "Exiting (user aborted)."
		quit
	elif [[ "$LINE" =~ ^[-]?[0-9]+ ]]
	then
		case "$MODE" in
			sum)
				ANSWER=$(($ANSWER+$LINE))
				echo "$ANSWER"
				;;
			mul)
				ANSWER=$(($ANSWER*$LINE))
				echo "$ANSWER"
				;;
			*)
				echo "BAD OPERATION?"
				;;
		esac
	else 
		echo "Found unsupported symbol: $LINE"
		quit
	fi
done
