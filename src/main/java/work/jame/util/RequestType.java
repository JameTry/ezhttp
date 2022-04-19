package work.jame.util;

/**
 * @author : Jame
 * @date : 2022-04-19 11:54
 **/
public enum RequestType {
    GET("GET"),POST("POST");
    private  String requestType;

    RequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
