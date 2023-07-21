package com.qpf.basic.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qpf.basic.dto.PointDto;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 百度地图工具类
 */
public class BaiduMapUtils {

    //百度地图开放平台-服务端应用的AK
    private static final String Application_ID = "569d80c893bffab68ecf87e6a4732296";

    public static PointDto getPoint(String address) {
        try {
            URL url = new URL("http://api.map.baidu.com/geocoding/v3/?address=" + address + "&output=json&ak=" + Application_ID);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream urlStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String str = buffer.toString();
            /**
             * {
             *     "status": 0,
             *     "result": {
             *       "location": {
             *         "lng": 116.30762232672,
             *         "lat": 40.056828485961
             *       },
             *       "precise": 1,
             *       "confidence": 80,
             *       "comprehension": 100,
             *       "level": "门址"
             *     }
             *   }
             */
            JSONObject resultObj = JSON.parseObject(str).getJSONObject("result").getJSONObject("location");
            Double lng = resultObj.getDouble("lng");
            Double lat = resultObj.getDouble("lat");

            PointDto point = new PointDto();
            point.setLng(lng);
            point.setLat(lat);
            System.out.println(point);
            return point;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //地球半径,进行经纬度运算需要用到的数据之一
    private static final double EARTH_RADIUS = 6378137;

    //根据坐标点获取弧度
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     * @param point1 A点坐标
     * @param point2 B点坐标
     * @return
     */
    public static double getDistance(PointDto point1, PointDto point2) {
        double radLat1 = rad(point1.getLat());
        double radLat2 = rad(point2.getLat());
        double a = radLat1 - radLat2;
        double b = rad(point1.getLng()) - rad(point2.getLng());
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;

    }

    public static void main(String[] args) {
        getPoint("成都市天府广场");
    }
}
