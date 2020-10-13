package com.hastlin.zaplacrecepte.controller;

import com.hastlin.zaplacrecepte.service.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
@ActiveProfiles("test")
public class ContactControllerTest {

    @MockBean
    EmailService emailService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_call_email_service_to_send_contact_email() throws Exception {
        //given
        doNothing().when(emailService).sendSimpleMessage(any(), any(), any());

        //when
        this.mockMvc.perform(
                post("/api/contact")
                        .content("{\"message\":\"some message from zaplacrecepte.pl\"}")
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        //then
        verify(emailService, times(1)).sendSimpleMessage("zaplacrecepte@gmail.com", "Kontakt ze strony", "some message from zaplacrecepte.pl");

    }

}