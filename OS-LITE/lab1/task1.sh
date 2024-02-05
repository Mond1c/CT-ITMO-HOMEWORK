#!/bin/bash

X=$1
if [[ X -lt $2 ]]
then
	X=$2
fi

if [[ X -lt $3 ]]
then
	X=$3
fi

echo $X

