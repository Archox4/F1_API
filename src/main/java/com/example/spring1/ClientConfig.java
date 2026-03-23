package com.example.spring1;

import com.example.spring1.clients.RaceDataClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {

    @Bean
    public RestClient.Builder restClientBuilder(){
        return RestClient.builder();
    }

    private <T> T createClient(RestClient.Builder builder, Class<T> clientClass) {
        RestClient restClient = builder.baseUrl("https://api.openf1.org/v1/").build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();
        return factory.createClient(clientClass);
    }

    @Bean
    public RaceDataClients.MeetingClient meetingClient(RestClient.Builder builder) {
        return createClient(builder, RaceDataClients.MeetingClient.class);
    }

    @Bean
    public RaceDataClients.SessionClient sessionClient(RestClient.Builder builder) {
        return createClient(builder, RaceDataClients.SessionClient.class);
    }

    @Bean
    public RaceDataClients.LapClient lapClient(RestClient.Builder builder) {
        return createClient(builder, RaceDataClients.LapClient.class);
    }

    @Bean
    public RaceDataClients.DriverClient driverClient(RestClient.Builder builder) {
        return createClient(builder, RaceDataClients.DriverClient.class);
    }

    @Bean
    public RaceDataClients.SessionResultClient sessionResultClient(RestClient.Builder builder) {
        return createClient(builder, RaceDataClients.SessionResultClient.class);
    }

}