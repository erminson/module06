package ru.erminson.lc.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class SqlFactory {
    private final ResourceLoader resourceLoader;

    @Autowired
    public SqlFactory(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String getSqlQuery(String fileName) {
        Resource resource = resourceLoader.getResource("classpath:queries/" + fileName);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Sql file:" + fileName + "not found");
        }
    }
}
