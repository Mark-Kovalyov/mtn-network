install:
	mvn install
	mvn source:jar install

test:
	mvn clean test -Dtesttype=prop
	mvn clean test -Dtesttype=jupiter
