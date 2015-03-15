package gamecenter.core.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStreamReader;


public class HttpUtil {
    public static JSONObject getJson(String url) {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        JSONObject json = null;
        try {
            HttpResponse res = client.execute(get);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                json = new JSONObject(new JSONTokener(new InputStreamReader(entity.getContent(), HTTP.UTF_8)));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);

        } finally {
            //关闭连接 ,释放资源  
            client.getConnectionManager().shutdown();
        }
        return json;
    }

}
