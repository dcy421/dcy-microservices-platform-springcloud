//package com.dcy.config;
//
//import feign.Response;
//import feign.Util;
//import feign.codec.ErrorDecoder;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.IOException;
//
//import static feign.FeignException.errorStatus;
//
///**
// * @Author：dcy
// * @Description:
// * @Date: 2020/3/3 10:35
// */
//@Configuration
//@Slf4j
//public class FeignClientErrorDecoder implements ErrorDecoder {
//
//    @Override
//    public Exception decode(String methodKey, Response response) {
//        log.info("feign client response:", response);
//        String body = null;
//        try {
//            body = Util.toString(response.body().asReader());
//        } catch (IOException e) {
//            log.error("feign.IOException", e);
//        }
//        if (response.status() >= 400 && response.status() <= 500) {
//            throw new RuntimeException();
//        }
//        return errorStatus(methodKey, response);
//    }
//}
