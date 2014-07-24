package com.softserveinc.ita.mvc.impl;

import com.softserveinc.ita.entity.Applicant;
import com.softserveinc.ita.entity.Course;
import com.softserveinc.ita.entity.Group;
import com.softserveinc.ita.mvc.MvcGroupBaseTest;
import com.softserveinc.ita.utils.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public class MvcGroupTests extends MvcGroupBaseTest {

    private MockMvc mockMvc;

    @Autowired
    private JsonUtil jsonUtil;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void testGetGroupsByStatusAndExpectIsOk() throws Exception {
        mockMvc.perform(
                get("/BOARDING")
        )
                .andExpect(status().isOk());
    }

    @Test
    public void testGetGroupsAndExpectJsonType() throws Exception {
        mockMvc.perform(
                get("/BOARDING")
        )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetCoursesAndExpectIsOk() throws Exception {
        mockMvc.perform(
                get("/courses")
        )
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCoursesAndExpectJsonType() throws Exception {
        mockMvc.perform(
                get("/courses")
        )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testPostGroupAndExpectJsonType() throws Exception {
        Group group = new Group("3");
        group.setCourse(new Course("Java"));
        String jsonGroup = jsonUtil.toJson(group);
        mockMvc.perform(post("/addGroup")
                .content(jsonGroup)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllGroupsAndExpectIsOk() throws Exception {
        mockMvc.perform(
                get("/allGroups")
        )
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllGroupsAndExpectJsonType() throws Exception {
        mockMvc.perform(
                get("/allGroups")
        )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetApplicantsByGroupIDAndExpectOk() throws Exception {
        mockMvc.perform(
                get("/applicants/TestGroupID")
        )
                .andExpect(status().isOk());
    }

    @Test
    public void testGetApplicantsByGroupIDAndExpectValidJson() throws Exception {
        List<Applicant> applicants = new ArrayList<>();
        Applicant applicantOne = new Applicant("TestApplicantOneName", "TestApplicantOneSurname");
        Applicant applicantTwo = new Applicant("TestApplicantTwoName", "TestApplicantTwoSurname");
        Applicant applicantThree = new Applicant("TestApplicantThreeName", "TestApplicantThreeSurname");
        Collections.addAll(applicants, applicantOne, applicantTwo, applicantThree);
        String expected = jsonUtil.toJson(applicants);

        mockMvc.perform(
                get("/applicants/TestGroupID")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    public void testGetApplicantsByGroupIDAndExpectMediaTypeJson() throws Exception {
        List<Applicant> applicants = new ArrayList<>();
        Applicant applicantOne = new Applicant("TestApplicantOneName", "TestApplicantOneSurname");
        Applicant applicantTwo = new Applicant("TestApplicantTwoName", "TestApplicantTwoSurname");
        Applicant applicantThree = new Applicant("TestApplicantThreeName", "TestApplicantThreeSurname");
        Collections.addAll(applicants, applicantOne, applicantTwo, applicantThree);
        String expected = jsonUtil.toJson(applicants);

        mockMvc.perform(
                get("/applicants/TestGroupID")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetApplicantsByWrongGroupIdAndExpectInternalServerError() throws Exception{
        mockMvc.perform(
                get("/applicants/wrongId")
        ).andExpect(status().isInternalServerError());

    }
}

