package com.shop.ease.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.UserDtls;



@Repository
public interface UserRepository extends JpaRepository<UserDtls,Integer> {

    // public boolean existsByPhone_number(String phone_number);
    public boolean existsByEmail(String email);

    public UserDtls findByEmail(String email);

    public List<UserDtls> findByUsename(String usename);

    public UserDtls findById(int id);

    public UserDtls findByPhonenumber(String phonenumber);

    @Query("SELECT COUNT(*) FROM UserDtls")
    public int countuser();

    public int countByRole(String role);
     
}
