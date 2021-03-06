
## 설명
로그인 처리를 구현하는데는 두개의 인터페이스가 존재한다 UserDetailsService와 AuthencationProvider가 존재한다. 두개의 인터페이스를 구현하면 security로그인 부분에서 데이터베이스에서 유저정보를 불러어오는부분과, 비밀번호 대조를 직접구현할 수 있다.

UserDetailsService : 유저정보를 디비에서 불러오는 역할
AuthencationProvider가 : 로그인 인증처리를 구현(비밀번호 대조)

### 1. security 기본셋팅
가장먼저 web.xml에 내용을 추가합니다.
```xml
<filter-name>springSecurityFilterChain</filter-name>
<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class> //filter 처리을 spring security로 위임하는것
</filter>

<filter-mapping>
<filter-name>springSecurityFilterChain</filter-name>
<url-pattern>/*</url-pattern>
</filter-mapping>
<filter>
```
* 기본적인 필터는 설정을 안해줘도 자동으로 작동


web.xml에 security-context.xml 추가해준다.

```xml
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>/WEB-INF/spring/security-context.xml</param-value>
</context-param>
<listener>
```


그다음 메이븐 의존성을 추가합니다.
### 메이븐 의존성 추가
```xml
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-core</artifactId>
<version>3.1.3.RELEASE</version>
</dependency>

<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-taglibs</artifactId>
  <version>3.1.3.RELEASE</version>
</dependency>

<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-config</artifactId>
  <version>3.1.3.RELEASE</version>
</dependency>

```

로그인 주소,로그아웃주소등 설정
먼저 security 네임스페이스을 추가해줘야한다.
그다음 security-context.xml에 내용을 추가한다
```xml
<security:http auto-config="true" use-expressions="true">
  // expressions을 추가해야 hasRole,permitAll등의 다양한 옵션들을 쓸수 있음
<security:form-login login-page="/loginForm"/> //로그인 url
<!-- <security:form-login login-page="/loginForm" authentication-failure-url="성공유알엘 "/> -->
  <security:intercept-url pattern="/admin/**" access="hasAuthority('ROLE_ADMIN')"/>// 관리자만 들어갈수있음
<security:intercept-url pattern="/manager/**" access="hasRole('ROLE_MANAGER')"/> //위랑 같은뜻 매니저만 들어갈수있음
<security:intercept-url pattern="/member/**" access="isAuthenticated()"/> //인증한사람만 들어갈수있음
<security:intercept-url pattern="/user" access="hasRole('ROLE_USER')" />
<security:intercept-url pattern="/welcome" access="hasRole('ROLE_ADMIN')" />
<security:intercept-url pattern="/**" access="permitAll" /> //누구나 다들어갈수 있음

````
* <security:form-login login-page="/loginForm"/> 로그인실패하면 여기에서 설정해둔 url로 이동하게 된다.


##
 사용자 커스터 마이징
 ![](http://i.imgur.com/Hhpxz9N.png)

#### 화면 input 설정
* 아이디를 입력하는 input의 name은 j_username이다
* 비밀번호를 입력하는 input은  j_password이다.
* action은 j_spring_security_check이다. (post)

### login 커스터마이징
```xml
<security:form-login
 username-parameter="loginid" //form name 변경할 수 있음
 password-parameter="loginpwd" //form name 변경할 수 있음
 login-page="/login" //권한없는 url접근하면 자동 여기에서 지정한 로그인으로가짐
 default-target-url="/welcome" // 로그인하고 지정한 url이없을떄 defalut로 이동되는 url
 authentication-failure-url="/login?fail=true"//로그인 실패시 이동되는 url 설정
 />

```

* 로그인 실패시 세션에 자동으로에러 메시지를 출력해주는 부분이 있는데 그걸 사용하게되면 로그인을 할때 마다 세션메시지가 계속 출력되게되고 이러다보면 was에 메모리가 가득차 full gc가 지속적으로 발생하게되면서 전반적으로 웹페이지가 느려질 수 있다.


| 테이블      |           설명          |                                   컬럼                                   |
|-------------|:-----------------------:|:------------------------------------------------------------------------:|
| users       | 사용자 이름과 암호 정보 | username:사용자이름(로그인ID) <br>password:인증암호<br> enabled:계정사용가능여부|
| authorities | 사용자-권한 매핑 정보   | username:사용자 이름<br> authority:권한                                      |


## 2. db 연동

### 인증 처리과정
1. 접속자가 계정과 암호를 입력한다.
2. 접속자가 입력한 계정을 이용하여 데이터베이스에서 사용자 정보를 조회한 결과를 UserDetailsService로 담는다.

>정확하게 말해  UserDetailsService에 담는것이 아니라 비지니스 로직을 처리하는일을하고 UserDetails라는 곳에 담는다. 기본적으로 계정 암호 권한룰 그외 몇가지만 설정되어 있어 필요따라 유저 정보를 추가함

3. AuthenticationProvider: 접속자가 입력한 로그인정보랑 사용자 정보랑 같일치하면 로그인 인증이 성공한다.

root-context.xml에 디비정보를 추가해준다.
```xml
<!-- Root Context: defines shared resources visible to all other web components -->
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
<property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
<property name="url" value="jdbc:mysql://디비주소"></property>
<property name="username" value="디비아이디"></property>
<property name="password" value="디비비번"></property>
</bean>
```

secuirty-context.xml에 내용을 추가해준다.
```xml
<security:authentication-manager alias="authenticationManager">
  <security:authentication-provider ref="customeAuthenticationProvider">
  </security:authentication-provider>
  <security:authentication-provider user-service-ref="customeUserDetailsService">
     <security:password-encoder ref="passwordEncoder"></security:password-encoder>
  </security:authentication-provider>
</security:authentication-manager>
```
그다음 customeAuthenticationProvider, customeUserDetailsService을 구현해주면 된다.
```java
@Service
public class CustomUserDetailsService implements UserDetailsService {


	@Inject
	UserDao dao;
	// 디비에서 유저정보를 불러오는메소 이것을 AuthenticationProvider에서 인증을통함
	@Override
	public user loadUserByUsername(String username) throws UsernameNotFoundException {
		user userInfo = null;
		try {
			userInfo = dao.read(username); //디비 정보를 불러와 유저정보 조
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
}


```
CustomUserDetailsService 이곳에서 유저가입력한 아이디를 통해 유저정보를 불러와서 CustomAuthenticationProvider class로 정보를 넘겨주게된다.

```java
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider { //authenticationManager
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication; //유저가 입력한 정보를 이이디비번으으로만든다.(로그인한 유저아이디비번정보를담는다)

    user userInfo = customeUserDetailsService.loadUserByUsername(authToken.getName()); //UserDetailsService에서 유저정보를 불러온다.
    if (userInfo == null) {
      throw new UsernameNotFoundException(authToken.getName());
    }

    if (!matchPassword(userInfo.getPassword(), authToken.getCredentials())) {
      throw new BadCredentialsException("not matching username or password");
    }

    List<GrantedAuthority> authorities = (List<GrantedAuthority>) userInfo.getAuthorities();

    return new UsernamePasswordAuthenticationToken(userInfo,null,authorities);
  }

  private boolean matchPassword(String password, Object credentials) {
    return password.equals(credentials);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }

}
```
그럼 CustomAuthenticationProviderclass에서 유저가입력한 정보와 디비에 유저정보를 통해 인증 처리를 하게된다.


```sql
CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `age` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

//이부분은 사용하지 않지만 사용하실분들은 권한설정 테이블을 만드셔도됩니다.
CREATE TABLE `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  UNIQUE KEY `ix_auth_username` (`username`,`authority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

```

| 테이블      |           설명          |                                   컬럼                                   |
|-------------|:-----------------------:|:------------------------------------------------------------------------:|
| users       | 사용자 이름과 암호 정보 | username:사용자이름(로그인ID) <br>password:인증암호<br> enabled:계정사용가능여부 <br> age:사용자정보 나이 |
| authorities | 사용자-권한 매핑 정보   | username:사용자 이름(로그인ID)<br> authority:권한        

age는 나중에 유저정보를 확인하기위해 넣어습니다.(추가적으로 유저정보를 더넣고싶으면 더넣어도 무방함)

예) user Table

| username | password | enabled | age |
|----------|:--------:|:-------:|:-------:|
|   admin  |   1234   |    1    | 33|
|  bkchoi  |   1234   |    1    |23|


authorities Table

| username |   authority   |
|----------|:------------:|
|   admin  |     USER     |
|   admin  | USER_MANAGER |
|  bkchoi  |     USER     |


위에처럼 생성하게되면 따로 dao연결과 같은 설정필요없이 알아서 아이디 비번을 디비에서 확인하게된다.(테이블 이름과 칼럼이름이 위와 동일해야함)

참고
* [사랑이 고픈 프로그래머..](http://zgundam.tistory.com/45)
* [Spring 4.0 프로그래밍](http://storefarm.naver.com/dcvirus/products/458328014?NaPm=ct%3Dj06r6ydk%7Cci%3D1744f23aa4586709889a372fc15683afa2b4928e%7Ctr%3Dsls%7Csn%3D182521%7Chk%3Dab9fe496302792c50421edea06a3e322286ad2b9)
