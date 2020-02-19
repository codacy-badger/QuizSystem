package ru.dgi.controller.quest;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ru.dgi.controller.AbstractControllerTest;
import ru.dgi.model.Quest;
import ru.dgi.model.Result;
import ru.dgi.util.TestUtil;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QuestRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL =  QuestRestController.REST_URL + '/';

    @Test
    public void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetActive() throws Exception {
        Quest quest = getCreatedActiveQuest("quest1", true);
        mockMvc.perform(get(REST_URL + quest.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_QUEST.contentMatcher(quest));
    }

    @Test
    public void testGetActiveNotFound() throws Exception {
        Quest quest = getCreatedQuest("quest1",  false);
        mockMvc.perform(get(REST_URL + quest.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    public void testGetAllActive() throws Exception {
        Quest quest1 = getCreatedActiveQuest("quest1",  true);
        Quest quest2 = getCreatedQuest("quest2",  true);
        Quest quest3 = getCreatedActiveQuest("quest3",  true);
        TestUtil.print(mockMvc.perform(get(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)))
                .andExpect(MATCHER_QUEST.contentListMatcher((Arrays.asList(quest1, quest3))));
    }

    @Test
    public void testGetAllStarted() throws Exception {
        Quest quest1 = getCreatedQuest("quest1Started",  true);
        Quest quest2 = getCreatedQuest("quest2NotStarted",  true);
        Quest quest3 = getCreatedQuest("quest3Started",  true);
        Result result1 = getCreatedResult(USER_NAME, quest1, true);
        Result result3 = getCreatedResult(USER_NAME, quest3, true);
        TestUtil.print(mockMvc.perform(get(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)))
                .andExpect(MATCHER_QUEST.contentListMatcher((Arrays.asList(quest1, quest3))));
    }

    @Test
    public void testGetAllActiveOrStarted() throws Exception {
        Quest quest1 = getCreatedQuest("quest1",  true);
        Quest quest2 = getCreatedActiveQuest("quest2",  true);
        Quest quest3 = getCreatedQuest("quest3",  true);
        Result result1 = getCreatedResult(USER_NAME, quest1, true);
        Result result3 = getCreatedResult(USER_NAME, quest3, true);
        TestUtil.print(mockMvc.perform(get(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)))
                .andExpect(MATCHER_QUEST.contentListMatcher(Arrays.asList(quest1, quest2, quest3)));
    }

}
