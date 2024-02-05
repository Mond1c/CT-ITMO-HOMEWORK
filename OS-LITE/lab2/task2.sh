#!/bin/bash

OUTPUT=FILE2
ps aux | awk '{if ($11 ~ /^\/sbin/) {print $2 ":" $11}}' >> $OUTPUT

