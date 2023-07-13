//package com.qpf.pethome_server;
//
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.InjectionConfig;
//import com.baomidou.mybatisplus.generator.config.*;
//import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
//import com.baomidou.mybatisplus.generator.config.po.TableInfo;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//
//import java.util.*;
//
///**
// * Created by CDHong on 2018/4/6.
// */
//public class GeneratorCode {
//
//    public static void main(String[] args) throws Exception {
//        //用来获取Mybatis-Plus.properties文件的配置信息
//        ResourceBundle rb = ResourceBundle.getBundle("mybatitsPlus-config"); //不要加后缀
//        AutoGenerator mpg = new AutoGenerator();
//        // 全局配置
//        GlobalConfig gc = new GlobalConfig();
//        gc.setOutputDir(rb.getString("OutputDir"));
//        gc.setFileOverride(true);
//        gc.setActiveRecord(false);// 不开开启 activeRecord 模式
//        gc.setEnableCache(false);// XML 二级缓存
//        gc.setBaseResultMap(true);// XML ResultMap
//        gc.setBaseColumnList(true);// XML columList
//        gc.setAuthor(rb.getString("author"));
//        mpg.setGlobalConfig(gc);
//        // 数据源配置
//        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setDbType(DbType.MYSQL);
//        dsc.setTypeConvert(new MySqlTypeConvert());
//        dsc.setDriverName(rb.getString("jdbc.driver"));
//        dsc.setUsername(rb.getString("jdbc.user"));
//        dsc.setPassword(rb.getString("jdbc.pwd"));
//        dsc.setUrl(rb.getString("jdbc.url"));
//        mpg.setDataSource(dsc);
//        // 策略配置
//        StrategyConfig strategy = new StrategyConfig();
//        strategy.setTablePrefix(new String[] { "t_" });// 此处可以修改为您的表前缀
//        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
//        strategy.setEntityLombokModel(true);// 是否使用lombok
//        strategy.setInclude(new String[]{"t_shop_audit_log"}); // 需要生成的表名
//        // strategy.setSuperEntityClass("com.dcl.basic.domain.BaseDomain");    //实体类的父类
//        // strategy.setSuperServiceClass("com.dcl.basic.service.IBaseService");//Service接口的父接口
//        // strategy.setSuperServiceImplClass("cn.itsource.basic.service.impl.BaseServiceImpl");//Service实现类的父接口
//        // strategy.setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper");
//        mpg.setStrategy(strategy);
//        // 包配置
//        PackageConfig pc = new PackageConfig();
//        pc.setParent(rb.getString("parent"));
//        pc.setController("controller");     //controller包名
//        pc.setService("service");           //service包名
//        pc.setServiceImpl("service.impl");  //service实现类包名
//        pc.setEntity("pojo");               //实体类包名
//        pc.setMapper("mapper");             //Mapper接口包名
//        mpg.setPackageInfo(pc);
//
//        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("parent", pc.getParent());
//                map.put("basic", rb.getString("basic"));
//                this.setMap(map);
//            }
//        };
//
//        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
//
//        String parentPath = "/"+pc.getParent().replaceAll("\\.","/")+"/";
//        System.out.println(parentPath);
//        // 调整 实体类 生成目录演示
//        focList.add(new FileOutConfig("/templates/entity.java.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return rb.getString("OutputDir")+ parentPath+"pojo/" + tableInfo.getEntityName() + ".java";
//            }
//        });
//        //dto
//        focList.add(new FileOutConfig("/templates/dto.java.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return rb.getString("OutputDir")+ parentPath+"dto/" + tableInfo.getEntityName() + "Dto.java";
//            }
//        });
//
//        // 映射文件生成目录
//        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return rb.getString("OutputDirXml")+ parentPath+"mapper/" + tableInfo.getEntityName() + "Mapper.xml";
//            }
//        });
//
//        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);
//
//        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
//        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
//        TemplateConfig tc = new TemplateConfig();
//        tc.setService("/templates/service.java.vm");
//        tc.setServiceImpl("/templates/serviceImpl.java.vm");
//        tc.setEntity("/templates/entity.java.vm");
//        tc.setMapper("/templates/mapper.java.vm");
//        tc.setController("/templates/controller.java.vm");
//        tc.setXml(null);
//        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
//        mpg.setTemplate(tc);
//
//        // 执行生成
//        mpg.execute();
//    }
//
//}