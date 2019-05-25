package com.github.kolegran.spdgoogle;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.Paths;

@SpringBootApplication
public class SpdGoogleApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpdGoogleApplication.class, args);
	}

	@Bean
	public Directory luceneDirectory(@Value("${app.lucene-dir}") String luceneDirectoryPath) throws IOException {
		return FSDirectory.open(Paths.get(luceneDirectoryPath));
	}
}
