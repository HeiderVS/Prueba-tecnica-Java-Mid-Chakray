package com.example.demo.Controller;

import com.example.demo.Model.Address;
import com.example.demo.Model.DTO.UserDTO;
import com.example.demo.Model.Response;
import com.example.demo.Model.User;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    List<User> usuarios = new ArrayList<>();
    Response response = new Response();

    public UserController () {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        User user1 = new User(
                1,
                "user1@mail.com",
                "user1",
                "7c4a8d09ca3762af61e59520943dc26494f8941b",
                LocalDateTime.parse("01-01-2024 00:00:00", formatter),
                new ArrayList<>(Arrays.asList(
                        new Address(1L, "workaddress", "street No. 1", "UK"),
                        new Address(2L, "homeaddress", "street No. 2", "AU")
                )));
        User user2 = new User(
                2,
                "user2@mail.com",
                "user2",
                "7c4a8d09ca3762af61e59520943dc26494f8941b",
                LocalDateTime.parse("01-01-2024 00:00:00", formatter),
                new ArrayList<>(Arrays.asList(
                        new Address(1L, "workaddress", "street No. 1", "UK"),
                        new Address(2L, "homeaddress", "street No. 2", "AU")
                )));
        User user3 = new User(
                3,
                "user3@mail.com",
                "user3",
                "7c4a8d09ca3762af61e59520943dc26494f8941b",
                LocalDateTime.parse("01-01-2024 00:00:00", formatter),
                new ArrayList<>(Arrays.asList(
                        new Address(1L, "workaddress", "street No. 1", "UK"),
                        new Address(2L, "homeaddress", "street No. 2", "AU")
                )));
        usuarios.add(user1);
        usuarios.add(user2);
        usuarios.add(user3);
    }
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
    @GetMapping
    public ResponseEntity<Response> sortedBy (@RequestParam String sortedBy) {
        try{

        if(sortedBy == null || sortedBy.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

            switch (sortedBy){
                case "email":
                    usuarios.sort(Comparator.comparing(User::getEmail));
                    break;
                case "id":
                    usuarios.sort(Comparator.comparing(User::getId));
                    break;
                case "name":
                    usuarios.sort(Comparator.comparing(User::getName));
                    break;
                case "created_at":
                    usuarios.sort(Comparator.comparing(User::getCreatedAt));
                    break;
                default:
                   response.setCode(1);
                   response.setMessage("Invalid sortedBy value");
                   response.setUsers_info(null);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            List<UserDTO> userDTOs = usuarios.stream()
                    .map(user -> new UserDTO(
                            user.getId(),
                            user.getEmail(),
                            user.getName(),
                            user.getCreatedAt(),
                            user.getAddresses()))
                    .collect(Collectors.toList());

            response.setCode(0);
            response.setMessage("All Users Sorted by " + sortedBy);
            response.setUsers_info((ArrayList<UserDTO>) userDTOs);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage("An error occurred while searching users");
            response.setUsers_info(null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{user_id}/addresses")
    public ResponseEntity<Response> getAdrressesByUser (@PathVariable int user_id) {
        try {

            User user = usuarios.stream()
                    .filter(u -> u.getId() == user_id)
                    .findFirst()
                    .orElse(null);

            if (user == null) {
                response.setCode(2);
                response.setMessage("User not found");
                response.setUsers_info(null);
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            }
            ArrayList<Address> addresses = user.getAddresses();
            response.setCode(0);
            response.setMessage("Addresses to user: " + user_id);
            response.setUsers_info(addresses);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (Exception e){
            response.setCode(-1);
            response.setMessage("An error occurred while searching user's addresses");
            response.setUsers_info(null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{user_id}/addresses/{address_id}")
    public ResponseEntity<Response> updateAddresseById(@PathVariable int user_id, @PathVariable int address_id, @RequestBody  Address address) {
        try {

            User user = usuarios.stream()
                    .filter(u -> u.getId() == user_id)
                    .findFirst()
                    .orElse(null);

            if (user == null) {
                response.setCode(2);
                response.setMessage("User not found");
                response.setUsers_info(null);
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            ArrayList<Address> addresses = user.getAddresses();
            Optional<Address> addressToUpdate = addresses.stream().filter(u -> u.getId() == address_id).findFirst();
            if (addressToUpdate.isPresent()) {
                Address existingAddress = addressToUpdate.get();
//                if (address.getName() != null && existingAddress.getName() != "") {
//                    existingAddress.setName(address.getName());
//                }
                if (address.getName() != null && !address.getName().isEmpty()) {
                    existingAddress.setName(address.getName());
                }
                if (address.getStreet() != null && !address.getStreet().isEmpty()) {
                    existingAddress.setStreet(address.getStreet());
                }
                if (address.getCountry_code() != null && !address.getCountry_code().isEmpty()) {
                    existingAddress.setCountry_code(address.getCountry_code());
                }
            } else {
                response.setCode(2);
                response.setMessage("Address not found");
                response.setUsers_info(null);
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            response.setCode(0);
            response.setMessage("From user: " + user_id + " Address: " + address_id + " was updated");
            response.setUsers_info(addresses);
            return new ResponseEntity<>(response, HttpStatus.OK);
//            List<Address> addresses = user.getAddresses();
//            addresses.stream()
//                    .filter(u -> u.getId() == address_id)
//                    .findFirst()
//                    .orElse(null);
//                    .ifPresent(p -> {
//                        p.setName(address.getName());
//                        p.setStreet(address.getStreet());
//                        p.setCountry_code(address.getCountry_code());
//                    });
//            if (address == null) {
//                response.setCode(3);
//                response.setMessage("Address not found");
//                response.setUsers_info(null);
//                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
//            }

//            addresses.stream()
//                    .filter(u -> u.getId() == address_id)
//                    .findFirst()
//                    .ifPresent(p -> {
//                        p.setName(address.getName());
//                        p.setStreet(address.getStreet());
//                        p.setCountry_code(address.getCountry_code());
//                    });
//            response.setCode(0);
//            response.setMessage("From user: " + user_id + "Addresses: " + address_id + "was updated");
//            response.setUsers_info((ArrayList<Address>)addresses );
//            return  new  ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.setCode(-1);
            response.setMessage("An error occurred while searching user's addresses");
            response.setUsers_info(null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Response> saveUser (@RequestBody User user){
        try{
            if (user.getName() == null || user.getEmail() == null || user.getPassword() == null  || user.getAddresses() == null) {
                response.setCode(-1);
                response.setMessage("Invalid data");
                response.setUsers_info(null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            //Obtenemos el ultimo usuario registrado
            Optional<User> lastUser = usuarios.stream()
                    .max(Comparator.comparing(User::getId));

            //Obtener el ultimo usuario registrado, si no existe nigun usuario setear el id 1  por defecto
            int newId = lastUser.map(userObj -> userObj.getId() + 1 ).orElse(1);

            //Convertir la fecha y hora a la zona horaria de UK
            ZonedDateTime zoneDateTimeUk = ZonedDateTime.now(ZoneId.of("Europe/London"));
            LocalDateTime nowUkTime = zoneDateTimeUk.toLocalDateTime();

            User newUser = new User();

            newUser.setId(newId);
            newUser.setName(user.getName());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(userService.encodePassword(user.getPassword()));
            newUser.setAddresses(user.getAddresses());
            newUser.setCreatedAt(nowUkTime);
            usuarios.add(newUser);

            response.setCode(0);
            response.setMessage("User created");
            response.setUsers_info(null);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (Exception e){
            response.setCode(-1);
            response.setMessage("An error occurred while saving the user");
            response.setUsers_info(null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> updateUserById (@PathVariable int id, @RequestBody User user) {
        try{
            Optional<User> userOptional = usuarios.stream()
                    .filter(u -> u.getId() == id)
                    .findFirst();
            if (!userOptional.isPresent()) {
                response.setCode(2);
                response.setMessage("User not found");
                response.setUsers_info(null);
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            User existingUser = userOptional.get();

            if (user.getName() != null || !user.getName().isEmpty()) {
                existingUser.setName(user.getName());
            }
            if (user.getEmail() != null || !user.getEmail().isEmpty()){
                existingUser.setEmail(user.getEmail());
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(userService.encodePassword(user.getPassword()));
            }


            response.setCode(0);
            response.setMessage("User updated successfully");
            response.setUsers_info(new ArrayList<>(Arrays.asList(existingUser)));
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.setCode(-1);
            response.setMessage("An error occurred while updating the user");
            response.setUsers_info(null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteUserById (@PathVariable int id) {
        try{
            Optional<User> oprtionalUser = usuarios.stream()
                    .filter(u -> u.getId() == id)
                    .findFirst();
            if(!oprtionalUser.isPresent()){
                response.setCode(2);
                response.setMessage("User not found");
                response.setUsers_info(null);
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            usuarios.remove(oprtionalUser.get());
            response.setCode(0);
            response.setMessage("User deleted successfully");
            response.setUsers_info(null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.setCode(-1);
            response.setMessage("An error occurred while deleting the user");
            response.setUsers_info(null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
