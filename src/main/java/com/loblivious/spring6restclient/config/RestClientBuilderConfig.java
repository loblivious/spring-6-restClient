package com.loblivious.spring6restclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientBuilderConfig {

  private final String rootUrl;
  private final String clientRegistrationId;

  public RestClientBuilderConfig(@Value("${beer.host.rootUrl}") String rootUrl,
      @Value("${oauth2.client.registrationId}") String clientRegistrationId) {
    this.rootUrl = rootUrl;
    this.clientRegistrationId = clientRegistrationId;
  }

  @Bean
  public RestClient restClient(RestClient.Builder builder,
      OAuth2AuthorizedClientManager authorizedClientManager) {
    OAuth2ClientHttpRequestInterceptor requestInterceptor =
        new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);

    return builder
        .baseUrl(this.rootUrl)
        .defaultRequest(requestHeadersSpec -> requestHeadersSpec.attributes(
            RequestAttributeClientRegistrationIdResolver.clientRegistrationId(
                this.clientRegistrationId)))
        .requestInterceptor(requestInterceptor)
        .build();
  }
}
