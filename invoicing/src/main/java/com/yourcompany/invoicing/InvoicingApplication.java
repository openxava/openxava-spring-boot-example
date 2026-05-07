package com.yourcompany.invoicing;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.openxava.util.DBServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.openxava.naviox.web.NaviOXFilter;

@SpringBootApplication
@ServletComponentScan(basePackages = { "org.openxava", "com.openxava" })
public class InvoicingApplication extends SpringBootServletInitializer implements WebMvcConfigurer {

    public static void main(String[] args) throws Exception {
        DBServer.start("invoicing-db");
        SpringApplication.run(InvoicingApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(InvoicingApplication.class);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.jsp");
    }

    @Bean
    public FilterRegistrationBean<NaviOXFilter> naviOXFilter() {
        FilterRegistrationBean<NaviOXFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new NaviOXFilter());
        registration.setName("naviox");
        registration.addUrlPatterns("*.jsp", "/modules/*", "/phone/index.jsp", "/m/*");
        registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
        return registration;
    }

    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        return new TomcatServletWebServerFactory() {

            @Override
            protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
                tomcat.enableNaming();
                return super.getTomcatWebServer(tomcat);
            }

            @Override
            protected void postProcessContext(Context context) {
                ContextResource resource = new ContextResource();
                resource.setName("jdbc/invoicingDS");
                resource.setType("javax.sql.DataSource");
                resource.setProperty("driverClassName", "org.hsqldb.jdbc.JDBCDriver");
                resource.setProperty("url", "jdbc:hsqldb:hsql://localhost:1666");
                resource.setProperty("username", "sa");
                resource.setProperty("password", "");
                resource.setProperty("maxTotal", "20");
                resource.setProperty("maxIdle", "5");
                resource.setProperty("maxWaitMillis", "10000");
                context.getNamingResources().addResource(resource);
            }
        };
    }

}