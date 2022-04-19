package work.jame.service;

import org.springframework.beans.factory.annotation.Autowired;
import work.jame.properties.HttpProperties;
import work.jame.util.RequestType;
import work.jame.util.Result;


/**
 * @author : Jame
 * @date : 2022-04-19 11:46
 **/
public  interface HttpService {




     Result send(String requestUri);



}
