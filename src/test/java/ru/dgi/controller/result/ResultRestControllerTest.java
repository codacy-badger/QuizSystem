package ru.dgi.controller.result;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.dgi.controller.AbstractControllerTest;
import ru.dgi.model.*;
import ru.dgi.util.json.JsonUtil;
import java.util.Arrays;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResultRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ResultRestController.REST_URL + '/';

    @Test
    public void testGetUnAuth() throws Exception {
        mockMvc.perform(get(setParentId(REST_URL, 0) + 0))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getResultByQuestIdAndUsernameTest() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        Result result = getCreatedResult(USER_NAME, quest, true);
        mockMvc.perform(get(setParentId(REST_URL, quest.getId()))
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_RESULT.contentMatcher(result));
    }

    @Test
    public void saveEntireResult() throws Exception {
        Quest quest = getCreatedActiveQuest("quest1", true);

        Question question1 = getCreatedQuestion("question1", quest, false);
        Variant variant11 = getCreatedVariant("variant11", true);
        Variant variant12 = getCreatedVariant("variant12", true);
        question1.setAnswerTypeId(0);
        question1.setVariants(Arrays.asList(variant11, variant12));
        questionService.save(question1);

        Question question2 = getCreatedQuestion("question1", quest, false);
        Variant variant21 = getCreatedVariant("variant21", true);
        Variant variant22 = getCreatedVariant("variant22", true);
        question2.setAnswerTypeId(0);
        question2.setVariants(Arrays.asList(variant21, variant22));
        questionService.save(question2);

        Result created = getCreatedResult(USER_NAME, quest, false);
        Answer answer1 = getCreatedAnswer(created, question1, variant11, false);
        Answer answer2 = getCreatedAnswer(created, question2, variant21, false);
        created.setAnswers(Arrays.asList(answer1, answer2));

        ResultActions action = mockMvc.perform(post(setParentId(REST_URL, quest.getId()) + "entire/")
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN)
                .content(JsonUtil.writeValue(created))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Result returned = MATCHER_RESULT.fromJsonAction(action);
        created.setId(returned.getId());
        MATCHER_RESULT.assertEquals(created, returned);


    }
}
