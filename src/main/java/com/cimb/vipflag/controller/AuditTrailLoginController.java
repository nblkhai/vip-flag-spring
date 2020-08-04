package com.cimb.vipflag.controller;


import com.cimb.vipflag.dao.AuditTrailChangesRepo;
import com.cimb.vipflag.dao.AuditTrailLoginRepo;
import com.cimb.vipflag.dao.UserRepo;
import com.cimb.vipflag.entity.AuditTrailChanges;
import com.cimb.vipflag.entity.AuditTrailLogin;
import com.cimb.vipflag.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/audit_login")
@CrossOrigin
public class AuditTrailLoginController {

    @Autowired
    private AuditTrailLoginRepo auditTrailLoginRepo;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/all_log")
    public Iterable<AuditTrailLogin> allLoginLog(){
        return auditTrailLoginRepo.findAll();
    }


    @PostMapping("/loginlog/{userId}")
    public AuditTrailLogin addUserLoginLog(@PathVariable int userId){
        LocalDateTime localDateTime = LocalDateTime.now();
        AuditTrailLogin findLog = auditTrailLoginRepo.findLogByUserId(userId);

        User findUser = userRepo.findById(userId).get();
        System.out.println("ini findLog");
        System.out.println(findLog);
        if(findLog==null){
            AuditTrailLogin newLog = new AuditTrailLogin();
            newLog.setLastLogin(localDateTime);
            newLog.setUserId(findUser.getUserId());
            newLog.setUsername(findUser.getUsername());
            return auditTrailLoginRepo.save(newLog);
        }
        else{
            findLog.setLastLogin(localDateTime);
            return auditTrailLoginRepo.save(findLog);
        }

    }

    @PostMapping("/logoutlog/{userId}")
    public AuditTrailLogin addUserLogoutLog(@PathVariable int userId){
        LocalDateTime localDateTime = LocalDateTime.now();
        AuditTrailLogin findLog = auditTrailLoginRepo.findLogByUserId(userId);
        System.out.println("ini find log logout");
        System.out.println(findLog);
        findLog.setLastLogout(localDateTime);
        return auditTrailLoginRepo.save(findLog);
    }
}
