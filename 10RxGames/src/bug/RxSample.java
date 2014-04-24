package bug;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;

public class RxSample {

	public static void main(String[] args) throws IOException {
		Observable.concat(Observable.from(1, 2, 3), Observable.never()).sample(1L, TimeUnit.SECONDS).subscribe(System.out::println);
		System.in.read();
	}
}
