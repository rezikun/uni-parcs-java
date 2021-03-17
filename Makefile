all: run

clean:
	rm -f out/Main.jar out/DCT.jar

out/Main.jar: out/JTransforms-3.1-with-dependencies.jar out/parcs.jar src/*.java
	@javac -cp 'out/parcs.jar:out/JTransforms-3.1-with-dependencies.jar' src/*.java
	@jar cf out/Main.jar -C src .
	@rm -f src/*.class

out/DCT.jar: out/JTransforms-3.1-with-dependencies.jar out/parcs.jar src/DCT.java src/Cell.java src/ImageMatrix.java
	@javac -cp 'out/JTransforms-3.1-with-dependencies.jar:out/parcs.jar' src/DCT.java src/Cell.java src/ImageMatrix.java
	@jar cf out/DCT.jar -C src .
	@rm -f src/DCT.class src/Cell.class src/ImageMatrix.class

build: out/Main.jar out/DCT.jar

run: out/Main.jar out/DCT.jar
	@cd out && java -cp Main.jar Main