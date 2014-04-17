package lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CollectionsTest {

	public static void main(String[] args) {
		//foreach
		Arrays.asList(4, 3, 2, 1).stream().forEach(System.out::println);
		
		//filter
		List<Integer> res = Arrays.asList(4, 3, 2, 1).stream()
													 .filter(i -> i % 2 == 0)
													 .sorted()
													 .collect(Collectors.toList());
		System.out.println(res);
		
		//max
		Optional<Integer> optional1 = Arrays.asList(4, 3, 2, 1).stream().max(Comparator.naturalOrder());
		System.out.println(optional1.get());
//		Optional<Integer> optional2 = new ArrayList<Integer>().stream().max(Comparator.naturalOrder());
//		System.out.println(optional2.get());		=> NSE Exception!!!
		Integer optional3 = new ArrayList<Integer>().stream().max(Comparator.naturalOrder()).orElse(-1);
		System.out.println(optional3);
		
		//groupBy
		Map<String, List<Integer>> grouping = Arrays.asList(4, 3, 2, 1).stream().collect(Collectors.groupingBy(i -> i % 2 == 0 ? "even" : "odd"));
		System.out.println(grouping);
	}
}
