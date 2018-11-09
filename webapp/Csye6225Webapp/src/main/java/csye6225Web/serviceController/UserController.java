package csye6225Web.serviceController;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {



    @GetMapping("/ResetPassword")
    public ResponseEntity<Object> resetPassword(@RequestHeader(value="username",required = true) String username,
                                                @RequestHeader(value="password",required = true) String password)

    {

                    String userData=username+" "+password;
                    return ResponseEntity.status(HttpStatus.OK).body(userData);


    }




}
