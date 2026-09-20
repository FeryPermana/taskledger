package com.ferry.taskledger.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "password123";

        String hash = encoder.encode(password);

        System.out.println("====================================");
        System.out.println("PASSWORD : " + password);
        System.out.println("HASH     : " + hash);
        System.out.println("====================================");
    }
}
// .\mvnw.cmd dependency:build-classpath -Dmdep.outputFile=cp.txt
// java -cp "target\classes;$(Get-Content cp.txt)" com.ferry.taskledger.config.PasswordHashGenerator