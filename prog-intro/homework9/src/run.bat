javac -cp . markdown/*.java &&
javac -cp . md2html/*.java &&
java -Dfile.encoding="UTF-8" -ea -jar Md2HtmlTest.jar Image &&
rm markdown/*.class &&
rm md2html/*.class