package net.cnki.odatax.core;

import net.cnki.odatax.exception.SystemException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author hudianwei
 * @des Http工具
 * @date 2018/8/2 15:26
 */
public class HttpUtil {
    public static final String REQ_METHOD_POST = "POST";

    public static final String REQ_METHOD_GET = "GET";

    public static String encodeURI(String url) throws UnsupportedEncodingException {
        if (DataUtil.isBlank(url)) {
            return "";
        } else {
            return URLEncoder.encode(url, "utf-8");
        }
    }

    public static String decodeURI(String url) throws UnsupportedEncodingException {
        if (DataUtil.isBlank(url)) {
            return "";
        } else {
            return URLDecoder.decode(url, "utf-8");
        }
    }

    public static String doHttpAndHttps(String url, String requestMessage, String method) throws Exception {
        HttpURLConnection urlConnection = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            switch (method) {
                case HttpUtil.REQ_METHOD_GET:
                    url = url + "?" + requestMessage;
                    urlConnection = openUrlConn(url, method);
                    break;
                case HttpUtil.REQ_METHOD_POST:
                    urlConnection = openUrlConn(url, method);
                    byte[] b = requestMessage.getBytes();
                    out.write(b);
                    out.flush();
                    break;
                default:
                    break;
            }
            in = urlConnection.getInputStream();
            String res = getContents(in);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                } finally {
                    out = null;
                }
            }
            if (in != null) {
                try {

                } catch (Exception e) {
                } finally {
                    in = null;
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
                urlConnection = null;
            }
        }
    }

    private static HttpURLConnection openUrlConn(String url, String method) throws Exception {
        URL httpurl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) httpurl.openConnection();
        try {
            connection.setRequestMethod(method);
            connection.setConnectTimeout(60 * 10 * 1000);
            connection.setReadTimeout(60 * 10 * 1000);
            if (HttpUtil.REQ_METHOD_POST.equals(method)) {
                connection.setDoOutput(true);
                connection.setDoInput(true);
            }
            connection.setUseCaches(false);
            return connection;
        } catch (Exception e) {
            throw new Exception("对方URL无法连接" + url, e);

        } finally {
            connection.disconnect();
            connection = null;
        }
    }

    private static String getContents(InputStream in) {
        BufferedReader bre = null;
        StringBuffer sb = new StringBuffer();
        String contents = "";
        try {
            bre = new BufferedReader(new InputStreamReader(in));
            while ((contents = bre.readLine()) != null) {
                sb.append(contents);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bre != null) {
                    bre.close();
                }
            } catch (Exception e2) {
                throw new SystemException(e2);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                throw new SystemException(e2);
            }
        }
        return sb.toString();
    }
}
