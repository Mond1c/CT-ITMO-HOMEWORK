@javac ^
    -encoding utf-8 ^
    -d __out ^
    RunJS.java ^
 && java -ea --module-path=jstest.graal -cp __out RunJS %*
