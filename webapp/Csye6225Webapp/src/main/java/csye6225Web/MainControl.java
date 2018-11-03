package csye6225Web;
import csye6225Web.daos.AWSRDSImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

//      @Bean
//      public CommandLineRunner setupDefaultUser(UserService service) {
//          return args -> {
//              service.save(new User(
//                      "user", //username
//                      "user", //password
//                      Arrays.asList(new Role("USER"), new Role("ACTUATOR")),//roles
//                      true//Active
//              ));
//          };
//      }
//
//    @Bean
//    public PasswordEncoder getPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(MainControl.class);
    }

    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        AWSRDSImpl awsrds = AWSRDSImpl.getInstance();
        awsrds.setupDatabase();

        SpringApplication.run(MainControl.class, args);
    }


}
