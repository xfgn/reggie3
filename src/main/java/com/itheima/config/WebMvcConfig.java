package com.itheima.config;

import com.itheima.common.JacksonObjectMapper;
import com.itheima.common.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录拦截器
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/backend/page/login/login.html")
                .excludePathPatterns("/employee/login")
                .excludePathPatterns("/backend/js/**").excludePathPatterns("/backend/plugins/**").excludePathPatterns("/backend/styles/**")
                .excludePathPatterns("/backend/api/**").excludePathPatterns("/backend/images/**").
                excludePathPatterns("/front/page/login.html")
                .excludePathPatterns("/front/js/**").excludePathPatterns("/front/api/**").excludePathPatterns("/front/fonts/**").excludePathPatterns("/front/styles/**")
                .excludePathPatterns("/user/sendMsg").excludePathPatterns("/user/login");
    }

    /*扩展mvc消息框架的消息转换器
     * 程序启动，该方法即被执行*/
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter converter=new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将java对象转为json
        converter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器追加到mvc框架的转换器集合中，index：0,代表优先使用
        converters.add(0,converter);
    }
}
