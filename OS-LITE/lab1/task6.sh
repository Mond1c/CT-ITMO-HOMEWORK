#!/bin/bash
INPUT=/home/artyom/Documents/SandboxBash/X.log
OUTPUT=full.log
grep --perl-regexp "^\[\s*[\d.]+\]\s\(WW\)" $INPUT | sed "s/(WW)/Warning:/" > $OUTPUT
grep --perl-regexp "^\[\s*[\d.]+\]\s\(II\)" $INPUT | sed "s/(II)/Information:/" >> $OUTPUT
cat $OUTPUT

