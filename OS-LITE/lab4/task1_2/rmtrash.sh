#!/bin/bash

TRASH_DIR=/home/artyom/Documents/itmo/OS/lab4/task1_2/.trash/
TRASH_LOG=$TRASH_DIR.trash.log

if [[ $# -ne 1 ]]
then
	echo "Expected 1 argument, found $#"
	echo "Usage: $0 <name_of_file>"
	exit 1
fi

if [[ ! -f $1 ]]
then
	echo "File \"$1\" not found"
	echo "Usage: $0 <name_of_file>"
	exit 1
fi

if [[ ! -d $TRASH_DIR ]]
then
	mkdir $TRASH_DIR
fi

if [[ ! -f $TRASH_DIR$TRASH_LOG ]]
then
	touch $TRASH_LOG
fi

NEW_NAME=0
while true
do
	RESULT=$(find "$TRASH_DIR" -name "$NEW_NAME")
	if [[ -z "$RESULT" ]] 
	then
		break
	fi
	NEW_NAME=$(($NEW_NAME + 1))
done

ln $1 "$TRASH_DIR"/$NEW_NAME &&
rm -f $1 &&
echo $(realpath $1) $NEW_NAME >> $TRASH_LOG
