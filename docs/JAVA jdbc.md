## JDBC

https://ko.wikipedia.org/wiki/JDBC

JDBC는 자바에서 DB에 접속할 수 있도록 하는 자바 API

다양한 DB에 접근 가능하도록 JAVA에서 표준화한 인터페이스이다.

JDBC는 크게 JDBC 인터페이스와 JDBC 드라이버로 구성되어 있다. (java.sql 패키지에 존재한다.)

따라서 JDBC를 통해 알맞은 JDBC Driver를 호출하고

그것에 따른 쿼리등을 실행하는데 도움받을 수 있다.

이때 JDBC Driver는 각 데이터베이스마다 존재할 수 있다.

그래서 JDBC를 사용하는 사용자 입장에서는 알맞은 JDBC 드라이버를 설치하고

JDBC의 DriverManager를 통해 원하는 데이터베이스 드라이버를 로딩하기만 하면 된다

그렇기 때문에 JDBC에서 제공하는 API를 학습한다고 하면 데이터베이스 자체의 변경점에 대해서

큰 변경을 일으킬 걱정이 없다.

### 연결과 Driver

연결하기 위해서는 Connection 객체를 확보해야 한다.

알맞은 DB에 연결해야 하기에, DB 종류마다 다르게 설치한 JDBC Driver를 동적 로딩하여

접속정보를 쥐어주면 연결할 수 있다.

Driver 동적로딩 코드를 보면 좀 더 이해가 편하다.

아래는 `mysql-connector-j` 의 Driver부분을 발췌했다.

```java
public class Driver extends NonRegisteringDriver implements java.sql.Driver {
    public Driver() throws SQLException {
    }

    static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException var1) {
            throw new RuntimeException("Can't register driver!");
        }
    }
}
```

동적 클래스로딩에 의해 JVM 클래스로더가 해당 클래스에 대해서 검증을 거친후 등록한다.

여기에서 스태틱 블록이 실행되면서 JDBC가 갖고있는 DriverManager에 현재 Driver를 등록한다.

따라서 동적 클래스로딩을 통해 Driver를 등록 할 수 있게 된다.

그런데 JDBC 4.0 버전 이후부터는 JDBC 드라이버가 자동 로딩이 도입되어 생략이 가능하다고 한다.

https://tecoble.techcourse.co.kr/post/2023-06-28-JDBC-DataSource/
