package com.simple.weather.api.application.configuration;

import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig
{
	@Bean(name = "httpClient")
	public CloseableHttpClient httpClient() {
		PoolingHttpClientConnectionManager connectionManager = 
			new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(100);
		connectionManager.setDefaultMaxPerRoute(20);
	
		RequestConfig requestConfig = RequestConfig.custom()
			.setConnectTimeout(5, TimeUnit.SECONDS)
			.setConnectionRequestTimeout(5, TimeUnit.SECONDS)
			.setResponseTimeout(5, TimeUnit.SECONDS)
			.build();
		
		return HttpClients.custom()
			.setConnectionManager(connectionManager)
			.setDefaultRequestConfig(requestConfig)
			.build();
	}
}
