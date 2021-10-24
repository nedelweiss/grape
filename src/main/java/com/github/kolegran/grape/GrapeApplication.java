package com.github.kolegran.grape;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.Paths;

@SpringBootApplication
public class GrapeApplication {
	public static void main(String[] args) {
		SpringApplication.run(GrapeApplication.class, args);
	}

	@Bean
	public Directory luceneDirectory(@Value("${app.lucene-dir}") String luceneDirectoryPath) throws IOException {
		return FSDirectory.open(Paths.get(luceneDirectoryPath));
	}
}
