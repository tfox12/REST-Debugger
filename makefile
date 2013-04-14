
all :
	ant

jar :
	ant jar

dist :
	ant jar

run :
	ant jar && java -jar dist/daemon.jar

