package com.yz.core.util.score;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;

/**
 * @描述: ${DESCRIPTION}
 * @作者: DuKai
 * @创建时间: 2017/11/23 19:47
 * @版本号: V1.0
 */
public class ScoreHttpClientUtil {

    private static HttpClientContext context = HttpClientContext.create();
    private static RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(120000)
            .setSocketTimeout(60000).setConnectionRequestTimeout(60000).setCookieSpec(CookieSpecs.STANDARD_STRICT)
            .setExpectContinueEnabled(true)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();

    private static CloseableHttpClient createHttpClient(CookieStore cookieStore) {
        if (cookieStore == null) {
            cookieStore = new BasicCookieStore();
        }

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultCookieStore(cookieStore)
                .setDefaultRequestConfig(requestConfig).build();

        return httpClient;
    }

    private static CloseableHttpClient createHttpClient() {
        return createHttpClient(null);
    }


    /**
     * 获取考试分数信息
     * @param ksh
     * @param csrq
     * @return
     */
    public static String getScoreInfo(String ksh, String csrq, String issueDate) {
        CloseableHttpClient httpClient = createHttpClient();
        try {
            //获取图片验证码
            HttpGet httpGetVali = new HttpGet(ScoreOnlineConstants.VALI_CODE);
            httpGetVali.addHeader("Accept", "image/webp,image/*,*/*;q=0.8");
            httpGetVali.addHeader("Host", "query-score.5184.com");
            httpGetVali.addHeader("Referer", ScoreOnlineConstants.SCORE_PAGE_URL);
            httpGetVali.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            CloseableHttpResponse responseVali = httpClient.execute(httpGetVali);
            byte[] imgByte = EntityUtils.toByteArray(responseVali.getEntity());
            DiscernUtil.init(null);//TODO 去掉
            String valiCode = DiscernUtil.getCode(imgByte);
            if(valiCode == null){
                return null;
            }

            Header[] header = responseVali.getAllHeaders();
            String cookies = "";
            for (int i=0;i<header.length;i++) {
                System.out.println(header[i].getName()+"=========="+header[i].getValue());
                if("Set-Cookie".equals(header[i].getName())){
                    cookies += header[i].getValue().split(";")[0]+";";
                }
            }

            cookies = cookies.substring(0,cookies.length()-1);

            String url = ScoreOnlineConstants.SCORE_RESULT + "?issue_date="+issueDate+"&data_type=ck_cj&verify_code="+valiCode+"&ksh="+ksh+"&csrq="+csrq;
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Host", "query-score.5184.com");
            httpGet.addHeader("Cookie",cookies);
            httpGet.addHeader("Referer",ScoreOnlineConstants.SCORE_PAGE_URL);
            HttpResponse response = httpClient.execute(httpGet, context);
            return toString(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 直接把Response内的Entity内容转换成String
     *
     * @param httpResponse
     * @return
     */
    private static String toString(HttpResponse httpResponse) {
        // 获取响应消息实体
        String result = null;
        try {
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ((CloseableHttpResponse) httpResponse).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


}
