package ru.dgi.controller.result;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.dgi.controller.AbstractControllerTest;
import ru.dgi.util.json.JsonUtil;
import ru.dgi.model.Quest;
import ru.dgi.model.Result;
import java.time.LocalDateTime;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminResultRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminResultRestController.REST_URL + '/';

    @Test
    public void testGetUnAuth() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        Result result = getCreatedResult(ADMIN_NAME, quest, true);
        mockMvc.perform(get(setParentId(REST_URL, quest.getId()) + result.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetForbidden() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        Result result = getCreatedResult(ADMIN_NAME, quest, true);
        mockMvc.perform(get(setParentId(REST_URL, quest.getId()) + result.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGet() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        Result result = getCreatedResult(ADMIN_NAME, quest, true);
        mockMvc.perform(get(setParentId(REST_URL, quest.getId()) + result.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_RESULT.contentMatcher(result));
    }

    @Test
    public void testGetNotFound() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        mockMvc.perform(get(setParentId(REST_URL, quest.getId()) + "0")
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    public void testGetAll() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        Result result1 = getCreatedResult(ADMIN_NAME, quest,true);
        Result result2 = getCreatedResult(USER_NAME, quest,true);
        mockMvc.perform(get(setParentId(REST_URL, quest.getId()))
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_RESULT.contentListMatcher(result1, result2));
    }

    @Test
    public void testGetCount() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        Result result1 = getCreatedResult(ADMIN_NAME, quest,true);
        Result result2 = getCreatedResult(USER_NAME, quest,true);
        mockMvc.perform(get(setParentId(REST_URL, quest.getId()) + "count/")
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testCreate() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        Result created = getCreatedResult(ADMIN_NAME, quest,false);
        ResultActions action = mockMvc.perform(post(setParentId(REST_URL, quest.getId()))
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN)
                .content(JsonUtil.writeValue(created))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Result returned = MATCHER_RESULT.fromJsonAction(action);
        created.setId(returned.getId());
        MATCHER_RESULT.assertEquals(created, returned);
    }

    @Test
    public void testUpdate() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        Result result = getCreatedResult(ADMIN_NAME, quest,true);
        result.setAnswerModified(LocalDateTime.now());
        ResultActions action = mockMvc.perform(post(setParentId(REST_URL, quest.getId()))
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN)
                .content(JsonUtil.writeValue(result))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Result returned = MATCHER_RESULT.fromJsonAction(action);
        MATCHER_RESULT.assertEquals(result, returned);
    }

    @Test
    public void testDelete() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        Result result = getCreatedResult(ADMIN_NAME, quest,true);
        mockMvc.perform(delete(setParentId(REST_URL, quest.getId()) + result.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andExpect(status().isOk());
        MATCHER_RESULT.assertCollectionEquals(Collections.emptyList(), resultService.getAll(quest.getId()));
    }

    @Test
    @Transactional
    public void testDeleteNotFound() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        mockMvc.perform(delete(setParentId(REST_URL, quest.getId()) + "0")
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
