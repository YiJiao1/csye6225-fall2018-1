package csye6225Web.serviceController;


import csye6225Web.models.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.springframework.core.io.support.ResourcePropertySource;
import java.io.IOException;
import java.util.UUID;

@RestController
public class UserController {



    @PostMapping("/user/register")
    public ResponseEntity<Object> createNewTransaction(@RequestHeader(value="username",required = true) String username,
                                                       @RequestHeader(value="password",required = true) String password)
    {

        return ResponseEntity.status(HttpStatus.OK).body("Register success!!");

    }




    @GetMapping("/reset")
    public ResponseEntity<Object> resetPassword(@RequestHeader(value="username",required = true) String username,
                                                @RequestHeader(value="password",required = true) String password)

    {

            ResourcePropertySource propertySource2=null;
            try
            {
                propertySource2 = new ResourcePropertySource("resources", "classpath:application.properties");
            } catch (IOException e) { e.printStackTrace(); }


            String ACCESS_KEY = propertySource2.getProperty("ACCESS_KEY").toString();
            String SECRET_KEY = propertySource2.getProperty("SECRET_KEY").toString();


            AWSCredentials awsCredentials=new BasicAWSCredentials(ACCESS_KEY,SECRET_KEY);
            AmazonSNSClient snsClient= new AmazonSNSClient(awsCredentials);

            String topicARN= "arn:aws:sns:us-east-1:398590284929:Csye6225Topic";
            String msg=username+":"+UUID.randomUUID().toString();

            PublishRequest publishRequest=new PublishRequest(topicARN,msg);

            try
            {
                PublishResult publishResult=snsClient.publish(publishRequest);
                System.out.println(publishResult.toString());
                String returnString="Rest password link sent to "+username;
                return ResponseEntity.status(HttpStatus.OK).body(returnString);
            }
            catch (Exception e)
            {
                System.out.println(e.toString()+" "+e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }



        }

    }





