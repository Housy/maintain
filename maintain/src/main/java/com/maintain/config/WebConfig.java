package com.maintain.config;


import com.maintain.filter.AuthFilter;
import com.maintain.filter.GlobalFilter;
import com.maintain.interceptor.GlobalInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig  implements WebMvcConfigurer {

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/zxc/foo").setViewName("foo");
//    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GlobalInterceptor())
                .addPathPatterns("/**");
    }
    @Bean
    public FilterRegistrationBean globalFilterRegist() {
        FilterRegistrationBean frBean = new FilterRegistrationBean();
        frBean.setFilter(new GlobalFilter());
        frBean.addUrlPatterns("/*");
        return frBean;
    }

    @Bean
    public FilterRegistrationBean authFilterRegist() {
        FilterRegistrationBean frBean = new FilterRegistrationBean();
        frBean.setFilter(new AuthFilter());
        frBean.addUrlPatterns("*.html");
        return frBean;
    }

//    @Bean
//    public ServletListenerRegistrationBean listenerRegist() {
//        ServletListenerRegistrationBean srb = new ServletListenerRegistrationBean();
//        srb.setListener(new MyHttpSessionListener());
//        System.out.println("listener");
//        return srb;
//    }

}
