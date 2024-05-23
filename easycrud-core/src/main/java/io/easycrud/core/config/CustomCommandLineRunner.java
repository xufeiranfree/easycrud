package io.easycrud.core.config;

import io.easycrud.core.constant.CacheConstants;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CustomCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        CacheConstants.triggerStaticInitialization();
    }
}
