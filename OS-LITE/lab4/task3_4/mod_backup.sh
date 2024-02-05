#!/bin/bash

PSEUDO_HOME=/home/artyom/Documents/itmo/OS/lab4/task3_4
SOURCE=$PSEUDO_HOME/source
LOG_FILE=$PSEUDO_HOME/backup-report

today=$(date '+%Y-%m-%d')
last_backup_date=$(ls $PSEUDO_HOME | grep -P "Backup-(\d){4}-(\d){2}-(\d){2}" | sort | tail -1 | awk -F "-" '{print $2"-"$3"-"$4}')

today_unix_time=$(date --date=$today +%s)
last_backup_unix_time=$(date --date=$last_backup_date +%s)
diff=$(($today_unix_time - $last_backup_unix_time))

if [[ diff -le $((7 * 24 * 60 * 60)) ]] && [[ ! -z $last_backup_date ]]
then
	# Already exists.
	#
	BACKUP_DIR="$PSEUDO_HOME/Backup-$last_backup_date"
	echo "Updating backup at $last_backup_date."
	
	echo "Added: "
	for file in $( find $SOURCE -type f )
	do
		relative_filename=$(sed "s;$SOURCE/;;g" <<< $file)
		if [[ ! -f $BACKUP_DIR/$relative_filename ]] 
		then
			echo -e "\t$relative_filename"
			needed_directories=$(dirname $relative_filename)
			if [[ ! -z $needed_directories ]]
			then
				mkdir -p $BACKUP_DIR/$needed_directories
			fi
			cp $file $BACKUP_DIR/$relative_filename
		fi
	done

	echo "Modified: "
	for file in $( find $SOURCE -type f )
	do
		relative_filename=$(sed "s;$SOURCE/;;g" <<< $file)
		if [[ -f $BACKUP_DIR/$relative_filename ]] 
		then
			## MODIFICATION: if files are equal by size, then compare them by contents 
			if [[ $(stat --printf="%s" $file) -eq $(stat --printf="%s" $BACKUP_DIR/$relative_filename) ]]
			then
				diff $BACKUP_DIR/$relative_filename $file > /dev/null 2>&1
				if [[ $? == 0 ]]
				then
					continue
				fi
			fi
			echo -e "\t $relative_filename $relative_filename.$today"
			cp $BACKUP_DIR/$relative_filename $BACKUP_DIR/$relative_filename.$today
			cp $file $BACKUP_DIR/$relative_filename
		fi
	done
else 
	# Creating.
	
	BACKUP_DIR="$PSEUDO_HOME/Backup-$today"
	cp -r $SOURCE $BACKUP_DIR
	echo "Backup done at $today to directory $BACKUP_DIR"
	echo "Added: "
	for file in $( find $SOURCE -type f  )
	do
		echo -e "\t$file"
	done
fi >> $LOG_FILE

echo "===============" >> $LOG_FILE

