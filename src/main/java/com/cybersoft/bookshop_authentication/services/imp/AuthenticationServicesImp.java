package com.cybersoft.bookshop_authentication.services.imp;

import com.cybersoft.bookshop_authentication.dto.TokenDTO;
import com.cybersoft.bookshop_authentication.dto.UserDTO;
import com.cybersoft.bookshop_authentication.entity.Users;
import com.cybersoft.bookshop_authentication.enumable.StatusUser;
import com.cybersoft.bookshop_authentication.exception.DataNotFound;
import com.cybersoft.bookshop_authentication.payload.request.SignUpRequest;
import com.cybersoft.bookshop_authentication.repository.UsersRepository;
import com.cybersoft.bookshop_authentication.services.AuthenticationServices;
import com.cybersoft.bookshop_authentication.services.MailSenderService;
import com.cybersoft.bookshop_authentication.utils.CommonHelper;
import com.cybersoft.bookshop_authentication.utils.JwtHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AuthenticationServicesImp implements AuthenticationServices {

    @Autowired
    private MailSenderService mailSenderService;

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
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setEmail(users.getEmail());
            tokenDTO.setRoles(List.of(users.getRole().getName()));

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = "";
            try {
                jsonData = objectMapper.writeValueAsString(tokenDTO);
            } catch (Exception e) {
                throw new DataNotFound("Error while serializing tokenDTO", e);
            }

            // mình can thiệt token để trả ra data token có jwt nằm ở trỏng
            return jwtHelper.generateToken(jsonData);

        }
    }

    @Override
    public List<String> decodeToken(String token) {
        // sử dụng api này chỉ để trả về danh sách token và trả ra danh sách role
        String data = jwtHelper.validateAndGetDataToken(token);
        if(data == null || data.isEmpty()) {
            throw new DataNotFound("Invalid token");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        TokenDTO tokenDTO = null;
        try {
            tokenDTO = objectMapper.readValue(data, TokenDTO.class);
            return tokenDTO.getRoles();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Users signUp(SignUpRequest signUpRequest) {
        // email ko tìm thấy thì tạo mới
        // email đã tồn tại thì trả về thông báo lỗi
        Optional<Users> existingUser = usersRepository.findByEmail(signUpRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new DataNotFound("User already exists with email: " + signUpRequest.getEmail());
        }

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
//            mailSenderService.sendEmail(newUser.getEmail(), "Welcome to Bookshop", "Your account has been created. Please change your password using the following link: <link>");
        } catch (Exception e){
            throw new RuntimeException( "Error converting UserDTO to JSON",e);
        }
        return newUser;
    }
}
