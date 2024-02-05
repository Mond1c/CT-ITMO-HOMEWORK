#!/bin/bash
FILE=/home/artyom/Documents/SandboxBash/syslog
grep --perl-regexp "^[\d:,]+ INFO" $FILE > info.log

