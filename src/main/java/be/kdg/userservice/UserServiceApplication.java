package be.kdg.userservice;

import be.kdg.userservice.boot.CreateDefaultUserListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(UserServiceApplication.class, args);
        ctx.getBean(CreateDefaultUserListener.class);
    }

}

