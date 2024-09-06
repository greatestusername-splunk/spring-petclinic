package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class ExternalAPI {

	private final RestTemplate restTemplate;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	@Autowired
	public ExternalAPI(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String fetchExternalAPI() {
		Random random = new Random();
		int minSleep = 1;
		int maxSleep = 4000;
		double skewFactor = 1.7; // Greater than 1 to create a skewed distribution

		// Apply the skew function to create a biased distribution
		double randomValue = random.nextDouble();
		double skewedValue = Math.pow(randomValue, skewFactor);
		int sleepDuration = (int) (minSleep + (maxSleep - minSleep) * skewedValue);

		String url;
		if (sleepDuration > 500) {
			url = "http://0.0.0.0:30727/check?customernum=123456789000";
			System.out.println("Sleep for " + sleepDuration + " milliseconds");
			return queryCreditCheck(() -> restTemplate.getForObject(url, String.class), sleepDuration);
		}
		else if (sleepDuration <= 25) {
			url = "http://0.0.0.0:30727/check?customernum=jrhicks";
			System.out.println("expecting 500");
			return queryCreditCheck(() -> restTemplate.getForObject(url, String.class), sleepDuration / 4);
		}
		else {
			url = "http://0.0.0.0:30727/check?customernum=7064897";
			System.out.println("Fast sleep for " + sleepDuration / 4 + " milliseconds");
			return queryCreditCheck(() -> restTemplate.getForObject(url, String.class), sleepDuration / 4);
		}
	}

	private <T> T queryCreditCheck(Supplier<T> supplier, int delay) {
		CompletableFuture<T> future = new CompletableFuture<>();
		scheduler.schedule(() -> {
			try {
				T result = supplier.get();
				future.complete(result);
			}
			catch (Exception e) {
				future.completeExceptionally(e);
			}
		}, delay, TimeUnit.MILLISECONDS);

		try {
			return future.get(); // Blocks until the future is completed
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to fetch External API", e);
		}
	}

}
