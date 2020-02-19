package ru.dgi.controller.quest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.dgi.controller.AbstractControllerTest;
import ru.dgi.model.*;
import ru.dgi.util.json.JsonUtil;
import ru.dgi.util.TestUtil;
import java.util.Arrays;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminQuestRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL =  AdminQuestRestController.REST_URL + '/';

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
    public void testGet() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        ResultActions action = mockMvc.perform(get(REST_URL + quest.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_QUEST.contentMatcher(quest));
    }

    @Test
    public void testGetNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + "0")
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    public void testGetAll() throws Exception {
        Quest quest1 = getCreatedQuest("quest1",  true);
        Quest quest2 = getCreatedQuest("quest2",  true);
        Quest quest3 = getCreatedQuest("quest3",  true);
        TestUtil.print(mockMvc.perform(get(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)))
                .andExpect(MATCHER_QUEST.contentListMatcher((Arrays.asList(quest1, quest2, quest3))));
    }

    @Test
    public void saveQuestCascadeChilds() throws Exception {
        Quest created = getCreatedQuest("quest1", false);

        Question question1 = getCreatedQuestion("question1", null, false);
        Variant variant11 = getCreatedVariant("variant11", false);
        Variant variant12 = getCreatedVariant("variant12", false);
        question1.setAnswerTypeId(0);
        question1.setVariants(Arrays.asList(variant11, variant12));

        Question question2 = getCreatedQuestion("question2", null, false);
        Variant variant21 = getCreatedVariant("variant21", false);
        Variant variant22 = getCreatedVariant("variant22", false);
        question2.setAnswerTypeId(0);
        question2.setVariants(Arrays.asList(variant21, variant22));

        created.setQuestions(Arrays.asList(question1, question2));

        ResultActions action = mockMvc.perform(post(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN)
                .content(JsonUtil.writeValue(created))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Quest returned = MATCHER_QUEST.fromJsonAction(action);
        created.setId(returned.getId());
        MATCHER_QUEST.assertEquals(created, returned);
    }

    @Test
    public void saveQuestCascadeChildsWithDublicateVariants() throws Exception {
        Quest created = getCreatedQuest("quest1", false);

        Question question1 = getCreatedQuestion("question1", null, false);
        Variant variant11 = getCreatedVariant("variant11", false);
        Variant variant12 = getCreatedVariant("variant12", false);
        question1.setAnswerTypeId(0);
        question1.setVariants(Arrays.asList(variant11, variant12));

        Question question2 = getCreatedQuestion("question2", null, false);
        Variant variant21 = getCreatedVariant("variant21", false);
        Variant variant22 = getCreatedVariant("variant22", false);
        question2.setAnswerTypeId(0);
        question2.setVariants(Arrays.asList(variant21, variant22));

        created.setQuestions(Arrays.asList(question1, question2));
        questService.save(created);

        // saving Quest with dubliate variants

        Quest created2 = getCreatedQuest("quest2", false);

        Question question3 = getCreatedQuestion("question3", null, false);
        Variant variant31 = getCreatedVariant("variant31", false);
        Variant variant32 = getCreatedVariant("variant32", false);
        question3.setAnswerTypeId(0);
        question3.setVariants(Arrays.asList(variant31, variant32));

        Question question4 = getCreatedQuestion("question4", null, false);
        Variant variant11Duplicate = getCreatedVariant("variant11", false);
        Variant variant12Duplicate = getCreatedVariant("variant12", false);
        question4.setAnswerTypeId(0);
        question4.setVariants(Arrays.asList(variant11Duplicate, variant12Duplicate));

        created2.setQuestions(Arrays.asList(question3, question4));

        mockMvc.perform(post(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN)
                .content(JsonUtil.writeValue(created2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Assert.assertEquals(6, variantService.getAll().size());
    }

    @Test
    public void testCreate() throws Exception {
        Quest created = getCreatedQuest("quest1", false);
        ResultActions action = mockMvc.perform(post(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN)
                .content(JsonUtil.writeValue(created))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Quest returned = MATCHER_QUEST.fromJsonAction(action);
        created.setId(returned.getId());
        MATCHER_QUEST.assertEquals(created, returned);
    }

    @Test
    public void testUpdate() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        quest.setName("quest1 updated");
        ResultActions action = mockMvc.perform(post(REST_URL)
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN)
                .content(JsonUtil.writeValue(quest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Quest returned = MATCHER_QUEST.fromJsonAction(action);
        MATCHER_QUEST.assertEquals(quest, returned);
    }

    @Test
    public void testDelete() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        mockMvc.perform(delete(REST_URL + quest.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andExpect(status().isOk());
        MATCHER_QUEST.assertCollectionEquals(Collections.emptyList(), questService.getAll());
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete(REST_URL + "0")
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void deleteEntireQuest() throws Exception {
        Quest quest = getCreatedQuest("quest1", false);

        Question question1 = getCreatedQuestion("question1", null, false);
        Variant variant11 = getCreatedVariant("variant11", false);
        Variant variant12 = getCreatedVariant("variant12", false);
        question1.setAnswerTypeId(0);
        question1.setVariants(Arrays.asList(variant11, variant12));

        Question question2 = getCreatedQuestion("question2", null, false);
        Variant variant21 = getCreatedVariant("variant21", false);
        Variant variant22 = getCreatedVariant("variant22", false);
        question2.setAnswerTypeId(0);
        question2.setVariants(Arrays.asList(variant21, variant22));

        quest.setQuestions(Arrays.asList(question1, question2));
        questService.save(quest);

        mockMvc.perform(delete(REST_URL + "entire/" + quest.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN))
                .andExpect(status().isOk());
        MATCHER_QUEST.assertCollectionEquals(Collections.emptyList(), questService.getAll());
    }

    @Test
    public void testEnableNotQuestions() throws Exception {
        Quest quest = getCreatedQuest("quest1", true);
        mockMvc.perform(post(REST_URL + quest.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN)
                .param("isActive", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testEnable() throws Exception {
        Quest quest = getCreatedQuest("quest1", false);
        Question question = getCreatedQuestion("question1", quest, false);
        quest.setQuestions(Arrays.asList(question));
        questService.save(quest);
        mockMvc.perform(post(REST_URL + quest.getId())
                .header(HttpHeaders.AUTHORIZATION, HTTP_AUTH_ADMIN)
                .param("isActive", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        quest.setActive(true);
        Quest returned = questService.get(quest.getId());
        MATCHER_QUEST.assertEquals(quest, returned);
        questService.deleteEntireQuest(quest.getId());

    }
}
