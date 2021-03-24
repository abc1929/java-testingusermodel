package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.UserModelApplicationTesting;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.RoleService;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = UserModelApplicationTesting.class, properties = {"command.line.runner.enabled=false"})
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
@AutoConfigureMockMvc
public class UserControllerTestNoDB {


   @Autowired
   private WebApplicationContext webApplicationContext;


   private MockMvc mockMvc;


   private List<User> userList = new ArrayList<>();
   private List<Role> roleList = new ArrayList<>();


   @MockBean
   UserService userService;

   @MockBean
   RoleService roleService;


   @Before
   public void setUp() throws Exception {


      Role r1 = new Role("admin");
      Role r2 = new Role("user");
      Role r3 = new Role("data");

      r1.setRoleid(1);
      r2.setRoleid(2);
      r3.setRoleid(3);

      roleList.add(r1);
      roleList.add(r2);
      roleList.add(r3);

      // admin, data, user
      User u1 = new User("admin",
              "password",
              "admin@lambdaschool.local");
      u1.setUserid(10);
      u1.getRoles()
              .add(new UserRoles(u1,
                      r1));
      u1.getRoles()
              .add(new UserRoles(u1,
                      r2));
      u1.getRoles()
              .add(new UserRoles(u1,
                      r3));
      u1.getUseremails()
              .add(new Useremail(u1,
                      "admin@email.local"));
      u1.getUseremails()
              .add(new Useremail(u1,
                      "admin@mymail.local"));

      u1.getUseremails().get(0).setUseremailid(11);
      u1.getUseremails().get(1).setUseremailid(12);

      userList.add(u1);

      // data, user
      User u2 = new User("cinnamon",
              "1234567",
              "cinnamon@lambdaschool.local");
      u2.setUserid(20);
      u2.getRoles()
              .add(new UserRoles(u2,
                      r2));
      u2.getRoles()
              .add(new UserRoles(u2,
                      r3));
      u2.getUseremails()
              .add(new Useremail(u2,
                      "cinnamon@mymail.local"));
      u2.getUseremails()
              .add(new Useremail(u2,
                      "hops@mymail.local"));
      u2.getUseremails()
              .add(new Useremail(u2,
                      "bunny@email.local"));

      u2.getUseremails().get(0).setUseremailid(21);
      u2.getUseremails().get(1).setUseremailid(22);
      u2.getUseremails().get(2).setUseremailid(23);

      userList.add(u2);

      // user
      User u3 = new User("barnbarn",
              "ILuvM4th!",
              "barnbarn@lambdaschool.local");
      u3.setUserid(30);
      u3.getRoles()
              .add(new UserRoles(u3,
                      r2));
      u3.getUseremails()
              .add(new Useremail(u3,
                      "barnbarn@email.local"));
      u3.getUseremails().get(0).setUseremailid(31);
      userList.add(u3);

      User u4 = new User("puttat",
              "password",
              "puttat@school.lambda");
      u4.setUserid(40);
      u4.getRoles()
              .add(new UserRoles(u4,
                      r2));
      userList.add(u4);

      User u5 = new User("misskitty",
              "password",
              "misskitty@school.lambda");
      u5.setUserid(50);
      u5.getRoles()
              .add(new UserRoles(u5,
                      r2));
      userList.add(u5);

      RestAssuredMockMvc.webAppContextSetup(webApplicationContext);


      mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(SecurityMockMvcConfigurers.springSecurity()).build();

   }

   @After
   public void tearDown() throws Exception {
   }

   @Test
   public void listAllUsers() throws Exception {
      String apiURL = "/users/users";

      Mockito.when(userService.findAll()).thenReturn(userList);


      RequestBuilder rb = MockMvcRequestBuilders.get(apiURL).accept(MediaType.APPLICATION_JSON);
      MvcResult r = mockMvc.perform(rb).andReturn();

      String tr = r.getResponse().getContentAsString();


      ObjectMapper mapper = new ObjectMapper();

      String er = mapper.writeValueAsString(userList);

      System.out.println(er);
      assertEquals(er, tr);

   }

   @Test
   public void getUserById() throws Exception {
      String apiURL = "/users/user/10";

      Mockito.when(userService.findUserById(10)).thenReturn(userList.get(0));


      RequestBuilder rb = MockMvcRequestBuilders.get(apiURL).accept(MediaType.APPLICATION_JSON);
      MvcResult r = mockMvc.perform(rb).andReturn();

      String tr = r.getResponse().getContentAsString();


      ObjectMapper mapper = new ObjectMapper();

      String er = mapper.writeValueAsString(userList.get(0));

      System.out.println(er);
      assertEquals(er, tr);


   }

   @Test
   public void getUserByName() throws Exception {

      String apiURL = "/users/user/name/admin";

      Mockito.when(userService.findByName("admin")).thenReturn(userList.get(0));


      RequestBuilder rb = MockMvcRequestBuilders.get(apiURL).accept(MediaType.APPLICATION_JSON);
      MvcResult r = mockMvc.perform(rb).andReturn();

      String tr = r.getResponse().getContentAsString();


      ObjectMapper mapper = new ObjectMapper();

      String er = mapper.writeValueAsString(userList.get(0));

      System.out.println(er);
      assertEquals(er, tr);
   }

   @Test
   public void getUserLikeName() throws Exception {

      String apiURL = "/users/user/name/like/kitt";

      Mockito.when(userService.findByNameContaining("kitt")).thenReturn(userList);


      RequestBuilder rb = MockMvcRequestBuilders.get(apiURL).accept(MediaType.APPLICATION_JSON);
      MvcResult r = mockMvc.perform(rb).andReturn();

      String tr = r.getResponse().getContentAsString();


      ObjectMapper mapper = new ObjectMapper();

      String er = mapper.writeValueAsString(userList);

      System.out.println(er);
      assertEquals(er, tr);

   }

   @Test
   public void addNewUser() throws Exception {
      String apiURL = "/users/user";

      var test = new User("mojo", "Coffee123", "mojo@lambdaschool.local");
      test.setUserid(100);

      test.getRoles().add(new UserRoles(test, roleList.get(1)));
      ObjectMapper mapper = new ObjectMapper();

      String testString = mapper.writeValueAsString(test);

      Mockito.when(userService.save(any(User.class))).thenReturn(test);


      RequestBuilder rb = MockMvcRequestBuilders.post(apiURL).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
              .content(testString);
//      MvcResult r = mockMvc.perform(rb).andReturn();

      mockMvc.perform(rb)
              .andExpect(status().isCreated())
              .andDo(MockMvcResultHandlers.print());


   }

   @Test
   public void updateFullUser() throws Exception {


      String apiUrl = "/users/user/20";

      var test = new User("mojo", "Coffee123", "mojo@lambdaschool.local");
      test.setUserid(20);

      test.getRoles().add(new UserRoles(test, roleList.get(1)));

      Mockito.when(userService.update(test,
              20L))
              .thenReturn(test);
      ObjectMapper mapper = new ObjectMapper();
      String testString = mapper.writeValueAsString(test);

      RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl,
              20L)
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON)
              .content(testString);

      mockMvc.perform(rb)
              .andExpect(status().isOk())
              .andDo(MockMvcResultHandlers.print());

   }

   @Test
   public void updateUser() throws Exception {

      String apiUrl = "/users/user/10";

      User u1 = new User("mojo",
              "password",
              "admin@lambdaschool.local");
      u1.setUserid(10);
      u1.getRoles()
              .add(new UserRoles(u1,
                      roleList.get(0)));
      u1.getRoles()
              .add(new UserRoles(u1,
                      roleList.get(1)));
      u1.getRoles()
              .add(new UserRoles(u1,
                      roleList.get(2)));
      u1.getUseremails()
              .add(new Useremail(u1,
                      "admin@email.local"));
      u1.getUseremails()
              .add(new Useremail(u1,
                      "admin@mymail.local"));

      u1.getUseremails().get(0).setUseremailid(11);
      u1.getUseremails().get(1).setUseremailid(12);


      var test = new User();
      test.setUsername("mojo");
      test.setUserid(10L);



      Mockito.when(userService.update(test,
              10L))
              .thenReturn(u1);
//      ObjectMapper mapper = new ObjectMapper();
//      String testString = mapper.writeValueAsString(test);
//      System.out.println(testString);

      RequestBuilder rb = MockMvcRequestBuilders.patch(apiUrl,
              10L)
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON)
              .content("{\"userid\":10,\"username\":\"mojo\"}");

      mockMvc.perform(rb)
              .andExpect(status().isOk())
              .andDo(MockMvcResultHandlers.print());

   }

   @Test
   public void deleteUserById() throws Exception {
      String apiURL = "/users/user/20";

      ObjectMapper mapper = new ObjectMapper();



      RequestBuilder rb = MockMvcRequestBuilders.delete(apiURL).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
//      MvcResult r = mockMvc.perform(rb).andReturn();
      mockMvc.perform(rb)
              .andExpect(status().isOk())
              .andDo(MockMvcResultHandlers.print());


   }

   @Test
   public void getCurrentUserInfo() throws Exception {

      String apiURL = "/users/getuserinfo";

      Mockito.when(userService.findByName("admin")).thenReturn(userList.get(0));


      RequestBuilder rb = MockMvcRequestBuilders.get(apiURL).accept(MediaType.APPLICATION_JSON);
      MvcResult r = mockMvc.perform(rb).andReturn();

      String tr = r.getResponse().getContentAsString();


      ObjectMapper mapper = new ObjectMapper();

      String er = mapper.writeValueAsString(userList.get(0));

      System.out.println(er);
      assertEquals(er, tr);

   }
}