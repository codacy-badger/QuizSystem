package ru.dgi.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import ru.dgi.matcher.ModelMatcher;
import ru.dgi.model.Role;

import javax.annotation.PostConstruct;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class AbstractServiceTest {
    protected static final String ADMIN_NAME = "smahrov";

    protected static final ModelMatcher<Role> MATCHER_QUEST = ModelMatcher.of(Role.class);

    protected MockMvc mockMvc;

    @Autowired
    private FilterChainProxy filterChainProxy;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    public void setup() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext)
                .dispatchOptions(true)
                .addFilters(filterChainProxy)
                .build();
    }
}
