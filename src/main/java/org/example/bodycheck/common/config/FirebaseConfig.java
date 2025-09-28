package org.example.bodycheck.common.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

	@Value("${firebase.service-account.path}")
	private String firebaseServiceAccountPath;

	@Bean
	public FirebaseApp firebaseApp() throws IOException {
		FileInputStream serviceAccount =
			new FileInputStream(firebaseServiceAccountPath);

		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.fromStream(serviceAccount))
			.build();

		// 이미 초기화된 경우 예외 방지
		if (FirebaseApp.getApps().isEmpty()) {
			return FirebaseApp.initializeApp(options);
		} else {
			return FirebaseApp.getInstance();
		}
	}
}
