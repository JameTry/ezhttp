package work.jame.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : Jame
 * @date : 2022-04-19 11:44
 **/
@ConfigurationProperties(prefix = "ezhttp")
public class HttpProperties {
    /**
     * 配置默认的url
     */
    private String prefixUrl;
    private Integer port;
    /**
     * 编码
     */
    private String encoding = "utf-8";
    /**
     * 使用缓存
     */
    private boolean useCache = false;
    /**
     * 最大连接超时时间 单位毫秒
     */
    private int connectionTimeOut = 15000;
    /**
     * 读取从服务器返回最大超时时间 单位毫秒
     */
    private int readTimeOut = 30000;

    public String getPrefixUrl() {
        return prefixUrl;
    }

    public void setPrefixUrl(String prefixUrl) {
        this.prefixUrl = prefixUrl;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public boolean getUseCache() {
        return useCache;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    public int getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(int connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }
}
