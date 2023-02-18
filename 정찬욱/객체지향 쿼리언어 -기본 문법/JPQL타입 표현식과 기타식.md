# 7 JPQL 타입 표현

<img src="./img/type.png">

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

<img src="./img/same.png">


