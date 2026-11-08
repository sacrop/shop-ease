package com.shop.ease.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.MyUserDetails;


@Repository
public interface UserRepository extends JpaRepository<MyUserDetails,Integer> {

    // public boolean existsByPhone_number(String phone_number);
    public boolean existsByEmail(String email);

    public MyUserDetails findByEmail(String email);

    public List<MyUserDetails> findByUserName(String userName);

    public MyUserDetails findById(int id);

    public MyUserDetails findByPhonenumber(String phonenumber);

    @Query("SELECT COUNT(*) FROM MyUserDetails")
    public int countUsers();

    public int countByRole(String role);
     
}
