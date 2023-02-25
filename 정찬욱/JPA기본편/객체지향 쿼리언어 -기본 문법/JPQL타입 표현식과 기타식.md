# 7 JPQL 타입 표현

<img src="Desktop/JPAStudy/정찬욱/JPA기본편/객체지향 쿼리언어 -기본 문법/img/type.png">

```java
public class Main{
    public static void main(String[] args) {
        
        String query = "select m.username, 'HELLO', true FROM Member m"+
                "where m.type = jpql.MemberType.ADMIN";
        List<Object[]> result = em.createQuery(query)
                .getResultList();
        
        for(object[] objects:result){
            System.out.println("objects ="+ objects[0]);
            System.out.println("objects ="+ objects[1]);
            System.out.println("objects ="+ objects[2]);
        }
    }
}

```

<img src="Desktop/JPAStudy/정찬욱/JPA기본편/객체지향 쿼리언어 -기본 문법/img/same.png">


