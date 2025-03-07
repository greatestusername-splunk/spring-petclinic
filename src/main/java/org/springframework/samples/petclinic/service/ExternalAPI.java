package org.springframework.samples.petclinic.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ExternalAPI {

	private final RestTemplate restTemplate;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

	@Autowired
	public ExternalAPI(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String fetchExternalAPI() {
		Random random = new Random();
		int minSleep = 1;
		int maxSleep = 4000;
		double skewFactor = 1.7;

		double randomValue = random.nextDouble();
		double skewedValue = Math.pow(randomValue, skewFactor);
		int sleepDuration = (int) (minSleep + (maxSleep - minSleep) * skewedValue);

		final String url;
		if (sleepDuration > 500) {
			url = "http://0.0.0.0:30727/check?customernum=123456789000";
			System.out.println("Sleep for " + sleepDuration + " milliseconds");
		}
		else if (sleepDuration <= 25) {
			url = "http://0.0.0.0:30727/check?customernum=jrhicks";
			System.out.println("Expecting 500");
			sleepDuration /= 4;
		}
		else {
			url = "http://0.0.0.0:30727/check?customernum=7064897";
			System.out.println("Fast sleep for " + sleepDuration / 4 + " milliseconds");
			sleepDuration /= 4;
		}

		vettingProcess(sleepDuration);

		CompletableFuture.supplyAsync(() -> {
			try {
				return restTemplate.getForObject(url, String.class);
			}
			catch (RestClientException e) {
				System.err.println("Error fetching External API: " + e.getMessage());
				return "Error: Unable to fetch external API data.";
			}
		}, scheduler).thenAccept(result -> {
			System.out.println("Result received: " + result);
		}).exceptionally(e -> {
			System.err.println("Unexpected error: " + e.getMessage());
			return null;
		});

		return "Operation started asynchronously";
	}

	private void vettingProcess(int millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			System.err.println("Sleep interrupted: " + e.getMessage());
		}
	}

}