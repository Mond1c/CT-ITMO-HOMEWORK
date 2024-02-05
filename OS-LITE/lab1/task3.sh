#!/bin/bash/

while true
do
	echo "	0: exit
	1: vi
	2: nano
	3: links"
	read CHOICE
	case $CHOICE in
		0)
			exit
		;;

		1) 
			vi
		;;
	
		2) 
			nano
		;;

		3) 
			links
		;;
	esac
done

