package lambda;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LambdasTest {

	public static void main(String[] args) {
		//Supplier: no input, only output
		Supplier<Integer> s1 = () -> "Hello world".length();
		System.out.println(s1.get());
		
		//Consumer: only input, no output
		Consumer<String> c1 = s -> System.out.println(s);
		c1.accept("Hello world");
		Consumer<String> c2 = System.out::println;
		Consumer<String> c3 = c2.andThen(s -> System.out.println(s.concat("test123")));
		c3.accept("foobar");
		
		//Function: input and output
		Function<Integer, Integer> f1 = i -> i * i;
		System.out.println(f1.apply(5));
		
		//Predicate: input and boolean output
		Predicate<File> p1 = f -> f.isDirectory();
		System.out.println(p1.test(new File("src\\lambda")));
	}
}
