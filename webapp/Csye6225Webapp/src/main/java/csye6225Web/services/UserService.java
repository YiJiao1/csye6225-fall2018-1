package csye6225Web.services;

import com.amazonaws.services.dynamodbv2.xspec.B;
import csye6225Web.models.User;
import csye6225Web.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    public boolean userNameExist(String username)
    {
        for(User u:userRepository.findAll())
        {
            if (u.getUsername().equals(username))
            {
                return true;
            }
        }
        return false;
    }


    public void saveUser(String username, String password)
    {
        User user= new User();
        user.setUsername(username);
        user.setPassword(BCrypt.hashpw(password,BCrypt.gensalt()));
        userRepository.save(user);
    }


    public boolean userIsValid(String username, String password)
    {
        for(User u: userRepository.findAll())
        {
            if(u.getUsername().equals(username)&& BCrypt.checkpw(password,u.getPassword()))
            {
                return true;
            }
        }

        return false;
    }






}
