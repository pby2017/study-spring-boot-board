# study-spring-boot-board

# [following Spring boot board edu streaming by 우종선](https://www.youtube.com/channel/UCdShL6X4Ac5xlLouOhRvfvg)

## study curriculum
1. [Join controller](https://youtu.be/8K5bSFKAhf8)
 * Create package, class, interface, html for join function
2. [Join JPA database](https://youtu.be/EMUcgCZhMjI)
 * Create database
 * Modify application.properties database
3. [Refactoring & add service](https://youtu.be/sxUGWToMBfI)
 * Refactoring controller and service role
 * Create UserPasswordHashClass using SHA256
 * Create LoginService.java
4. [Design index page](https://youtu.be/ua9YdiCs4xs)
 * Get bootstrap Webpage template

## record

## 2019 / 02 / 25 Mon
* Create database in MySQL workbench
* Anntation
  ```
  Class
  @Service : this is service class ?
  @Controller : this is controller class ?

  Field
  @Autowired : create instance and make object ?
  
  Method
  @RequestMapping : get request from method what action ?
  @PostMapping : get param from method POST action ?

  Parameter
  @RequestParam : get param to parameter value ?
  ```
* Modify application.properties database
    * before execute web
        ```
        spring.datasource.url=jdbc:mysql://localhost:3306/pilot
        ->
        spring.datasource.url=jdbc:mysql://localhost:3306/example?serverTimezone=UTC
        ```
    * after execute web & create database table from JPA(Users.java)
        ```
        spring.jpa.hibernate.ddl-auto=create
        ->
        spring.jpa.hibernate.ddl-auto=validate
        ```
* Refactoring controller and service role
    * Controller only get param and send param to service
        ```java
        @PostMapping("/joinRequest")
        public String joinRequest(@RequestParam Map<String, String> paramMap) {
            String userId = paramMap.get("user_id");
            String userPw = paramMap.get("user_pw");
            String userName = paramMap.get("user_name");

            String page = joinService.joinUser(userId, userPw, userName);

            return page;
        }
        ```
    * Service only get data and process data and save data
        ```java
        public String joinUser(String userId, String userPw, String userName) {

            if (userId.equals("") || userPw.equals("") || userName.equals("")) {
                return "join";
            }

            Users users = new Users();
            users.setUser_id(userId);
            String hashedPassword = userPasswordHashClass.getSHA256(userPw);
            users.setUser_pw(hashedPassword);
            users.setUser_name(userName);

            usersRepository.save(users);
            return "index";
        }
        ```
* Create UserPasswordHashClass using SHA256
    ```java
    public class UserPasswordHashClass {

        public String getSHA256(String plainText) {
            String shaString = "";
            try {
                MessageDigest sh = MessageDigest.getInstance("SHA-256");
                sh.update(plainText.getBytes());
                byte byteData[] = sh.digest();
                StringBuffer stringBuffer = new StringBuffer();
                int byteSize = byteData.length;

                for (int i = 0; i < byteSize; i++) {
                    stringBuffer.append(Integer.toString(byteData[i] & 0xff + 0x100, 16).substring(1));
                }
                shaString = stringBuffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
                shaString = null;
            }
            return shaString;
        }
    }
    ```
* Create LoginService
    * Add ORM query function in UserRepository
        ```java
        public interface UsersRepository extends JpaRepository<Users, Long>{
            public Users findByUser_idAndUser_pw(String userId, String userPw);
        }
        // need modify preference
        // Window - Preference - Spring - Validation - Data Validator - Invalid Derived Query [check off 체크해제]
        ```
    * Create LoginService.java
        ```java
        @Service
        public class LoginService {
            
            @Autowired
            private UserPasswordHashClass userPasswordHashClass;
            
            @Autowired
            private UsersRepository usersRepository;
            
            public String login(String userId, String userPw) {
                if(userId.equals("") || userPw.equals("")) {
                    return "login";
                }
                
                String hashedPassword = userPasswordHashClass.getSHA256(userPw);
                
                Users users = usersRepository.findByUser_idAndUser_pw(userId, hashedPassword);
                if(users == null) {
                    return "login";
                }
                
                return "index";
            }
        }
        ```
    * Modify loginRequest function in UsersController.java
        ```java
        @PostMapping("/loginRequest")
        public String loginRequest(@RequestParam Map<String, String> paramMap) {
            String userId = paramMap.get("user_id");
            String userPw = paramMap.get("user_pw");

            String page = loginService.login(userId, userPw);

            return page;
        }
        ```
* Add thymeleaf in index.html
    ```html
    <!DOCTYPE html>
    <html xmlns:th="http://www.thymeleaf.org">
    ...
    <body>
        ...
        <ul class="nav navbar-nav navbar-right">
            <li th:if='${session.loginUser==null}'><a href="joinPage"><span class="glyphicon glyphicon-log-in"></span> Join</a></li>
            <li th:if='${session.loginUser==null}'><a href="loginPage"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
            <li th:if='${session.loginUser!=null}'><a href="logout"><span class="glyphicon glyphicon-log-in"></span> Logout</a></li>
        </ul>
        ...
    </body>
    ```
* Add thymeleaf in index.html meta tag
    ```html
    <!DOCTYPE html>
    <html xmlns:th="http://www.thymeleaf.org">
    ```
* Modify annotation from @RequestMapping to @GetMapping in MainController.java
    ```java
    @Controller
    public class MainController {

        @GetMapping("/")
        public String index() {
            return "index";
        }
        
        @GetMapping("/joinPage")
        public String joinPage() {
            return "join";
        }
        
        @GetMapping("/loginPage")
        public String loginPage() {
            return "login";
        }
        
        @GetMapping("/logout")
        public String logout() {
            return "logout";
        }
    }
    ```
* Modify JPA ORM query method
    * from AndAnd to And but didn't work
        ```java
        public interface UsersRepository extends JpaRepository<Users, Long>{
            public Users findByUser_idAndUser_pw(String userId, String userPw);
        }
        ```
    * so remodify user_id to userId (camel case) then did work
        ```java
        public interface UsersRepository extends JpaRepository<Users, Long>{
            public Users findByUserIdAndUserPw(String userId, String userPw);
        }
        ```
* Add session remain function
    * Add HttpSession in LoginService.java
        ```java
        @Service
        public class LoginService {

            ...
            @Autowired
            HttpSession session;

            public String login(String userId, String userPw) {
                ...
                
                session.setAttribute("userId", userId);

                return "index";
            }
        }
        ```
    * Check th:if {userId} keyword in index.html
        ```html
        <ul class="nav navbar-nav navbar-right">
            <li th:if='${session.userId==null}'><a href="joinPage"><span class="glyphicon glyphicon-log-in"></span> Join</a></li>
            <li th:if='${session.userId==null}'><a href="loginPage"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
            <li th:if='${session.userId!=null}'><a href="logout"><span class="glyphicon glyphicon-log-in"></span> Logout</a></li>
        </ul>
        ```
* Create session logout
    * Check logout button in index.html
        ```html
        <li th:if='${session.userId!=null}'><a href="/logoutPage"><span class="glyphicon glyphicon-log-in"></span> Logout</a></li>
        ```
    * Add logout page function in MainController.java
        ```java
        @Controller
        public class MainController {
            ...                        
            @GetMapping("/logoutPage")
            public String logoutPage() {
                return "logout";
            }
        }
        ```
    * Create logout.html from join.html copy
        ```html
        <body>
            <form action="logoutRequest" method="post">
                <input type="submit" value="로그아웃">
            </form>
        </body>
        ```
    * Add logoutRequest funtion in UsersController.java
        ```java
        @Controller
        public class UsersController {
            ...
            @PostMapping("/logoutRequest")
            public String logoutRequest() {
                
                String page = logoutService.logout();
                
                return page;
            }
        }
        ```
    * Create LogoutService.java
        ```java
        @Service
        public class LogoutService {

            @Autowired
            HttpSession session;

            public String logout() {
                
                session.invalidate();

                return "index";
            }
        }
        ```

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
