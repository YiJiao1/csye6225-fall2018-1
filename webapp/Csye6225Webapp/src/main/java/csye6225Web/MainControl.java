package csye6225Web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import csye6225Web.models.User;
//import csye6225Web.models.Role;
//import csye6225Web.services.UserService;
//import java.util.Arrays;

@SpringBootApplication
public class MainControl extends SpringBootServletInitializer{


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {

        return builder.sources(MainControl.class);
    }



    public static void main(String[] args)
    {

        SpringApplication.run(MainControl.class,args);
    }


}
