package lambda;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test1 {

	public static void main(String[] args) {
		List<Integer> res1 = Arrays.asList(5, 3, 4, 1, 2).stream().filter(i -> i % 2 != 0).map(i -> i + 1).collect(Collectors.toList());
		System.out.println(res1.toString());
		
		List<Object> res2 = Arrays.asList(5, 3, 4, 1, 2).stream().map(i -> i % 2 == 0 ? i += 1 : i).collect(Collectors.toList());
		System.out.println(res2.toString());
	}
}
