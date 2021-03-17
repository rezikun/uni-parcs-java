all: run

clean:
	rm -f out/Main.jar out/DCT.jar

out/Main.jar:out/parcs.jar src/*.java
	@javac -cp out/parcs.jar src/*.java
	@jar cf out/Main.jar -C src .
	@rm -f src/*.class

out/DCT.jar: out/parcs.jar src/*.java
	@javac -cp out/parcs.jar src/*.java
	@jar cf out/DCT.jar -C src .
	@rm -f src/*.class

build: out/Main.jar out/DCT.jar

run: out/Main.jar out/DCT.jar
	@cd out && java -cp 'parcs.jar:Main.jar' Main