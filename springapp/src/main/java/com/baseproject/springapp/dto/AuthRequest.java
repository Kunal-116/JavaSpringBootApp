
/* DTO for receiving username and password from the frontend for register/login. */

package com.baseproject.springapp.dto;

public record AuthRequest(String u_mobile,String u_password) {
}
