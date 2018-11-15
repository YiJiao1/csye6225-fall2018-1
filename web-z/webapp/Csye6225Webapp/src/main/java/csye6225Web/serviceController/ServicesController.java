package csye6225Web.serviceController;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSAsyncClient;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import csye6225Web.daos.UserImpl;
import csye6225Web.models.User;
import csye6225Web.repositories.UserRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;




@Controller
public class ServicesController {




    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/time", method = RequestMethod.GET, produces = "application/json")//communication in json
    @ResponseBody
    public String getCurrentTime(@RequestHeader(value="username", required = true) String username,
                                 @RequestHeader(value="password", required = true) String password) {

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return "Missing username or password, please verify.";
        }
        JSONObject obj = new JSONObject();

        System.out.println(username + password);
        Iterable users = userRepository.findAll();
        System.out.checkError();
        for (User a:userRepository.findAll()) {
            if (!a.getUsername().equals(username)) {
                continue;
            } else if (BCrypt.checkpw(password, a.getPassword())) {
                Date date= new Date();
                SimpleDateFormat datetimeFormat=new SimpleDateFormat();
                String currenttime=datetimeFormat.format(date);
                try {
                    obj.put("Currenttime: ", currenttime);
                    obj.put("id", "2");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                return "Incorrect password !!\n";
            }
            return obj.toString();
        }
        return "User name does not exist!\n";


    }


    private boolean checkPass(String plainPassword, String hashedPassword) {
        if (BCrypt.checkpw(plainPassword, hashedPassword)) return true;
        else return false;
    }




    @RequestMapping(value = "/user/register", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String userRegister(@RequestHeader(value="username", required = true) String username,
                               @RequestHeader(value="password", required = true) String password)
    {

        UserImpl userImpl = UserImpl.getInstance();

        UUID uuid = UUID.randomUUID();
        for (User a:userRepository.findAll()) {
            if (a.getUsername().equals(username)) {
                return "Username already exist, please enter another one!!";
            }
        }
        String  hashPassword= BCrypt.hashpw(password,BCrypt.gensalt());
        User user = new User();
        System.out.println("1");
        user.setUsername(username);
        System.out.println("2");
        user.setPassword(hashPassword);
        System.out.println("3");
        user.setUserid(uuid.toString());
        System.out.println("4");
        userImpl.createUser(user);

        //userRepository.save(user);
        return "Register success!\n";

    }
    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getAllUsers(@RequestHeader(value="username", required = true) String username,
                              @RequestHeader(value="password", required = true) String password) {
        JSONArray obj = new JSONArray();
        //authorization------
        UserImpl userImpl = UserImpl.getInstance();
        String user_id = userImpl.register(username, password);
        if (user_id.equals("")) {
            return "User NOT FOUND";
        }
        //-----------------

        for (User a:userRepository.findAll())
        {
            try {
                obj.put(new JSONObject().put("ID", a.getUserid()).put(" UserName:", a.getUsername()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return obj.toString();
    }



    @RequestMapping(value="/reset",method=RequestMethod.GET)
    @ResponseBody
    public String resettPassword(@RequestHeader(value="username", required=true) String username,
                                 @RequestHeader(value="password", required=true) String password)

    {
        UserImpl userImple=UserImpl.getInstance();

        if(userImple.register(username,password)==null)
        {
            return "Invalid username and password";
        }

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
            return "Rest password link sent to "+username;
        }
        catch (Exception e)
        {
            System.out.println(e.toString()+" "+e.getMessage());
            return e.getMessage();
        }

    }







}