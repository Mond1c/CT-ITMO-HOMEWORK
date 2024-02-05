#!/bin/bash
let COUNTER=0
wc -l /var/log/*.log | tail -1 | awk '{print $1}'

