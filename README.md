# study-spring-boot-board

# [following Spring boot board edu streaming by 우종선](https://www.youtube.com/channel/UCdShL6X4Ac5xlLouOhRvfvg)

## study curriculum
1. [Join Controller](https://youtu.be/8K5bSFKAhf8)
 * Create package, class, interface, html for join function

## record

## 2019 / 02 / 24 Sun

* Create Spring Starter Project ()
    ```
    > project name : exampleProject
    > type : Gradle (Buildship 3.x)
    > package : com.example
    > Dependencies
        > Web
        > Thymeleaf
        > JPA
        > MySQL
    ```
* Create controller package
  * MainController.java (page mapper)
    ```java
    @Controller
    public class MainController {

        @RequestMapping(value="/")
        public String index() {
            return "index";
        }
        
        @RequestMapping(value="/joinPage")
        public String joinPage() {
            return "join";
        }
    }
    ```
* Create index.html
  * Temporary page move link
    ```html
    <body>
        <a href="http://localhost:8080/joinPage">가입 페이지로</a><br/><br/>
        <a href="http://localhost:8080/loginPage">로그인 페이지로</a><br/><br/>
        <a href=""></a><br/><br/>
    </body>
    ```
* Create join.html
  * Join post form
    ```html
    <body>
        <form action="http://localhost:8080/joinRequest">
            아이디: <input type="text" name="user_id"> 
            비밀번호: <input type="password" name="user_pw"> 
            이름: <input type="text" name="user_name">
            <input type="submit" value="가입하기">
        </form>
    </body>
    ```
* Create model package
  * Users.java (User info object)
    ```java
    @Entity
    @Table(name = "users")
    public class Users {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String user_id;
        private String user_pw;
        private String user_name;

        getter/setter
    }
    ```
* Create repository package
  * UsersRepository.java (Extends JpaRepository<Users, Long>)
    ```java
    public interface UsersRepository extends JpaRepository<Users, Long>{

    }
    ```
* Create controller - UsersController.java (request process)
    ```java
    public class UsersController {

        private UsersRepository usersRepository;
        
        @PostMapping(value="/joinRequest")
        public String joinRequest(HttpServletRequest request) {
            JoinService joinService = new JoinService();
            joinService.joinUser(request, usersRepository);
            
            return "index";
        }
        
        @PostMapping(value="/loginRequest")
        public String loginRequest() {
            return "index";
        }
    }
    ```
* Create service package
  * JoinService.java (Create user object & Save)
    ```java
    public class JoinService {
        public void joinUser(HttpServletRequest request, UsersRepository usersRepository) {
            String userId = request.getParameter("user_id");
            String userPw = request.getParameter("user_password");
            String userName = request.getParameter("user_name");
            
            Users users = new Users();
            users.setUser_id(userId);
            users.setUser_pw(userPw);
            users.setUser_name(userName);
            
            usersRepository.save(users);
        }
    }
    ```
* Add application.properties
    ```
    spring.jpa.hibernate.ddl-auto=create
    spring.datasource.url=jdbc:mysql://localhost:3306/pilot
    spring.datasource.username=root
    spring.datasource.password=root
    ```