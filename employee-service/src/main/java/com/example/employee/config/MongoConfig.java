package com.example.employee.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.employee.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri:mongodb://localhost:27017/employee_db}")
    private String mongoUri;

    @Override
    protected String getDatabaseName() {
        return "employee_db";
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        boolean isSsl = mongoUri.startsWith("mongodb+srv://") || Boolean.TRUE.equals(connectionString.getSslEnabled());

        if (isSsl) {
            try {
                TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
                };

                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());

                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .applyToSslSettings(builder -> {
                            builder.enabled(true);
                            builder.invalidHostNameAllowed(true);
                            builder.context(sc);
                        })
                        .build();

                return MongoClients.create(settings);
            } catch (Exception e) {
                return MongoClients.create(connectionString);
            }
        } else {
            return MongoClients.create(connectionString);
        }
    }
}
