package com.yz.core.util.score;

/**
 * @描述: 查询考试成绩
 * @作者: DuKai
 * @创建时间: 2017/11/23 19:43
 * @版本号: V1.0
 */
public class QueryScoreTest {

    public static void main(String[] args) {
        String result = ScoreHttpClientUtil.getScoreInfo("130105621","9106","20161100");
        System.out.println(result);
    }

}
