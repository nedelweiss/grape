package com.github.kolegran.spdgoogle;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
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
	public Directory createDirectory() throws IOException {
		String luceneDirectoryPath = System.getenv().getOrDefault("LUCENE_DIR", "/dev/lucene-data");
		return FSDirectory.open(Paths.get(luceneDirectoryPath));
	}
}
