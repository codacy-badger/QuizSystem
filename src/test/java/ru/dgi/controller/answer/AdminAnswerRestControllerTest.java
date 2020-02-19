package ru.dgi.controller.answer;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ru.dgi.controller.AbstractControllerTest;
import ru.dgi.model.Answer;
import ru.dgi.model.Result;
import ru.dgi.model.Quest;
import ru.dgi.model.Question;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminAnswerRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminAnswerRestController.REST_URL + '/';

    @Test
    public void getOneMoreThanId() throws Exception {
        Quest quest = getCreatedActiveQuest("quest1", true);

        Question question = getCreatedQuestion("question", quest, false);
        question.setAnswerTypeId(1);
        questionService.save(question);

        Result result1 = getCreatedResult(ADMIN_NAME, quest, true);
        Answer answer1 = getCreatedAnswer(result1, question, null, false);
        answer1.setAnswerText("answerText1");
        answerService.save(answer1);

        Result result2 = getCreatedResult(USER_NAME, quest, true);
        Answer answer2 = getCreatedAnswer(result2, question, null, false);
        answer2.setAnswerText("answerText2");
        answerService.save(answer2);

        mockMvc.perform(get(setParentId(REST_URL, question.getId()) + "getonemorethanid/" + answer1.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_ANSWER.contentMatcher(answer2));

    }
}
