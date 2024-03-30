package sn.esmt.mpisi2.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sn.esmt.mpisi2.DTO.UserRegisteredDTO;
import sn.esmt.mpisi2.Mpisi2Application;
import sn.esmt.mpisi2.model.Role;
import sn.esmt.mpisi2.repository.UserRepository;

import static org.mockito.BDDMockito.given;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = Mpisi2Application.class)
public class LoginControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private UserDetailsService userDetailsService;

    @Mock  // Annotation pour indiquer que c'est un mock
    private UserRepository userRepo;  // Injection du repository

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

//        // Simuler la réponse de findByEmail
//        User user = new User();
//        user.setUsername("testuser");
//        user.setPassword("password123");
//        user.setRole(new Role("USER"));
//
//        given(userRepo.findByEmail("testuser")).willReturn(user);
    }
    @Test
    public void testLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }

//    @Test
//    public void testLoginUser() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/login")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .param("username", "testuser")
//                        .param("password", "password123"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }

}
