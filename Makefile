all: run

clean:
	rm -f out/Main.jar out/Runner.jar

out/Main.jar: out/parcs.jar src/*.java
	@javac -cp out/parcs.jar src/*.java
	@jar cf out/Main.jar -C src Main.class -C src Runner.class -C src Cell.class -C src ImageMatrix.class
	@rm -f src/*.class

out/Runner.jar: out/parcs.jar src/Runner.java src/Cell.java src/ImageMatrix.java src/Dct.java
	@javac -cp out/parcs.jar src/Dct.java src/Runner.java src/Cell.java src/ImageMatrix.java
	@jar cf out/Runner.jar -C src Runner.class -C src Cell.class -C src ImageMatrix.class -C src Dct.class
	@rm -f src/Runner.class src/Cell.class src/ImageMatrix.class src/Dct.class

build: out/Main.jar out/Runner.jar

run: out/Main.jar out/Runner.jar
	@cd out && java -cp parcs.jar:Main.jar Main