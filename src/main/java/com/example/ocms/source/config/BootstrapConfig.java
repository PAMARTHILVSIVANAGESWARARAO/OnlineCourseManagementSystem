package com.example.ocms.source.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.ocms.source.model.User;
import com.example.ocms.source.repository.UserRepository;

@Configuration
public class BootstrapConfig implements CommandLineRunner {

    @Autowired
    private UserRepository users;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        if (users.findByEmail("admin@ocms.com").isEmpty()) {

            users.save(new User("admin@ocms.com", encoder.encode("password"), "ADMIN", true));
            System.out.println("+-------------------------------------------------------------+");
            System.out.println("Admin Inserted SuccessFully");
            System.out.println("+-------------------------------------------------------------+");

        }
        else{
            System.out.println("+-------------------------------------------------------------+");
            System.out.println("Admin already in database");
            System.out.println("+-------------------------------------------------------------+");

        }
    }
}
