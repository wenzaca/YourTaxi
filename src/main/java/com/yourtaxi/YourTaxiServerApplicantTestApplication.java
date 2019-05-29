package com.yourtaxi;

import com.yourtaxi.util.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@EnableCircuitBreaker
@EnableEurekaServer
public class YourTaxiServerApplicantTestApplication extends WebMvcConfigurerAdapter
{
    @Value("${swagger.title}")
    private String title;

    @Value("${swagger.description}")
    private String description;

    @Value("${swagger.contact.name}")
    private String contactName;

    @Value("${swagger.contact.url}")
    private String contactURL;

    @Value("${swagger.contact.email}")
    private String contactEmail;

    @Value("${swagger.version}")
    private String version;

    @Value("${swagger.license.url}")
    private String licenseUrl;

    @Value("${swagger.license.termsUrl}")
    private String licenseTermsUrl;

    @Value("${swagger.license.provider}")
    private String licenseProvider;


    public static void main(String[] args)
    {
        SpringApplication.run(YourTaxiServerApplicantTestApplication.class, args);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new LoggingInterceptor()).addPathPatterns("/**");
    }


    @Bean
    public Docket docket()
    {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage(getClass().getPackage().getName()))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(generateApiInfo());
    }


    private ApiInfo generateApiInfo()
    {

        return new ApiInfoBuilder().title(title)
            .description(description)
            .contact(new Contact(contactName, contactURL, contactEmail))
            .license(licenseProvider).licenseUrl(licenseUrl).termsOfServiceUrl(licenseTermsUrl)
            .version(version)
            .build();
    }
}
