package com.cxy;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(com.cxy.PredictProperties.class)
public class PredictConfiguration {

    @Bean
    @ConditionalOnMissingBean(Predict.class)
    public BayesPredictImpl predict(PredictProperties predictProperties){
        return new BayesPredictImpl(predictProperties);
    }
}
