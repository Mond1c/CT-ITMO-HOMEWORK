#!/bin/bash

TRASH_DIR=/home/artyom/Documents/itmo/OS/lab4/task1_2/.trash/
TRASH_LOG=$TRASH_DIR.trash.log
TRASH_LOG_NEW=$TRASH_DIR.trash.log.backup

function my_exit() {
	if [[ $2 -ne 0 ]] 
	then
		echo $1
		echo "Usage: $0 <name_of_file>"
	fi
	if [[ -f $TRASH_LOG_NEW ]] 
	then
		cp $TRASH_LOG_NEW $TRASH_LOG
		rm $TRASH_LOG_NEW
	fi
	exit $2
}

if [[ $# -ne 1 ]]
then
	my_exit "Expected 1 argument, found $#" 1
fi


if [[ ! -d $TRASH_DIR ]] 
then
	mkdir $TRASH_DIR
fi

if [[ ! -f $TRASH_LOG ]]
then
	touch $TRASH_LOG
fi

if [[ -f $TRASH_LOG_NEW ]]
then
	rm $TRASH_LOG_NEW
fi
touch $TRASH_LOG_NEW


found_anything=0
while read -r line
do
	old_full_filename=$(echo "$line" | cut -d ' ' -f 1)
	old_name=$(basename $old_full_filename)
	old_dir=$(dirname $old_full_filename)/
	new_name=$(echo "$line" | cut -d ' ' -f 2)
	if [[ $old_name == $1 ]]
	then
		ans=""
		found_anything=1
		while [[ $ans != n && $ans != y ]]
		do
			echo "Do you want to recover \"$old_dir$old_name\"? [y/n]"
			read ans<&1
			if [[ $ans == y ]]
			then
				if [[ ! -d $old_dir ]]
				then
					echo "Directory $old_dir does not exist. Recovering to $HOME"
					old_dir=$HOME/
				fi
				while [[ -f $old_dir$old_name ]]
				do
					echo "There is $old_name file already. New name of file: "
					read old_name<&1
				done
				ln $TRASH_DIR$new_name $old_dir$old_name
				rm $TRASH_DIR$new_name
			else
				echo $line >> $TRASH_LOG_NEW
			fi 
		done
	else
		echo $line >> $TRASH_LOG_NEW
	fi
done <<< $(cat $TRASH_LOG)

if [[ $found_anything == 0 ]]
then
	my_exit "No files found with name \"$1\"" 1
fi

my_exit "" 0
