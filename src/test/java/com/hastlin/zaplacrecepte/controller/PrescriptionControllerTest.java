package com.hastlin.zaplacrecepte.controller;

import com.hastlin.zaplacrecepte.model.entity.UserEntity;
import com.hastlin.zaplacrecepte.repository.PrescriptionRepository;
import com.hastlin.zaplacrecepte.repository.UserRepository;
import com.hastlin.zaplacrecepte.service.EmailService;
import com.hastlin.zaplacrecepte.service.SmsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class PrescriptionControllerTest {

    @Value("classpath:/prescriptionDto.json")
    private Resource prescriptionJson;

    @MockBean
    PrescriptionRepository prescriptionRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    EmailService emailService;

    @MockBean
    SmsService smsService;


    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        Authentication authentication = Mockito.mock(Authentication.class);
        UserEntity userEntity = UserEntity.builder().id("someUserId").clientType("SERVICE_BASED").defaultPrice(30).paymentProvider("P24").build();
        when(authentication.getPrincipal()).thenReturn(userEntity);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void should_save_entity_and_get_200OK_when_everything_ok() throws Exception {
        doNothing().when(emailService).sendSimpleMessage(any(), any(), any());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(UserEntity.builder().smsMessageRequestPayment("Please pay us").build()));
        this.mockMvc.perform(
                post("/api/prescriptions")
                        .content(Files.readAllBytes(prescriptionJson.getFile().toPath()))
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();



        verify(prescriptionRepository, times(2)).save(any());
    }

    @Test
    public void should_answer_with_all_prescriptions() throws Exception {
        this.mockMvc.perform(
                get("/api/prescriptions")
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        verify(prescriptionRepository, times(1)).findByOwnerId(eq("someUserId"));
    }

}