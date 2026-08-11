package com.example.employee;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.List;

public class SeedDataTest {

    @Test
    void seedUsers() {
        String uri = "mongodb+srv://sivamurthi:sivamurthi@cluster0.cqmgg.mongodb.net/employee_db?retryWrites=true&w=majority&tls=true&tlsAllowInvalidHostnames=true";
        System.out.println("Connecting to MongoDB Atlas at: " + uri);

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("employee_db");
            MongoCollection<Document> usersCollection = database.getCollection("users");

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            // Check admin
            if (usersCollection.countDocuments(new Document("username", "admin")) == 0) {
                Document admin = new Document()
                        .append("username", "admin")
                        .append("email", "admin@example.com")
                        .append("password", encoder.encode("admin123"))
                        .append("role", "ROLE_ADMIN")
                        .append("createdAt", new Date())
                        .append("_class", "com.example.employee.entity.User");
                usersCollection.insertOne(admin);
                System.out.println("✅ Inserted admin user into MongoDB Atlas!");
            } else {
                System.out.println("ℹ️ Admin user already exists in MongoDB Atlas.");
            }

            // Check user
            if (usersCollection.countDocuments(new Document("username", "user")) == 0) {
                Document user = new Document()
                        .append("username", "user")
                        .append("email", "user@example.com")
                        .append("password", encoder.encode("user123"))
                        .append("role", "ROLE_USER")
                        .append("createdAt", new Date())
                        .append("_class", "com.example.employee.entity.User");
                usersCollection.insertOne(user);
                System.out.println("✅ Inserted standard user into MongoDB Atlas!");
            } else {
                System.out.println("ℹ️ Standard user already exists in MongoDB Atlas.");
            }

            // Check department
            MongoCollection<Document> deptCol = database.getCollection("departments");
            if (deptCol.countDocuments() == 0) {
                Document dept = new Document()
                        .append("name", "Engineering")
                        .append("code", "ENG")
                        .append("location", "Floor 3")
                        .append("_class", "com.example.employee.entity.Department");
                deptCol.insertOne(dept);
                System.out.println("✅ Inserted Engineering department!");
            }

            // Check designation
            MongoCollection<Document> desigCol = database.getCollection("designations");
            if (desigCol.countDocuments() == 0) {
                Document desig = new Document()
                        .append("title", "Software Engineer")
                        .append("code", "SE")
                        .append("_class", "com.example.employee.entity.Designation");
                desigCol.insertOne(desig);
                System.out.println("✅ Inserted Software Engineer designation!");
            }

            // Check city
            MongoCollection<Document> cityCol = database.getCollection("cities");
            if (cityCol.countDocuments() == 0) {
                Document city = new Document()
                        .append("name", "Bangalore")
                        .append("state", "Karnataka")
                        .append("country", "India")
                        .append("_class", "com.example.employee.entity.City");
                cityCol.insertOne(city);
                System.out.println("✅ Inserted Bangalore city!");
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to connect to MongoDB Atlas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
