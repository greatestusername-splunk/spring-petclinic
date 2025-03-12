package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class ExternalAPI {

	private final RestTemplate restTemplate;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(6);

	@Autowired
	public ExternalAPI(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public CompletableFuture<String> fetchExternalAPI() {
		Random random = new Random();
		int minSleep = 1;
		int maxSleep = 4000;
		double skewFactor = 1.7; // Greater than 1 to create a skewed distribution

		double randomValue = random.nextDouble();
		double skewedValue = Math.pow(randomValue, skewFactor);
		int sleepDuration = (int) (minSleep + (maxSleep - minSleep) * skewedValue);

		String url;
		if (sleepDuration > 3500) {
			url = "http://local-local:30727/check?customernum=123456789000";
			System.out.println("Delaying request for " + sleepDuration + " milliseconds");
			return delayBeforeRequest(() -> {
				try {
					return makeHttpRequest(url);
				}
				catch (Exception e) {
					throw new RuntimeException("Request failed local", e);
				}
			}, sleepDuration);
		}
		else if (sleepDuration <= 650) {
			url = "http://0.0.0.0:30727/test";
			System.out.println("Request to /test");
			return queryCreditCheck(() -> {
				try {
					return makeHttpRequest(url);
				}
				catch (Exception e) {
					throw new RuntimeException("Request failed test", e);
				}
			}, 2);
		}
		else {
			url = "http://0.0.0.0:30727/check?customernum=7064897";
			System.out.println("Fast sleep for " + sleepDuration + " milliseconds");
			return queryCreditCheck(() -> {
				try {
					return makeHttpRequest(url);
				}
				catch (Exception e) {
					throw new RuntimeException("Request failed check", e);
				}
			}, sleepDuration / 4);
		}
	}

	private String makeHttpRequest(String urlString) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
		connection.setConnectTimeout(3000);
		connection.setReadTimeout(3000);
		connection.setRequestMethod("GET");

		int responseCode = connection.getResponseCode();
		if (responseCode != 200) {
			throw new RuntimeException("Failed with HTTP error code : " + responseCode);
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder response = new StringBuilder();
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
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
			System.out.println("Response received: " + response);
		}).exceptionally(ex -> {
			System.err.println("Error fetching API: " + ex.getMessage());
			return null;
		});
	}

}