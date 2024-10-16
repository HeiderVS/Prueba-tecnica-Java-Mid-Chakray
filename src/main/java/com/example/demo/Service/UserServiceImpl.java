package com.example.demo.Service;

import com.example.demo.Model.Address;
import com.example.demo.Model.User;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {


    @Override
    public String encodePassword(String password) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(password.getBytes());
        byte[] digest = md.digest();

        StringBuffer hexString = new StringBuffer();

        for (int i = 0;i<digest.length;i++) {
            hexString.append(Integer.toHexString(0xFF & digest[i]));
        }

        return hexString.toString();
    }

//    @Override
//    public void updateAddress(int userId, int addressId, Address address) {
//        List<User> users = getAllUsers();
//        User user = users.stream().filter(u -> u.getId() == userId).findFirst().orElse(null);
//        if (user == null){ return;}
//        List<Address> addresses = user.getAddresses();
//        Optional<Address> addressToUpdate = addresses.stream().filter(a -> a.getId() == addressId).findFirst();
//        if (addressToUpdate.isPresent()){
//            Address existingAddress = addressToUpdate.get();
//            existingAddress.setName(address.getName());
//            existingAddress.setStreet(address.getStreet());
//            existingAddress.setCountry_code(address.getCountry_code());
//        }
//    }
}
