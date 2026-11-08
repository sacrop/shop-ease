package com.shop.ease.service;


import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shop.ease.Repository.UserRepository;
import com.shop.ease.dto.UserDto;
import com.shop.ease.model.MyUserDetails;



@Service
public class UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelmapper;
    private final BCryptPasswordEncoder passwordencoder;
    
  
    public UserService(
        UserRepository userRepository,
        ModelMapper modelmapper,
        BCryptPasswordEncoder passwordencoder
    ) {
        this.userRepository = userRepository;
        this.modelmapper = modelmapper;
        this.passwordencoder = passwordencoder;
    }

    public MyUserDetails createUser(MyUserDetails user) {
        user.setUserName(user.getUserName());
    
        user.setPassword(passwordencoder.encode(user.getPassword()));
        user.setToken(0);
        
        user.setRole("ROLE_USER");
        user.setOtpverified(false);
        return userRepository.save(user);
        
    }


    public boolean checkUserMail(String email) {
       return userRepository.existsByEmail(email);
    }

    public List<MyUserDetails> getalluser() {
        return userRepository.findAll();
    }


    public List<MyUserDetails> finduser(String usename) {
       return userRepository.findByUserName(usename);
    }
    // @Override
    // public boolean checkUserphoneNumber(String phone_number) {
    //     return userRepo.existsByPhone_number(phone_number); 
    // }


    public void changelock(int id, Boolean lock) {
         MyUserDetails user=userRepository.findById(id);
         if(lock){
            user.setToken(0);
            userRepository.save(user);
         }
         else{
            user.setToken(1);
            userRepository.save(user);
         }
         

    }

   public int getusercount() {
      return userRepository.countUsers();
   }


   public int getadmincount() {
      return userRepository.countByRole("ROLE_ADMIN");
   }

   public MyUserDetails convertUserDtoUserDtls(UserDto userDto){

      MyUserDetails user=new MyUserDetails();
      user=modelmapper.map(userDto,MyUserDetails.class);
      return user;
   }

   public MyUserDetails getUserByMail(String email){
      return userRepository.findByEmail(email);
   }


   public void saveUser(MyUserDetails principalUser) {
      userRepository.save(principalUser);
   }
    
}
