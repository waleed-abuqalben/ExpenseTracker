package com.waleed.expenseTracker.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class General {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
