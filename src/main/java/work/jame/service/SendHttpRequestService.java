package work.jame.service;


import work.jame.properties.HttpProperties;
import work.jame.util.RequestType;
import work.jame.util.Result;
import work.jame.util.StringUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author : Jame
 * @date : 2022-04-19 12:34
 **/
public class SendHttpRequestService {


    private final HttpProperties httpProperties;

    public SendHttpRequestService(HttpProperties httpProperties) {
        this.httpProperties = httpProperties;
    }

    /**
     * 发送get请求
     *
     * @param requestUri
     * @return
     */
    public Result sendGet(String requestUri) {
        return sendGet(requestUri, null);
    }

    /**
     * 发送带参数的get请求
     *
     * @param requestUri
     * @param params
     * @return
     */
    public Result sendGet(String requestUri, Map<String, String> params) {

        HttpURLConnection connection = null;

        Result result = new Result();
        try {
            connection = createHttpURLConnection(requestUri, params);
            connection.setRequestProperty("Content-Type", "text/html;charset=" + httpProperties.getEncoding());
            connection.setRequestMethod(RequestType.GET.getRequestType());

            result = getResult(connection);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSource(connection, null);
        }

        return result;
    }

    /**
     * 发送普通post请求
     *
     * @param requestUri
     * @return
     */
    public Result sendPost(String requestUri) {
        return sendPost(requestUri, null);
    }


    /**
     * 发送带json数据的post请求
     *
     * @param requestUri
     * @param jsonData
     * @return
     */
    public Result sendPost(String requestUri, String jsonData) {
        HttpURLConnection connection = null;
        OutputStream outputStream = null;
        Result result = new Result();
        try {
            connection = createHttpURLConnection(requestUri, null);
            connection.setRequestMethod(RequestType.POST.getRequestType());
            connection.setUseCaches(httpProperties.getUseCache());
            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            if (jsonData != null && jsonData.length() != 0) {
                connection.setRequestProperty("Content-Type", "application/json");
                outputStream = connection.getOutputStream();
                outputStream.write(jsonData.getBytes());
            }
            result = getResult(connection);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSource(connection, outputStream);
        }
        return result;

    }

    /**
     * 读取文件到字节数组
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] readByByteArrayOutputStream(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("file is not exists!");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
        BufferedInputStream bin = null;
        try {
            bin = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            while (bin.read(buffer) > 0) {
                bos.write(buffer);
            }
            return bos.toByteArray();
        } finally {
            if (bin != null) {
                bin.close();
            }
            bos.close();
        }
    }


    /**
     * 发送post请求文件-暂时未实现
     * @param urlString
     * @param file
     * @return
     */
    public Result sendPostFile(String urlString, File file) {
        Result result = new Result();
        StringBuffer sb = new StringBuffer();
        try {
            OutputStream out = null;
            InputStream in = null;
            DataOutputStream outWrite;
            BufferedReader buffReader;
            String line;

            HttpURLConnection conn = createHttpURLConnection(urlString, null);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //二进制数据
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            //缓存
            conn.setUseCaches(false);
            try {
                out = conn.getOutputStream();
                outWrite = new DataOutputStream(out);
                outWrite.write(readByByteArrayOutputStream(file));
                outWrite.flush();
                result.setCode(conn.getResponseCode());
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    in = conn.getInputStream();
                } else {
                    in = conn.getErrorStream();
                }
                buffReader = new BufferedReader(new InputStreamReader(in, httpProperties.getEncoding()));
                while ((line = buffReader.readLine()) != null) {
                    sb.append(line);
                }
                result.setResult(sb.toString());
            } finally {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }


    private Result getResult(HttpURLConnection connection) {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder sb;
        Result result = new Result();
        try {
            result.setCode(connection.getResponseCode());
            inputStream = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, httpProperties.getEncoding()));
            sb = new StringBuilder();
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                sb.append(temp);
                sb.append(System.lineSeparator());
            }
            result.setResult(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSource(inputStream, bufferedReader);
        }
        return result;
    }


    /**
     * 创建连接
     *
     * @param requestUri
     * @return
     */
    private HttpURLConnection createHttpURLConnection(String requestUri, Map<String, String> requestParams) throws UnsupportedEncodingException {
        HttpURLConnection connection = null;
        String stringUrl;


        if (httpProperties.getPort() != null) {
            stringUrl = StringUtil.amendmentUrl("http://" + httpProperties.getPrefixUrl() + ":" + httpProperties.getPort() + "/", requestUri);
        } else {
            stringUrl = StringUtil.amendmentUrl("http://" + httpProperties.getPrefixUrl() + "/", requestUri);
        }
        if (requestParams != null && requestParams.size() != 0) {
            StringBuilder sb = new StringBuilder(stringUrl);
            sb.append("?");
            int i = 0;
            Set<Map.Entry<String, String>> entrySet = requestParams.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                //中文解析问题
                sb.append(URLEncoder.encode(entry.getKey(), httpProperties.getEncoding()))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), httpProperties.getEncoding()));
                if (++i < entrySet.size()) {
                    sb.append("&");
                }
            }
            stringUrl = sb.toString();
            System.out.println(stringUrl);
        }
        try {
            URL url = new URL(stringUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(httpProperties.getConnectionTimeOut());
            connection.setReadTimeout(httpProperties.getReadTimeOut());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (connection == null) {
            throw new RuntimeException("create Connection failed! " + stringUrl);
        }
        return connection;
    }

    private void closeSource(HttpURLConnection conn, OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            conn.disconnect();
        }
    }

    private void closeSource(InputStream inputStream, BufferedReader bufferedReader) {
        try {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
