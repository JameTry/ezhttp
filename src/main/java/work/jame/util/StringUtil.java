package work.jame.util;

/**
 * @author : Jame
 * @date : 2022-04-19 20:42
 * @description :
 **/
public class StringUtil {


    /**
     * 修正路径
     *
     * @param prefixUrl
     * @param requestPath
     * @return
     */
    public static String amendmentUrl(String prefixUrl, String requestPath) {
        if (!requestPath.startsWith("/")) {
            return prefixUrl + "/" + requestPath;
        }
        return prefixUrl + requestPath;
    }

}
