package lambda;

import java.util.Optional;

public class OptionalTest {

	public static void main(String[] args) {
		Optional<Integer> optI = Optional.of(12);
		Optional<Integer> map = optI.map(i -> i + 1);
		System.out.println("map: " + map);
		
		Optional<Integer> empty = Optional.empty();
		Optional<Integer> emptyMap = empty.map(i -> i + 1);
		System.out.println("emptyMap: " + emptyMap);
		
		Optional<String> optS = Optional.of("foobar");
		Optional<String> flatMap = optS.flatMap(s -> optI.map(i -> s + i));
		System.out.println("flatMap: " + flatMap);
		
		Object obj = flatMap.get();
		System.out.println("obj: " + obj);
		
		Optional<Integer> filteredEven = optI.filter(i -> i % 2 == 0);
		Optional<Integer> filteredOdd = optI.filter(i -> i % 2 == 1);
		System.out.println("filteredEven: " + filteredEven);
		System.out.println("filteredOdd: " + filteredOdd);
		filteredEven.ifPresent(i -> System.out.println(i + " was even"));
		filteredOdd.ifPresent(i -> System.out.println(i + " was odd"));
		
		Integer evenOrElse = filteredEven.orElse(-2);
		Integer oddOrElse = filteredOdd.orElse(-1);
		System.out.println("filteredEven or else: " + evenOrElse);
		System.out.println("filteredOdd or else: " + oddOrElse);
	}
}
