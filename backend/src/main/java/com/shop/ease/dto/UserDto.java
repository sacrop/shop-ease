package com.shop.ease.dto;

public class UserDto {
    private int userId;
    private String userName;
    private String email;
    private String phoneNumber;
    private String password;
    private String role;
    private int token;
    private boolean otpverified;

    private UserDto(Builder builder) {
        this.userId = builder.userId;
        this.userName = builder.userName;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        this.password = builder.password;
        this.role = builder.role;
        this.token = builder.token;
        this.otpverified = builder.otpverified;
    }

    public static class Builder {
        private int userId;
        private String userName;
        private String email;
        private String phoneNumber;
        private String password;
        private String role;
        private int token;
        private boolean otpverified;

        public Builder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setRole(String role) {
            this.role = role;
            return this;
        }

        public Builder setToken(int token) {
            this.token = token;
            return this;
        }

        public Builder setOtpverified(boolean otpverified) {
            this.otpverified = otpverified;
            return this;
        }

        public UserDto build() {
            return new UserDto(this);
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public boolean isOtpverified() {
        return otpverified;
    }

    public void setOtpverified(boolean otpverified) {
        this.otpverified = otpverified;
    }

    
}
