package com.mine.ranking.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Required setup code for MockMvc tests.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class MockMvcBase
{

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Before
    public void setUp()
    {
        this.mockMvc = webAppContextSetup(context).build();
    }
}