package ru.dgi.controller.variant;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.dgi.controller.AbstractControllerTest;
import ru.dgi.util.json.JsonUtil;
import ru.dgi.model.Variant;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminVariantRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminVariantRestController.REST_URL + '/';

    @Test
    public void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetForbidden() throws Exception {
        mockMvc.perform(get(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCreate() throws Exception {
        Variant created = getCreatedVariant("variant1",true);
        ResultActions action = mockMvc.perform(post(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN)
                .content(JsonUtil.writeValue(created))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Variant returned = MATCHER_VARIANT.fromJsonAction(action);
        created.setId(returned.getId());
        MATCHER_VARIANT.assertEquals(created, returned);
    }

    @Test
    public void testUpdate() throws Exception {
        Variant variant = getCreatedVariant("variant1", true);
        variant.setName("variant1 updated");
        ResultActions action = mockMvc.perform(post(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN)
                .content(JsonUtil.writeValue(variant))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Variant returned = MATCHER_VARIANT.fromJsonAction(action);
        MATCHER_VARIANT.assertEquals(variant, returned);
    }

    @Test
    public void testDelete() throws Exception {
        Variant variant = getCreatedVariant("variant", true);
        mockMvc.perform(delete(REST_URL + variant.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andExpect(status().isOk());
        MATCHER_VARIANT.assertCollectionEquals(Collections.emptyList(), variantService.getAll());
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete(REST_URL + "0")
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}