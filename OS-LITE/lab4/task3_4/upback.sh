#!/bin/bash

PSEUDO_HOME=/home/artyom/Documents/itmo/OS/lab4/task3_4
RESTORE_DIR=$PSEUDO_HOME/restore

last_backup_date=$(ls $PSEUDO_HOME | grep -P "Backup-(\d){4}-(\d){2}-(\d){2}" | sort | tail -1 | awk -F "-" '{print $2"-"$3"-"$4}')

if [[ -z last_backup_date ]] 
then
	echo "There're no backups."
	exit 1
fi

BACKUP_DIR=$PSEUDO_HOME/Backup-$last_backup_date

if [[ ! -d $RESTORE_DIR ]] 
then
	mkdir $RESTORE_DIR
fi

for file in $( find $BACKUP_DIR -type f | grep -v -P "\.(\d){4}-(\d){2}-(\d){2}")
do
	relative_filename=$(sed "s;$BACKUP_DIR;;g" <<< $file)
	needed_directories=$(dirname $relative_filename)
	if [[ ! -z $needed_directories ]]
	then
		mkdir -p $RESTORE_DIR/$needed_directories
	fi
	cp $file $RESTORE_DIR/$relative_filename
done
