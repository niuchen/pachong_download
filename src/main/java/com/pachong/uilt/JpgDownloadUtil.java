package com.pachong.uilt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import com.pachong.Headerpachong;
import com.pachong.HttpClientUtil;
import com.pachong.initAPI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

public class JpgDownloadUtil {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(JpgDownloadUtil.class);

    public static boolean download(String url, String filePathName) {
        boolean isranet=false;
        CloseableHttpClient httpclient = HttpClientUtil.getHttpClient();
        CloseableHttpResponse response =null;
        try {
            HttpGet httpget =   Headerpachong.newimgerURL(url);
              httpget = new HttpGet(url);

            // 伪装成google的爬虫JAVA问题查询
            httpget.setHeader("User-Agent",
                    "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
// Execute HTTP request  
        //    System.out.println("executing request " + httpget.getURI());
              response = httpclient.execute(httpget);

            File storeFile = new File(filePathName);
            FileOutputStream output = new FileOutputStream(storeFile);

// 得到网络资源的字节数组,并写入文件  
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    byte b[] = new byte[1024];
                    int j = 0;
                    while( (j = instream.read(b))!=-1){
                        output.write(b,0,j);
                    }
                    output.flush();
                    output.close();
                    System.out.println("下载过完了 " + httpget.getURI());
                    isranet=true;
                } catch (IOException ex) {
// In case of an IOException the connection will be released  
// back to the connection manager automatically  
                    throw ex;
                } catch (RuntimeException ex) {
// In case of an unexpected exception you may want to abort  
// the HTTP request in order to shut down the underlying  
// connection immediately.  
                    httpget.abort();
                    throw ex;
                } finally {
// Closing the input stream will trigger connection release  
                    try { instream.close(); } catch (Exception ignore) {}
                }
            }

        } catch (Exception e) {
            logger.error("异常了:"+e.getMessage(), e);
        } finally {
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isranet;
    }

    public static void main(String[] args) throws MalformedURLException {

//抓取下面图片的测试  
        // 抓取下面图片的测试
        JpgDownloadUtil.download(
                "https://s3.amazonaws.com/rap.certs/Gia/Certificates/2017/6/13/1265088036.pdf",
                initAPI.filepath+"/222.pdf");
    }
}  