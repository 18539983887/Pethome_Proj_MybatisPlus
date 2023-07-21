package com.qpf.basic.utils;

import com.qpf.basic.dto.PointDto;
import com.qpf.org.pojo.Shop;
import java.util.List;

public class ShopNearestUtils {

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     *
     * @param point 用户指定的地址坐标
     * @param shops 商店
     * @return
     */
    public static Shop getNearestShop(PointDto point, List<Shop> shops) {

        //如果传过来的集合只有一家店铺,那么直接将这家店铺的信息返回就是最近的店铺了
        Shop shop = shops.get(0);
        //获取集合中第一家店铺到指定地点的距离
        double distance = BaiduMapUtils.getDistance(point, BaiduMapUtils.getPoint(shops.get(0).getAddress()));
        double minDistance = distance;
        //如果有多家店铺,那么就和第一家店铺到指定地点的距离做比较
        if (shops.size() > 1) {
            for (int i = 1; i < shops.size(); i++) {
                double distanceTmp = BaiduMapUtils.getDistance(point, BaiduMapUtils.getPoint(shops.get(i).getAddress()));
                if (distanceTmp < distance) {
                    shop = shops.get(i);
                    minDistance = distanceTmp;
                }
            }
        }

        if (minDistance > 50000) { //大于50公里，没有合适的店铺
            return null;
        }
        return shop;
    }
}