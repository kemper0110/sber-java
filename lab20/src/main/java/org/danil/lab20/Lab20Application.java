package org.danil.lab20;

import org.danil.lab20.fetcher.CachedFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Lab20Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab20Application.class, args);
    }

    @Bean
    public FilterRegistrationBean<CachedFilter> loggingFilter() {
        FilterRegistrationBean<CachedFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new CachedFilter());
        registrationBean.addUrlPatterns("/fetch");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
