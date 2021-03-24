package com.lambdaschool.usermodel.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.UserModelApplicationTesting;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.repository.RoleRepository;
import com.lambdaschool.usermodel.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplicationTesting.class,
        properties = {
                "command.line.runner.enabled=false"})
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
public class UserServiceImplTestNoDB {

   @Autowired
   UserService userService;


   @MockBean
   UserRepository userRepository;

   @MockBean
   RoleRepository roleRepository;

   private List<User> userList;
   private List<Role> roleList = new ArrayList<>();

   @Before
   public void setUp() throws Exception {

      userList =  new ArrayList<>();

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

      MockitoAnnotations.initMocks(this);
   }

   @After
   public void tearDown() throws Exception {
   }

   @Test
   public void findUserById() {
      Mockito.when(userRepository.findById(10L)).thenReturn(Optional.of(userList.get(0)));

      assertEquals("admin",userService.findUserById(10).getUsername());

   }

   @Test
   public void findByNameContaining() {

      Mockito.when(userRepository.findByUsernameContainingIgnoreCase("kitt")).thenReturn((userList.subList(4,5)));

      assertEquals("misskitty",userService.findByNameContaining("kitt").get(0).getUsername());

   }

   @Test
   public void findAll() {
      Mockito.when(userRepository.findAll()).thenReturn(userList);

      assertEquals(5,userService.findAll().size());

   }

   @Test
   public void delete() {


//      System.out.println(userRepository.findAll().toString());
//
      Mockito.when(userRepository.findById(10L))
              .thenReturn(Optional.of(userList.get(0)));

      Mockito.doNothing()
              .when(userRepository)
              .deleteById(10L);

      userService.delete(10);
//      userList.remove(4);
//      System.out.println(userService.findAll().get(0).getUserid());
      assertEquals(5,
              userList.size());

   }

   @Test
   public void findByName() {

      Mockito.when(userRepository.findByUsername("misskitty")).thenReturn(userList.get(4));

      assertEquals("misskitty",userService.findByName("misskitty").getUsername());

   }

   @Test
   public void save() throws JsonProcessingException {
      var test = new User("mojo", "Coffee123", "mojo@lambdaschool.local");
      test.setUserid(100);

      test.getRoles().add(new UserRoles(test, roleList.get(1)));


      Mockito.when(userRepository.findById(100L))
              .thenReturn(Optional.of(test));
      Mockito.when(roleRepository.findById(1L))
              .thenReturn(Optional.of(roleList.get(0)));
      Mockito.when(roleRepository.findById(2L))
              .thenReturn(Optional.of(roleList.get(1)));
      Mockito.when(roleRepository.findById(3L))
              .thenReturn(Optional.of(roleList.get(2)));


      Mockito.when(userRepository.save(any(User.class)))
              .thenReturn(test);

      assertEquals(100L,
              userService.save(test)
                      .getUserid());

   }

   @Test
   public void update() throws Exception {


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

//      System.out.println(roleList.get(0).getRoleid());


      ObjectMapper objectMapper = new ObjectMapper();
      User newuser = objectMapper
              .readValue(objectMapper.writeValueAsString(u1), User.class);

      Mockito.when(userRepository.save(any(User.class)))
              .thenReturn(u1);
//
         Mockito.when(userRepository.findById(10L))
                 .thenReturn(Optional.of(newuser));
         Mockito.when(roleRepository.findById(1L))
                 .thenReturn(Optional.of(roleList.get(0)));
         Mockito.when(roleRepository.findById(2L))
                 .thenReturn(Optional.of(roleList.get(1)));
         Mockito.when(roleRepository.findById(3L))
                 .thenReturn(Optional.of(roleList.get(2)));



      User updated = userService.update(u1,
              10);

      assertNotNull(updated);
      assertEquals("mojo",
              updated.getUsername());
   }

   @Test
   public void deleteAll() {

      Mockito.doNothing().when(userRepository).deleteAll();

      userService.deleteAll();
      assertEquals(5,userList.size());


   }
}