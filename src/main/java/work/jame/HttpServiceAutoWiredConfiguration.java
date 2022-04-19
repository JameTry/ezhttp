package work.jame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import work.jame.properties.HttpProperties;
import work.jame.service.SendHttpRequestService;
import work.jame.service.HttpService;


/**
 * @author : Jame
 * @date : 2022-04-19 11:47
 **/
@Configuration
@EnableConfigurationProperties(HttpProperties.class)
public class HttpServiceAutoWiredConfiguration {

    @Autowired
    private HttpProperties httpProperties;

    @Bean
    public SendHttpRequestService getGetHttpService() {
        return new SendHttpRequestService(httpProperties);
    }


}
