package appolo.project.Service;

import appolo.project.Entity.User;
import appolo.project.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;
    public UserService (PasswordEncoder passwordEncoder){
        this.passwordEncoder=passwordEncoder;
    }

    public void saveUser(User user,String password){
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        userRepo.save(user);
    }

    public boolean validatePassword(String password,String storedPassword){
        return passwordEncoder.matches(password,storedPassword);
    }

    public boolean validateRole(String role,String desiredRoleForThatRoute){
        return role.equals(desiredRoleForThatRoute);

    }

    public Optional<User> findTheUser(String username){
        try{
            return userRepo.findByUsername(username);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
