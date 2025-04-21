package com.hospital_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital_management_system.entity.Role;
import com.hospital_management_system.entity.User;
import com.hospital_management_system.payload.LoginDTO;
import com.hospital_management_system.payload.SignUpDTO;
import com.hospital_management_system.repository.RoleRepository;
import com.hospital_management_system.repository.UserRepository;
import com.hospital_management_system.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtTokenProvider tokenProvider;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void testAuthenticateUser() throws Exception{

        LoginDTO loginDTO = new LoginDTO();
           loginDTO.setUsernameOrEmail("user@example.com");
           loginDTO.setPassword("password");

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));



        Mockito.when(authenticationManager.authenticate((new UsernamePasswordAuthenticationToken(
                loginDTO.getUsernameOrEmail(), loginDTO.getPassword())))).thenReturn(authentication);

        Mockito.when(tokenProvider.generateToken(authentication)).thenReturn("mocked-token");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").value("mocked-token"));


    }
    @Test
    public void testRegisterUser() throws Exception{

        SignUpDTO signUpDTO = new SignUpDTO();
          signUpDTO.setName("prashant");
          signUpDTO.setEmail("pra@123gmail.com");
          signUpDTO.setUsername("pra123");
          signUpDTO.setPassword("pr12398");

          Role role = new Role();
            role.setId(1);
            role.setName("admin");

            User user = new User();
            user.setName(signUpDTO.getName());
            user.setUsername(signUpDTO.getUsername());
            user.setEmail(signUpDTO.getEmail());
            user.setPassword("asdadsas838823djdsfj2329");

            Mockito.when(userRepository.existsByUsername(signUpDTO.getUsername())).thenReturn(false);

            Mockito.when(userRepository.existsByEmail(signUpDTO.getEmail())).thenReturn(false);
            Mockito.when(passwordEncoder.encode(signUpDTO.getPassword())).thenReturn("sadadasds8238238sadasd929");
            Mockito.when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(role));
            Mockito.when(userRepository.save(user)).thenReturn(user);

            mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(signUpDTO)))
                            .andExpect(MockMvcResultMatchers.status().isOk())
                            .andExpect(MockMvcResultMatchers.content().string("User registered successfully"));

    }

    @Test
    public void testRegisterUser_Username_AlreadyTaken() throws Exception{

        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setName("prashant");
        signUpDTO.setEmail("pra@123gmail.com");
        signUpDTO.setUsername("pra123");
        signUpDTO.setPassword("pr12398");

        Mockito.when(userRepository.existsByUsername(signUpDTO.getUsername())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpDTO)))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andExpect(MockMvcResultMatchers.content().string("Username is already taken!"));

    }

    @Test
    public void testRegisterUser_EmailId_AlreadyTaken() throws Exception{

        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setName("prashant");
        signUpDTO.setEmail("pra@123gmail.com");
        signUpDTO.setUsername("pra123");
        signUpDTO.setPassword("pr12398");

        Mockito.when(userRepository.existsByEmail(signUpDTO.getEmail())).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpDTO)))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andExpect(MockMvcResultMatchers.content().string("Email is already taken!"));

    }
}
