package net.thumbtack.school.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HospitalServer {

    public static void main(String[] args) {
        System.out.println("Start application... \n");
        SpringApplication.run(HospitalServer.class, args);
        System.out.println("Stop application... \n");
    }

}

