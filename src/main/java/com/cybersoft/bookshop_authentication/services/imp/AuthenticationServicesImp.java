package com.cybersoft.bookshop_authentication.services.imp;

import com.cybersoft.bookshop_authentication.dto.UserDTO;
import com.cybersoft.bookshop_authentication.entity.Users;
import com.cybersoft.bookshop_authentication.enumable.StatusUser;
import com.cybersoft.bookshop_authentication.exception.DataNotFound;
import com.cybersoft.bookshop_authentication.payload.request.SignUpRequest;
import com.cybersoft.bookshop_authentication.repository.UsersRepository;
import com.cybersoft.bookshop_authentication.services.AuthenticationServices;
import com.cybersoft.bookshop_authentication.utils.CommonHelper;
import com.cybersoft.bookshop_authentication.utils.JwtHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServicesImp implements AuthenticationServices {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public String signIn(String email, String password) {
        Users users = usersRepository.findByEmail(email).orElseThrow(() -> new DataNotFound("User not found with email: " + email));
        if (!passwordEncoder.matches(password, users.getPassword())) {
            throw new DataNotFound("Invalid password for user: " + email);
        }else{
            return jwtHelper.generateToken(users.getEmail());
        }
    }

    @Override
    public Users signUp(SignUpRequest signUpRequest) {
        Optional<Users> existingUser = usersRepository.findByEmail(signUpRequest.getEmail());
        // email ko tìm thấy thì tạo mới
        System.out.println(existingUser);
        String randomPassword = CommonHelper.generateRandomPassword(8);

        Users newUser = new Users();
        newUser.setEmail(signUpRequest.getEmail());
        newUser.setStatus(StatusUser.CHANGE_PASSWORD);
        newUser.setPassword(passwordEncoder.encode(randomPassword));

        usersRepository.save(newUser);

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(newUser.getEmail());
        userDTO.setPassword(randomPassword);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonData = objectMapper.writeValueAsString(userDTO);
            kafkaTemplate.send("email", jsonData);
        } catch (Exception e){
            throw new RuntimeException( "Error converting UserDTO to JSON",e);
        }
        return newUser;
    }
}
