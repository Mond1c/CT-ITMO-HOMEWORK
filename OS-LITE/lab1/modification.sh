#!/bin/bash

# Обработать файл .sh: удалить все комментарии из него (исключая shebang)
# bash modification.sh <input_file> <output_file>

sed "/^#!/p; /#.*/d" $1 > $2

