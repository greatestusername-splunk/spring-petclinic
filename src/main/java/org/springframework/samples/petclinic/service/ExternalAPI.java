package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalAPI {

	private final RestTemplate restTemplate;

	@Autowired
	public ExternalAPI(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String fetchExternalAPI() {
		try {
			String url = "http://0.0.0.0:8888/check?customernum=123456789000";
			// String url = "https://www.google.com";
			return restTemplate.getForObject(url, String.class);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to fetch External API", e);
		}
	}

}
