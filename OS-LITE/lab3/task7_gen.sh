#!/bin/bash

while true
do
	read LINE
	case $LINE in
		"r")
			kill -USR1 $(cat .pid)
			;;
		"p")
			kill -USR2 $(cat .pid)
			;;
		"s")
			kill -XFSZ $(cat .pid)
			;;
		"q")
			kill -SIGTERM $(cat .pid)
			exit
			;;
		*)
			:
			;;
	esac
done

