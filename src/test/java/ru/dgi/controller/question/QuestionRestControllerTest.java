package ru.dgi.controller.question;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ru.dgi.controller.AbstractControllerTest;
import ru.dgi.model.Quest;
import ru.dgi.model.Question;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QuestionRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = QuestionRestController.REST_URL + '/';

    @Test
    public void testGetUnAuth() throws Exception {
        mockMvc.perform(get(setParentId(REST_URL, + 0) + 0))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetActive() throws Exception {
        Quest quest = getCreatedActiveQuest("quest1", true);
        Question question = getCreatedQuestion("question1", quest, true);
        mockMvc.perform(get(setParentId(REST_URL, quest.getId()) + question.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_QUESTION.contentMatcher(question));
    }

    @Test
    public void testGetActiveNotFound() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        Question question = getCreatedQuestion("question1", quest, true);
        mockMvc.perform(get(setParentId(REST_URL, quest.getId()) + question.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    public void testGetAllActive() throws Exception {
        Quest quest = getCreatedActiveQuest("quest1", true);
        Question question1 = getCreatedQuestion("question1", quest,true);
        Question question2 = getCreatedQuestion("question2", quest,true);
        mockMvc.perform(get(setParentId(REST_URL, quest.getId()))
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_QUESTION.contentListMatcher(question1, question2));
    }
}
