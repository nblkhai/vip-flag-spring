package com.cimb.vipflag.controller;

import com.cimb.vipflag.dao.CifDataUploadedRepo;
import com.cimb.vipflag.dao.UserRepo;
import com.cimb.vipflag.dao.UserRoleRepo;
import com.cimb.vipflag.entity.User;
import com.cimb.vipflag.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private CifDataUploadedRepo cifDataUploadedRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserRoleRepo userRoleRepo;

    private PasswordEncoder pwEncoder = new BCryptPasswordEncoder();

    @GetMapping("/{userId}")
    public User getUserById (@PathVariable int userId){
        User findUser = userRepo.findById(userId).get();
        return findUser;
    }

    @GetMapping("/username/{username}")
    public User getUserByUsername (@PathVariable String username){
        User findUser = userRepo.findByUsername(username);
        System.out.println(findUser);
        return findUser;
    }

    //add new user with user role
    @PostMapping("/add/role/{roleName}")
    public User addUserWithRole (@RequestBody User userData, @PathVariable String roleName){
        UserRole findRole = userRoleRepo.findUserRoleByRoleName(roleName);
        userData.setUserRole(findRole);
        String encodedPassword = pwEncoder.encode(userData.getPassword());
        userData.setPassword(encodedPassword);
        System.out.println(encodedPassword);
        return userRepo.save(userData);
    }
    //add user role dengan user sudah ada di db
    @PostMapping("/{userId}/role/{roleName}")
    public User addUserRole (@PathVariable int userId,@PathVariable String roleName){
        UserRole findRole = userRoleRepo.findUserRoleByRoleName(roleName);
        User findUser = userRepo.findById(userId).get();
        findUser.setUserRole(findRole);
        return userRepo.save(findUser);
    }

    @PostMapping("/remove/{userId}")
    public void removeUserRole (@PathVariable int userId){

        userRepo.deleteById(userId);
    }

    @GetMapping("/all_users")
    public List<User> allUsers (){
        return userRepo.findAll();
    }

    @GetMapping("/login")
    public User getLoginParams(@RequestParam String username, @RequestParam String password){
        LocalDateTime localDateTime = LocalDateTime.now();

        User findUser = userRepo.findUserLogin(username,password);
        findUser.setLastEntry(localDateTime);
        userRepo.save(findUser);
        return findUser;
    }

    @PostMapping("/logout/{userId}")
    public User userLogout(@PathVariable int userId){
        LocalDateTime localDateTime = LocalDateTime.now();

        User findUser = userRepo.findById(userId).get();
        findUser.setLastLogout(localDateTime);
        userRepo.save(findUser);
        return findUser;
    }

    @PostMapping("/register")
    @Transactional
    public User registerUser(@RequestBody User user) {

        userRepo.save(user);

        return user;
    }

     @PostMapping("/changePassword")
 public User changePassword(@RequestBody User user, @RequestParam("oldPass") String oldPass,@RequestParam("newPass") String newPass){
     User findUser = userRepo.findById(user.getUserId()).get();
     System.out.println(oldPass);
     System.out.println(newPass);
//     if (pwEncoder.matches(oldPass, findUser.getPassword())) {
//         System.out.println("masuk");
//         String newPassEncoded = pwEncoder.encode(newPass);
//         findUser.setPassword(newPassEncoded);
//         userRepo.save(findUser);
//         return findUser;
//     }
//     throw new RuntimeException("Wrong old password!");
         findUser.setPassword(newPass);
         userRepo.save(findUser);
         return user;
 }


}
