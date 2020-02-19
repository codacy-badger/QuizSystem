package ru.dgi.controller.answer;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.dgi.controller.AbstractControllerTest;
import ru.dgi.util.json.JsonUtil;
import ru.dgi.model.*;
import ru.dgi.util.TestUtil;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AnswerRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AnswerRestController.REST_URL + '/';

    @Test
    public void testGetAllUnAuth() throws Exception {
        mockMvc.perform(get(setParentId(REST_URL, 0)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetAll() throws Exception {
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

        Result result = getCreatedResult(USER_NAME, quest, true);
        Answer answer1 = getCreatedAnswer(result, question1, variant11, true);
        Answer answer2 = getCreatedAnswer(result, question2, variant21, true);

        TestUtil.print(mockMvc.perform(get(setParentId(REST_URL, result.getId()))
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)))
                .andExpect(MATCHER_ANSWER.contentListMatcher(Arrays.asList(answer1, answer2)));
    }

        @Test
        public void testCreate() throws Exception {
            Quest quest = getCreatedActiveQuest("quest1", true);
            Question question = getCreatedQuestion("question1", quest, false);
            question.setAnswerTypeId(0);
            Variant variant1 = getCreatedVariant("variant1", true);
            Variant variant2 = getCreatedVariant("variant2", true);
            question.setVariants(Arrays.asList(variant1, variant2));
            questionService.save(question);
            Result result = getCreatedResult(USER_NAME, quest, true);
            Answer created = getCreatedAnswer(result, question, variant1, false);
            ResultActions action = mockMvc.perform(post(setParentId(REST_URL, result.getId()))
                    .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER)
                    .content(JsonUtil.writeValue(created))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            Answer returned = MATCHER_ANSWER.fromJsonAction(action);
            created.setId(returned.getId());
            MATCHER_ANSWER.assertEquals(created, returned);
        }

        @Test
        public void testUpdate() throws Exception {
            Quest quest = getCreatedActiveQuest("quest1", true);
            Question question = getCreatedQuestion("question1", quest, false);
            Variant variant1 = getCreatedVariant("variant1", true);
            Variant variant2 = getCreatedVariant("variant2", true);
            question.setVariants(Arrays.asList(variant1, variant2));
            questionService.save(question);
            Result result = getCreatedResult(USER_NAME, quest, true);
            Answer answer = getCreatedAnswer(result, question, variant1, true);
            answer.setVariant(variant2);
            ResultActions action = mockMvc.perform(post(setParentId(REST_URL, result.getId()))
                    .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER)
                    .content(JsonUtil.writeValue(answer))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            Answer returned = MATCHER_ANSWER.fromJsonAction(action);
            MATCHER_ANSWER.assertEquals(answer, returned);
        }

        @Test
        public void testDelete() throws Exception {
            Quest quest = getCreatedQuest("quest1", true);
            Question question = getCreatedQuestion("question1", quest, false);
            Variant variant1 = getCreatedVariant("variant1", true);
            Variant variant2 = getCreatedVariant("variant2", true);
            question.setVariants(Arrays.asList(variant1, variant2));
            questionService.save(question);
            Result result = getCreatedResult(USER_NAME, quest, true);
            Answer answer = getCreatedAnswer(result, question, variant1, true);
            mockMvc.perform(delete(setParentId(REST_URL, result.getId()) + answer.getId())
                    .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_USER))
                    .andExpect(status().isOk());
            MATCHER_ANSWER.assertCollectionEquals(Collections.emptyList(), answerService.getAll(result.getId()));
        }

}
