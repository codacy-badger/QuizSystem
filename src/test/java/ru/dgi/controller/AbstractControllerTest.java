package ru.dgi.controller;

import org.springframework.security.web.FilterChainProxy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import ru.dgi.matcher.ModelMatcher;
import ru.dgi.model.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import ru.dgi.service.*;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public abstract class AbstractControllerTest {

    protected static final String ADMIN_NAME = "admin";
    protected static final String HTTP_AUTH_ADMIN = "Basic " + Base64Utils.encodeToString("admin:admin".getBytes());
    protected static final String USER_NAME = "user";
    protected static final String HTTP_AUTH_USER = "Basic " + Base64Utils.encodeToString("user:user".getBytes());

    protected static final ModelMatcher<Answer> MATCHER_ANSWER = ModelMatcher.of(Answer.class);
    protected static final ModelMatcher<Result> MATCHER_RESULT = ModelMatcher.of(Result.class);
    protected static final ModelMatcher<Variant> MATCHER_VARIANT = ModelMatcher.of(Variant.class);
    protected static final ModelMatcher<Question> MATCHER_QUESTION = ModelMatcher.of(Question.class);
    protected static final ModelMatcher<Quest> MATCHER_QUEST = ModelMatcher.of(Quest.class);

    protected MockMvc mockMvc;

    @Autowired
    private FilterChainProxy filterChainProxy;

    @Autowired
    protected AnswerService answerService;

    @Autowired
    protected ResultService resultService;

    @Autowired
    protected VariantService variantService;

    @Autowired
    protected QuestionService questionService;

    @Autowired
    protected QuestService questService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    public void setup() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext)
                .dispatchOptions(true)
                .addFilters(filterChainProxy)
                .build();
    }

    public String setParentId(String restUrl, Integer parentId) {
        String parentSubstr = restUrl.substring(restUrl.indexOf("{"), restUrl.indexOf("}") + 1);
        return restUrl.replace(parentSubstr, parentId.toString());
    }

    public Quest getCreatedQuest(String name, Boolean saveToDb) {
        Quest quest = new Quest();
        quest.setName(name);
        if (saveToDb) questService.save(quest);
        return quest;
    }

    public Quest getCreatedActiveQuest(String name, Boolean saveToDb) {
        Quest quest = new Quest();
        quest.setName(name);
        quest.setActive(true);
        if (saveToDb) questService.save(quest);
        return quest;
    }

    public Question getCreatedQuestion(String name, Quest quest, Boolean saveToDb) {
        Question question = new Question();
        question.setName(name);
        question.setQuest(quest);
        if (saveToDb) questionService.save(question);
        return question;
    }

    public Variant getCreatedVariant(String name, Boolean saveToDb) {
        Variant variant = new Variant();
        variant.setName(name);
        if (saveToDb) variantService.save(variant);
        return variant;
    }

    public Result getCreatedResult(String userName, Quest quest, Boolean saveToDb) {
        Result result = new Result(quest, userName, "John Doe", LocalDateTime.now(), null, Status.UNCOMPLETED);
        if (saveToDb) resultService.save(result);
        return result;
    }

    public Answer getCreatedAnswer(Result result, Question question, Variant variant, Boolean saveToDb) {
        Answer answer = new Answer();
        answer.setResult(result);
        answer.setQuestion(question);
        answer.setVariant(variant);
        if (saveToDb) answerService.save(answer);
        return answer;
    }
}
