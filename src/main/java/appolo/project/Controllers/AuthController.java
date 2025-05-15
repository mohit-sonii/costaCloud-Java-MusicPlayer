package appolo.project.Controllers;

import appolo.project.Entity.Role;
import appolo.project.Entity.User;
import appolo.project.Service.UserService;
import appolo.project.Util.AuthPOJO;
import appolo.project.Util.TokenCookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/join")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenCookie tokenCookie;

    @PostMapping("/u/signup")
    public ResponseEntity<String> Signup(@RequestBody AuthPOJO reqUser, @CookieValue(value = "auth_for_sec",defaultValue = "")String cookiValue, HttpServletResponse response){
            try{
                Optional<User> findUser = userService.findTheUser(reqUser.getUsername());
                if(findUser.isPresent()){
                    return new ResponseEntity<>("User already Present. Try login",HttpStatus.CONFLICT);
                }
               if(!cookiValue.isEmpty()){
                   return new ResponseEntity<>("Try cleaning the cookies and cache",HttpStatus.BAD_REQUEST);
               }
               User newUser = new User();
               newUser.setUsername(reqUser.getUsername());
               newUser.setRole(Role.USER);
               userService.saveUser(newUser,reqUser.getPassword());

               String token = tokenCookie.generateToken(newUser.getUsername(),newUser.getUser_id(),"USER");
               tokenCookie.generateCookie(token,response);
               return new ResponseEntity<>("Account Creation Successfully",HttpStatus.CREATED);

            }catch(Exception e){
                return new ResponseEntity<>("Internal Server Error" + e,HttpStatus.INTERNAL_SERVER_ERROR);
            }

    }

    @PostMapping("/a/signup")
    public ResponseEntity<String> SignupAdmin(@RequestBody AuthPOJO user, @CookieValue(value = "auth_for_sec",defaultValue = "")String cookiValue, HttpServletResponse response){
        try{
            Optional<User> findUser = userService.findTheUser(user.getUsername());
            if(findUser.isPresent()){
                return new ResponseEntity<>("User already Present. Try login",HttpStatus.CONFLICT);
            }
            if(!cookiValue.isEmpty()){
                return new ResponseEntity<>("Try cleaning the cookies and cache",HttpStatus.BAD_REQUEST);
            }
            User newUser = new User();
            newUser.setUsername(user.getUsername());
            newUser.setRole(Role.ADMIN);
            userService.saveUser(newUser,user.getPassword());

            String token = tokenCookie.generateToken(newUser.getUsername(),newUser.getUser_id(),"ADMIN");
            tokenCookie.generateCookie(token,response);
            return new ResponseEntity<>("Account Creation Successfully",HttpStatus.CREATED);

        }catch(Exception e){
            return new ResponseEntity<>("Internal Server Error" + e,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/u/login")
    public ResponseEntity<String> login(@RequestBody AuthPOJO user,@CookieValue(value="auth_for_sec",defaultValue = "") String CookieValue,HttpServletResponse response){
       try{
           Optional<User> findUser = userService.findTheUser(user.getUsername());
           if(findUser.isEmpty()){
               return new ResponseEntity<>("User not Registered",HttpStatus.NOT_FOUND);
           }
           if(userService.validatePassword(user.getPassword(), findUser.get().getPassword()) && userService.validateRole(findUser.get().getRole().toString(),"USER")){
               String token = tokenCookie.generateToken(findUser.get().getUsername(),findUser.get().getUser_id(),"USER");
               tokenCookie.generateCookie(token,response);
               return new ResponseEntity<>("User logged in",HttpStatus.OK);
           }else{
               return new ResponseEntity<>("Either username or password is incorrect",HttpStatus.BAD_REQUEST);
           }

       }catch(Exception e){
           return new ResponseEntity<>("Internal Server Error "+e, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @PostMapping("/a/login")
    public ResponseEntity<String> loginAdmin(@RequestBody AuthPOJO user,@CookieValue(value="auth_for_sec",defaultValue = "") String CookieValue,HttpServletResponse response){
        try{
            Optional<User> findUser = userService.findTheUser(user.getUsername());
            if(findUser.isEmpty()){
                return new ResponseEntity<>("User not Registered",HttpStatus.NOT_FOUND);
            }
            if(userService.validatePassword(user.getPassword(), findUser.get().getPassword()) && userService.validateRole(findUser.get().getRole().toString(),"ADMIN")){
                String token = tokenCookie.generateToken(findUser.get().getUsername(),findUser.get().getUser_id(),"ADMIN");
                tokenCookie.generateCookie(token,response);
                return new ResponseEntity<>("admin logged in ",HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Either username or password is incorrect",HttpStatus.BAD_REQUEST);
            }

        }catch(Exception e){
            return new ResponseEntity<>("Internal Server Error "+e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response,@CookieValue(value = "auth_for_sec",defaultValue = "")String cookie){
        if(cookie.isEmpty())return new ResponseEntity<>("You are not logged in",HttpStatus.FORBIDDEN);
        tokenCookie.clearCookie(response);
        return new ResponseEntity<>("User logged out successfully",HttpStatus.OK);
    }
}
