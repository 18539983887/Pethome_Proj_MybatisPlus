package com.qpf.basic.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期全局JSON格式化配置
 *  1.基于jackson将Java对象转为json，或者将json转为Java对象
 *  2.将JSON解析为Java对象的过程称为 [从JSON反序列化Java对象]
 *  3.从Java对象生成JSON的过程称为 [序列化Java对象到JSON]
 * @author 狄琛林
 */
@Configuration
public class JacksonConfig {

    public static final String DEFAULT_LOCAL_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_LOCAL_TIME_FORMAT = "HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.build();
        // 把“忽略重复的模块注册”禁用，否则下面的注册不生效
        objectMapper.disable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);
        objectMapper.registerModule(configTimeModule());
        // 重新设置为生效，避免被其他地方覆盖
        objectMapper.enable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);
        return objectMapper;
    }

    private SimpleModule configTimeModule() {
        //定义"类型映射关系"
        SimpleModule simpleModule = new SimpleModule()
                //添加"反序列化时"的映射操作(把"页面数据"->"后台对象")
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_LOCAL_DATE_TIME_FORMAT)))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_LOCAL_DATE_FORMAT)))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_LOCAL_TIME_FORMAT)))
                .addDeserializer(Date.class, new DateDeserializers.DateDeserializer() {
                    @SneakyThrows   //lombok提供的处理异常的注解
                    @Override
                    public Date deserialize(JsonParser jsonParser, DeserializationContext dc) {
                        String text = jsonParser.getText().trim();
                        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
                        return sdf.parse(text);
                    }
                })

                //添加"序列化时"的映射操作(把"后台对象"->"页面数据")
                .addSerializer(BigInteger.class, ToStringSerializer.instance)
                .addSerializer(Long.class, ToStringSerializer.instance)
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_LOCAL_DATE_TIME_FORMAT)))
                .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_LOCAL_DATE_FORMAT)))
                .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_LOCAL_TIME_FORMAT)))
                .addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat(DEFAULT_DATE_FORMAT)));

        //
        return simpleModule;
    }
}