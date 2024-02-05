#!/bin/bash

proccess_name=task4_helper
first_pid=$(pgrep -o -f task4_helper)
cpulimit -p $first_pid -l 10 &
