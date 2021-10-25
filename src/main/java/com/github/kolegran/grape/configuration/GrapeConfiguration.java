package com.github.kolegran.grape.configuration;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Paths;

@Configuration
public class GrapeConfiguration {

    @Bean
    public Directory luceneDirectory(@Value("${app.lucene-dir}") String luceneDirectoryPath) throws IOException {
        return FSDirectory.open(Paths.get(luceneDirectoryPath));
    }
}
