package ru.dgi.controller.variant;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ru.dgi.controller.AbstractControllerTest;
import ru.dgi.model.Variant;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VariantRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VariantRestController.REST_URL + '/';

    @Test
    public void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGet() throws Exception {
        Variant variant = getCreatedVariant("variant1", true);
        mockMvc.perform(get(REST_URL +  + variant.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_VARIANT.contentMatcher(variant));
    }

    @Test
    public void testGetNotFound() throws Exception {
        mockMvc.perform(get( REST_URL + "0")
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    public void testGetAll() throws Exception {
        Variant variant1 = getCreatedVariant("variant1", true);
        Variant variant2 = getCreatedVariant("variant2", true);
        Variant variant3 = getCreatedVariant("variant3", true);
        Variant variant4 = getCreatedVariant("variant4", true);
        mockMvc.perform(get(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_VARIANT.contentListMatcher(variant1, variant2, variant3, variant4));
    }
}
