package ru.dgi.controller.question;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.dgi.controller.AbstractControllerTest;
import ru.dgi.util.json.JsonUtil;
import ru.dgi.model.Quest;
import ru.dgi.model.Question;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminQuestionRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminQuestionRestController.REST_URL + '/';

    @Test
    public void testGetUnAuth() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        Question question = getCreatedQuestion("question1", quest, true);
        mockMvc.perform(get(setParentId(REST_URL, quest.getId()) + question.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetForbidden() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        Question question = getCreatedQuestion("question1", quest, true);
        mockMvc.perform(get(setParentId(REST_URL, quest.getId()) + question.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGet() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        Question question = getCreatedQuestion("question1", quest, true);
        mockMvc.perform(get(setParentId(REST_URL, quest.getId()) + question.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_QUESTION.contentMatcher(question));
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
        Question question1 = getCreatedQuestion("question1", quest,true);
        Question question2 = getCreatedQuestion("question2", quest,true);
        mockMvc.perform(get(setParentId(REST_URL, quest.getId()))
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_QUESTION.contentListMatcher(question1, question2));
    }

    @Test
    public void testCreate() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        Question created = getCreatedQuestion("question1", quest,false);
        ResultActions action = mockMvc.perform(post(setParentId(REST_URL, quest.getId()))
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN)
                .content(JsonUtil.writeValue(created))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Question returned = MATCHER_QUESTION.fromJsonAction(action);
        created.setId(returned.getId());
        MATCHER_QUESTION.assertEquals(created, returned);
    }

    @Test
    public void testUpdate() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        Question question = getCreatedQuestion("question1", quest,true);
        question.setName("question1 updated");
        ResultActions action = mockMvc.perform(post(setParentId(REST_URL, quest.getId()))
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN)
                .content(JsonUtil.writeValue(question))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Question returned = MATCHER_QUESTION.fromJsonAction(action);
        MATCHER_QUESTION.assertEquals(question, returned);
    }

    @Test
    public void testDelete() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        Question question = getCreatedQuestion("question", quest,true);
        mockMvc.perform(delete(setParentId(REST_URL, quest.getId()) + question.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andExpect(status().isOk());
        MATCHER_QUESTION.assertCollectionEquals(Collections.emptyList(), questionService.getAll(quest.getId()));
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        mockMvc.perform(delete(setParentId(REST_URL, quest.getId()) + "0")
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
