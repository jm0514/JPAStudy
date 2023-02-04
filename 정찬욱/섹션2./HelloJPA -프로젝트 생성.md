## 실행환경
### Java11, Maven
#### pom.xml
```xml

        <!-- java 11버젼 사용시 추가 -->
        <dependency>
            <groupId>javax.xml.bind</groupId>
            <artifactId>jaxb-api</artifactId>
            <version>2.3.0</version>
        </dependency>
        <!-- JPA 하이버네이트 -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>5.3.10.Final</version>
        </dependency>
        <!-- H2 데이터베이스 -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>2.1.214</version>
        </dependency>
```

## JPA설정 파일
#### * /resources/META-INF/persistence.xml에 위치함.

```xml

    <properties>
    <!-- 필수 속성 -->
    <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
    <property name="javax.persistence.jdbc.user" value="sa"/>
    <property name="javax.persistence.jdbc.password" value=""/>
    <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
    <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
    <!-- 옵션 -->
    <property name="hibernate.show_sql" value="true"/>
    <property name="hibernate.format_sql" value="true"/>
    <property name="hibernate.use_sql_comments" value="true"/>
    <property name="hibernate.hbm2ddl.auto" value="create" />
    </properties>
```
 * name="javax.persistence.jdbc.driver"는 어떤 DB를 이용할 것인지를 정하면됨. oracle이면 oracle 여기선 h2를 사용.
 * "name=hibernate.dialect"로 db의 방언을 정할수 있음. ex) MySQL : Limit, Oracle : RowNum과 같이 표준SQL이 아닌 각 DB만의 고유한 명령어들을 여기서 정할 수 있음.
 * 예를들어 오라클DB를 쓴다면 value="org.hibernate.dialect.Oracle11gDialect/를 써주면 됨."
 * name="hibernate.show_sql"는 DB에 쿼리 나가는거 볼지 말지 결정
 * 





