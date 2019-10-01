package ru.charity.proj.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * Конфигурация REST клиента
 */
@Configuration
public class RestConfig {

    public static final String UTF_8_CHARSET = "UTF-8";

    @Value("${httpclient.connectTimeout:30000}")
    private int connectTimeout;

    @Value("${httpclient.connectRequestTimeout:30000}")
    private int connectRequestTimeout;

    @Value("${httpclient.httpclientReadTimeout:30000}")
    private int httpclientReadTimeout;

    @Value("${httpclient.bufferRequestBody:false}")
    private boolean bufferRequestBody;

    @Value("${httpclient.connectionTTL:120000}")
    private int connectionTTL;

    @Value("${httpclient.maxPoolSize:1000}")
    private int maxPoolSize;


    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper;
    }

    @Bean
    public RestTemplate getRestTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(closeableHttpClient());
        factory.setConnectionRequestTimeout(connectRequestTimeout);
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(httpclientReadTimeout);
        factory.setBufferRequestBody(bufferRequestBody);

        RestTemplate restTemplate = new RestTemplate(factory);

        MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonMessageConverter.setObjectMapper(objectMapper());

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName(UTF_8_CHARSET)));
        restTemplate.getMessageConverters().add(1, jsonMessageConverter);

        return new RestTemplate(factory);
    }

    private CloseableHttpClient closeableHttpClient() {
        RequestConfig.Builder requestBuilder = RequestConfig.custom();
        requestBuilder = requestBuilder.setConnectTimeout(connectTimeout).setSocketTimeout(connectTimeout);
        HttpClientBuilder result = HttpClients.custom();

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(connectionTTL, TimeUnit.MILLISECONDS);
        connManager.setDefaultMaxPerRoute(maxPoolSize);
        connManager.setMaxTotal(maxPoolSize);
        result.setConnectionManager(connManager)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setConnectionReuseStrategy(new DefaultConnectionReuseStrategy());
        result.setDefaultRequestConfig(requestBuilder.build());
        return result.build();
    }
}
