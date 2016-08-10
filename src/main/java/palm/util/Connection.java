package palm.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Connection {
    //设置请求和传输超时时间
    public final static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000).build();
    public static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

    public static String get(String url) {
        return getDeleteOrGetContent(new HttpGet(url), null);
    }

    public static String get(String url, Map<String,String> headers) {
        return getDeleteOrGetContent(new HttpGet(url),headers);
    }

    public static String post(String url, String json) {
        return getPostOrPutContent(new HttpPost(url), json);
    }

    public static String post(String url, Map<String,String> formData,Map<String,String> headers) {
        return postData( new HttpPost(url), headers,formData);
    }

    private static String getDeleteOrGetContent(HttpRequestBase httpRequest, Map<String, String> headers) {
        if (null == httpRequest) {
            return null;
        }
        if (null != headers) {
            for(Map.Entry<String,String> entry:headers.entrySet()){
                Header header = new BasicHeader(entry.getKey(),entry.getValue());
                httpRequest.addHeader(header);
            }
        }else {
            Header header = new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
            httpRequest.addHeader(header);
        }
        httpRequest.setConfig(requestConfig);
//        HttpHost proxy = new HttpHost("123.5.57.136", 9999);
//        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
//            CloseableHttpClient httpclient = HttpClients.custom()
//                    .setRoutePlanner(routePlanner)
//                    .build();

//        CredentialsProvider credsProvider = new BasicCredentialsProvider();
//        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("spider_group", "pGRyP.aG");
//        credsProvider.setCredentials(
//                new AuthScope("10.14.41.101", 8081),
//                creds);
        cm.setMaxTotal(800);//连接池最大并发连接数
        cm.setDefaultMaxPerRoute(400);//单路由最大并发数
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
//                .setRoutePlanner(routePlanner)
                .setConnectionManager(cm)
                //.setDefaultCredentialsProvider(credsProvider)
                .build();
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            Thread.sleep(1000);
            response = httpclient.execute(httpRequest);
            if (null == response) {
                response = httpclient.execute(httpRequest);
            }
            if (null == response){
                response = httpclient.execute(httpRequest);
            }
            entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                System.out.println(e1);
            }
            System.out.println(e);
            return null;
        } finally {
            if(entity!=null) {
                try {
                    EntityUtils.consume(entity);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            httpRequest.releaseConnection();
        }
    }

    private static String postData(HttpPost httpPost,
                                   Map<String,String> headers,Map<String,String> formData){
        if (null == httpPost)
            return null;
        if(null!=headers){
            for(Map.Entry<String,String> entry:headers.entrySet()){
                Header header = new BasicHeader(entry.getKey(),entry.getValue());
                httpPost.addHeader(header);
            }
        }else {
            Header header = new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
            httpPost.addHeader(header);
        }
        if (null!=formData){
            List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
            for(Map.Entry<String,String> entry:formData.entrySet()){
                nvps.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
            }
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            } catch (UnsupportedEncodingException e) {
                System.out.println(e);
            }
        }
        httpPost.setConfig(requestConfig);
        HttpHost proxy = new HttpHost("172.16.58.188", 9999);

        cm.setMaxTotal(800);//连接池最大并发连接数
        cm.setDefaultMaxPerRoute(400);//单路由最大并发数
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setRoutePlanner(routePlanner).setConnectionManager(cm)
                .build();
//        CredentialsProvider credsProvider = new BasicCredentialsProvider();
//        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("spider_group", "pGRyP.aG");
//        credsProvider.setCredentials(
//                new AuthScope("10.14.41.101", 8081),
//                creds);
//
//        CloseableHttpClient httpclient = HttpClients.custom()
//                .setRoutePlanner(routePlanner)
//                .setDefaultCredentialsProvider(credsProvider)
//                .build();
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {

            response = httpclient.execute(httpPost);
//            Header[] headerses = response.getAllHeaders();
//            System.out.println(response.containsHeader("Content-Encoding"));
//            for (Header header:headerses){
//                System.out.println(header.getName()+":"+header.getValue());
//            }
            if (null == response) {
                response = httpclient.execute(httpPost);
            }
            if (null == response){
                response = httpclient.execute(httpPost);
            }
            entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            if(entity != null) {
                try {
                    EntityUtils.consume(entity);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            httpPost.releaseConnection();
        }
    }

    private static String getPostOrPutContent( HttpEntityEnclosingRequestBase requestBase, String json) {
        if (null == requestBase) {
            return null;
        }
        HttpHost proxy = new HttpHost("172.16.58.188", 9999);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
//        CredentialsProvider credsProvider = new BasicCredentialsProvider();
//        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("spider_group", "pGRyP.aG");
//        credsProvider.setCredentials(
//                new AuthScope("10.14.41.101", 8081),
//                creds);

        cm.setMaxTotal(800);//连接池最大并发连接数
        cm.setDefaultMaxPerRoute(400);//单路由最大并发数

        CloseableHttpClient client = HttpClients.custom()
                //.setDefaultCredentialsProvider(credsProvider)
                .setRoutePlanner(routePlanner).setConnectionManager(cm)
                .build();

        requestBase.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        HttpEntity resEntity = null;
        try {
            StringEntity entity = new StringEntity(json, "UTF-8");
            entity.setContentType("application/json");
            requestBase.setEntity(entity);
            try {
                response = client.execute(requestBase);
                if (null == response) {
                    response = client.execute(requestBase);
                }if (null == response) {
                    response = client.execute(requestBase);
                }
                resEntity = response.getEntity();
            } catch (SocketTimeoutException e) {
                System.out.println(e);
                // DeepLogger.logger.debug("接口访问超时，休眠2s再次访问"+url+":"+json);
                Thread.sleep(2000);
                response = client.execute(requestBase);
                resEntity = response.getEntity();
            }
            return EntityUtils.toString(resEntity, "UTF-8");
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if(resEntity != null) {
                try {
                    EntityUtils.consume(resEntity);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            requestBase.releaseConnection();
        }
        return null;
    }

    public static void main(String[] args) {
//        System.out.println(Connection.get("https://www.google.com/search?async=_id:rg_s,_pms:s&q=70+years+old+face&start=0&asearch=ichunk&tbm=isch&ijn=0"));
        System.out.println(Connection.get("https://encrypted-tbn0.gstatic.com//images?q=tbn:ANd9GcTO8zVdwLBYWUXxsfadbUWcFkSGi6Ufqya0Z-Qa-5ScK8d3j4T0"));
    }
}
