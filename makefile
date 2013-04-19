
all :
	ant

jar :
	ant jar

dist :
	ant jar

run :
	ant jar
	java -Xmx2048m -jar dist/daemon.jar

