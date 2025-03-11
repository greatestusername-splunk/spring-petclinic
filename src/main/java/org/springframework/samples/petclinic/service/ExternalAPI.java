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

	// Increase the thread pool size to handle more concurrent tasks
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

	@Autowired
	public ExternalAPI(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public CompletableFuture<String> fetchExternalAPI() {
		Random random = new Random();
		int minSleep = 1;
		int maxSleep = 4000;
		double skewFactor = 1.7; // Greater than 1 to create a skewed distribution

		// Apply the skew function to create a biased distribution
		double randomValue = random.nextDouble();
		double skewedValue = Math.pow(randomValue, skewFactor);
		int sleepDuration = (int) (minSleep + (maxSleep - minSleep) * skewedValue);

		String url;
		if (sleepDuration > 1500) {
			url = "http://local-local:30727/check?customernum=123456789000";
			System.out.println("Delaying request for " + sleepDuration + " milliseconds");
			return delayBeforeRequest(() -> restTemplate.getForObject(url, String.class), sleepDuration);
		}
		else if (sleepDuration <= 25) {
			url = "http://0.0.0.0:30727/check?customernum=jrhicks";
			System.out.println("Expecting 500");
			return queryCreditCheck(() -> restTemplate.getForObject(url, String.class), sleepDuration / 4);
		}
		else {
			url = "http://0.0.0.0:30727/check?customernum=7064897";
			System.out.println("Fast sleep for " + sleepDuration + " milliseconds");
			return queryCreditCheck(() -> restTemplate.getForObject(url, String.class), sleepDuration);
		}
	}

	private <T> CompletableFuture<T> delayBeforeRequest(Supplier<T> supplier, int delay) {
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

		return future;
	}

	private <T> CompletableFuture<T> queryCreditCheck(Supplier<T> supplier, int delay) {
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

		return future;
	}

	// Example usage of fetchExternalAPI
	public void fetchAndProcess() {
		fetchExternalAPI().thenAccept(response -> {
			// Process the response
			System.out.println("Response received: " + response);
		}).exceptionally(ex -> {
			// Handle exceptions
			System.err.println("Error fetching API: " + ex.getMessage());
			return null;
		});
	}

}