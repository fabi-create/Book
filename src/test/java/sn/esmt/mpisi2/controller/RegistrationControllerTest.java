package sn.esmt.mpisi2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sn.esmt.mpisi2.DTO.UserRegisteredDTO;
import sn.esmt.mpisi2.Mpisi2Application;
import sn.esmt.mpisi2.model.Role;
import sn.esmt.mpisi2.model.User;
import sn.esmt.mpisi2.repository.RoleRepository;
import sn.esmt.mpisi2.repository.UserRepository;
import sn.esmt.mpisi2.service.DefaultUserService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = Mpisi2Application.class)
public class RegistrationControllerTest {

    @Mock
    private DefaultUserService userService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private RegistrationController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void testRegistrationFormSubmission() throws Exception {
        mockMvc.perform(post("/registration")
                        .param("name", "Test User")
                        .param("email_id", "test@example.com")
                        .param("password", "password123")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection()) // Attend une redirection
                .andExpect(redirectedUrl("/login")); // Redirige vers l'URL spécifiée
    }
}
