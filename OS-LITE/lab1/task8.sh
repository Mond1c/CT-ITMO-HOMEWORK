#!/bin/bash
INPUT=/etc/passwd
awk -F ':' '{print $1 ":" $3}' $INPUT | sort -t ':' -nk2 

