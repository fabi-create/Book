package sn.esmt.mpisi2.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sn.esmt.mpisi2.Mpisi2Application;
import sn.esmt.mpisi2.model.MyBookList;
import sn.esmt.mpisi2.service.EmprunteurService;
import sn.esmt.mpisi2.service.MyBookListService;

import java.sql.Date;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = Mpisi2Application.class)
public class MyBookListControllerIntegrationTest {


    private MockMvc mockMvc;

    @MockBean
    private MyBookListService myBookListService;

    @MockBean
    private EmprunteurService emprunteurService;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void deleteMyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/deleteMyList/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/dashboard/my_books"));

        verify(myBookListService).deleteById(1);
    }

    @Test
    void editMyList() throws Exception {
        MyBookList myBookList = new MyBookList();
        myBookList.setId(1);
        myBookList.setName("Book Name");
        myBookList.setAuthor("Author Name");

        when(myBookListService.getMyBookListById(1)).thenReturn(myBookList);

        mockMvc.perform(MockMvcRequestBuilders.get("/editMyList/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("MyBookListEdit"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("myBookList"));
    }

    @Test
    void updateEmprunteur() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/updateEmprunteur/{id}", 1)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("emprunteurId", "123"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/dashboard/my_books"));

        verify(myBookListService).updateEmprunteurId(1, 123);
    }

    @Test
    void updateDateRetour() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/{id}/updateDateRetour", 1)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("dateRetour", String.valueOf(Date.valueOf("2024-04-03"))))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/dashboard/my_books"));

        verify(myBookListService).updateDateRetour(1, Date.valueOf("2024-04-03"));
    }
}
