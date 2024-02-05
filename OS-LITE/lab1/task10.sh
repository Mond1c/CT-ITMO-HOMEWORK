#!/bin/bash
man bash 				| 
tr '[[:upper:]]' '[[:lower:]]' 		| 
grep -P -o "[\w]{4,}" 			| 
sort 					| 
uniq -c 				| 
sort -nrk1 				| 
head -3

