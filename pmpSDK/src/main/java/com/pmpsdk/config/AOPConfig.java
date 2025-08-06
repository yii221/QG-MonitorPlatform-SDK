package com.pmpsdk.config;

import com.pmpsdk.annotation.ThrowSDKException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@ThrowSDKException
@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.pmpsdk")
public class AOPConfig {
}
