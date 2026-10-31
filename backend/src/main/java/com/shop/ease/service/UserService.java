package com.shop.ease.service;


import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shop.ease.Repository.UserRepository;
import com.shop.ease.dto.UserDto;


import com.shop.ease.model.UserDtls;

@Service
public class UserService{

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelMapper modelmapper;


    @Autowired
    private BCryptPasswordEncoder passwordencoder;
    
  
    public UserDtls creaUser(UserDtls user) {
        user.setUsename(user.getUsename());
    
        user.setPassword(passwordencoder.encode(user.getPassword()));
        user.setToken(0);
        
        user.setRole("ROLE_USER");
        user.setOtpverified(false);
        return userRepo.save(user);
        
    }


    public boolean checkUserMail(String email) {
       return userRepo.existsByEmail(email);
    }

    public List<UserDtls> getalluser() {
        return userRepo.findAll();
    }


    public List<UserDtls> finduser(String usename) {
       return userRepo.findByUsename(usename);
    }
    // @Override
    // public boolean checkUserphoneNumber(String phone_number) {
    //     return userRepo.existsByPhone_number(phone_number); 
    // }


    public void changelock(int id, Boolean lock) {
         UserDtls user=userRepo.findById(id);
         if(lock){
            user.setToken(0);
            userRepo.save(user);
         }
         else{
            user.setToken(1);
            userRepo.save(user);
         }
         

    }

   public int getusercount() {
      return userRepo.countuser();
   }


   public int getadmincount() {
      return userRepo.countByRole("ROLE_ADMIN");
   }

   public UserDtls convertUserDtoUserDtls(UserDto userDto){

      UserDtls user=new UserDtls();
      user=modelmapper.map(userDto,UserDtls.class);
      return user;
   }

   public UserDtls getUserByMail(String email){
      return userRepo.findByEmail(email);
   }


public void saveUser(UserDtls principalUser) {
   userRepo.save(principalUser);
}
    
}
