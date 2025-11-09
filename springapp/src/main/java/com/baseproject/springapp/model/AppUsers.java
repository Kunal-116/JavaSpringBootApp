package com.baseproject.springapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

 @Entity
@Table(name="tbl_users")
public class AppUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    private String u_name;

    @Column(unique = true)
    private String u_email;

    @Column(name = "u_mobile", unique = true)
    private String umobile;

    private String u_password;

    private String u_role;


   public Long getId()
   {
    return user_id;
   }

   public void setId(Long user_id)
   {
    this.user_id=user_id;
   }

   public String getUname()
   {
    return u_name;
   }

   public String getRole()
   {
    return u_role;
   }

   public void setRole(String u_role)
   {
    this.u_role=u_role;
   }

   public void setUname(String u_name)
   {
    this.u_name=u_name;
   }

   public String getEmail()
   {
    return u_email;
   }

   public void setEmail(String u_email)
   {
    this.u_email=u_email;
   }

   public String getPassword()
   {
    return this.u_password;
   }

   public void setPassword(String u_password)
   {
    this.u_password=u_password;
   }

   public String getUmobile() {
    return umobile;
}

public void setUmobile(String UMobile) {
    this.umobile = UMobile;
}

    
}
