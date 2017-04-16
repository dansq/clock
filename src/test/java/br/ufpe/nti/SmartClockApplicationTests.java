package br.ufpe.nti;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import br.ufpe.nti.model.ClockController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmartClockApplicationTests {

	private final MockMvc mockMvc = standaloneSetup(new ClockController()).build();

    @Test
    public void validate_get_clock() throws Exception {

        mockMvc.perform(get("/clock"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.time").isNotEmpty())
                .andExpect(jsonPath("$.angle").isNotEmpty())
                .andExpect(jsonPath("$.angle").value("999.99"));
    }

}
