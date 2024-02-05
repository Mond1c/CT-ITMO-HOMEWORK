#!/bin/bash

USER=artyom
OUTPUT=FILE1
RESULT=$(ps aux | grep -P "^$USER" | awk '{print $2 ":" $11}')
wc -l <<< $RESULT > $OUTPUT
awk '{print $0}' <<< $RESULT >> $OUTPUT 

