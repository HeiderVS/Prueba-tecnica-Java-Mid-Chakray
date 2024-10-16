package com.example.demo.Service;

import com.example.demo.Model.User;


import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public interface UserService {

    String encodePassword (String password) throws NoSuchAlgorithmException;
}
