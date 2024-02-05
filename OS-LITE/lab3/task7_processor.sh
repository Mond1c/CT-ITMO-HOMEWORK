#!/bin/bash

echo $$ > .pid

PLAYER=""
CPU=""

print_rules()
{
	echo ""
	echo "Choose option: (r)ock, (p)aper, (s)cissors, (q)uit."
}

get_cpu_move()
{
	res=$(($RANDOM % 3))
	case $res in
		0)
			echo "r"
			;;
		1)
			echo "p"
			;;
		2)
			echo "s"
			;;
	esac
}

rock()
{
	play "r"
}

paper()
{
	play "p"
}

scissors()
{
	play "s"
}

play()
{
	PLAYER_MOVE=$1
	CPU_MOVE=$(get_cpu_move)
	echo "Player ($PLAYER_MOVE) vs CPU ($CPU_MOVE)"
	if [[ $PLAYER_MOVE == $CPU_MOVE ]]
	then
		echo "Draw. -- $PLAYER_MOVE was played by both."
		print_rules
		return
	fi
	if [[ $PLAYER_MOVE == "r" ]]
	then
		if [[ $CPU_MOVE == "p" ]]
		then
			echo "You lost. Paper covers rock."
		else
			echo "You won. Rock breaks scissors."
		fi
	elif [[ $PLAYER_MOVE == "s" ]]
	then
		if [[ $CPU_MOVE == "p" ]]
		then
			echo "You won. Scissors cuts paper."
		else
			echo "You lost. Rock breaks scissors."
		fi

	else
		if [[ $CPU_MOVE == "s" ]]
		then
			echo "You lost. Scissors cuts paper."
		else
			echo "You won. Paper covers Rock."
		fi

	fi
	print_rules
}

quit()
{
	echo "Quiting by user..."
	exit 0
}

trap "rock" USR1
trap "paper" USR2
trap "scissors" XFSZ
trap "quit" SIGTERM

# The SIGXFSZ signal is sent to a process when it grows a file that exceeds the maximum allowed size.

print_rules

while true
do
	:
done
