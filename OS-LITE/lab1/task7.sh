#!/bin/bash
OUTPUT=emails.lst
grep --no-messages --perl-regexp -o -h "[\d\w\.]+@[\d\w]+\.[\d\w]+" /etc/* | paste -sd "," > $OUTPUT

