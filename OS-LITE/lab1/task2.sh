#!/bin/bash/

S=""

read TEMP
while [[ $TEMP != q ]]
do
	S=$S$TEMP
	read TEMP
done

echo $S

