#!/bin/bash

ps axo pid,stime --sort stime | tail -n 5 | head -n 1

