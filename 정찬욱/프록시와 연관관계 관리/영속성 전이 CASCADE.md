# 3 영속성 전이 : CASCADE
* 특정 엔티티를 여속 상태로 만들때 연관된 엔티티도 함께 영속 상태로 만들고 싶을때 사용함
* ex) 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장.
* 연관관계나 지연로딩 이런것과는 하등 상관없음.
* 엔티티를 영속화 할 때 연관되 엔티티도 함께 영속화 하는 편리함을 제공하는 역할임
## 3.1 동작 방법
```java
    @Entity
    public class Parent{
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToMay(mappedBy = "parent", cascade = CascadeType.All)
    private List<Child> childList = new ArrayList<>();
    
    public void addChild(Child child){
        childList.add(child);
        child.setParent(this);
    }
}
```

```java
    @Entity
    public class Child{
    
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;
}
```
```java
     public class Main{
    public static void main(String[] args) {
        Child child1 = new Child();
        Child child2 = new Child();
        
        Parent parent = new Parent();
        parent.addChild(child1);
        parent.addChild(child2);
        
        em.persist(parent);
        tx.commit
    }
}

```
* 지금 Parent 중심으로 코드를 짜고 있는데
* Parent persist 할 때 child는 좀 자동으로 관리 됐으면 좋겠음
* em.persist(parent)만 해도 child까지 전부 persist됨. -> 개편함

## 3.2 CASCADE 종류
* All : 모두 적용(모든 라이프 사이클 : 삭제도 같이, 저장도 같이)
* PERSIST : 저장 할 때만 이용
* REMOVE : 삭제 할 때만
* (MERGE : 병합, REFRESH, DETACH) -> 잘 안

## 3.3 언제 써야 좋은가?
* 하나의 부모가 자식을 관리 할 때(ex : 첨부파일, 첨부파일 경로...) 첨부파일의 경로는 한개의 게시물에서만 관리함
* 근데 여러개의 부모가 하나의 자식을 관리함 -> 매우 곤란
* Child에서 다른 애들로 나가는건 상관없음. Child에 연관된 부모가 여럿이면 문제됨


# 4. 고아객체
## 4.1
* orphanRemoval = true : 고아객체가 제거됨. 
* 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동을 삭제하는 기능임

```java
    @Entity
    public class Parent{
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToMay(mappedBy = "parent", cascade = CascadeType.All, orphanRemoval = true)
    private List<Child> childList = new ArrayList<>();
    
    public void addChild(Child child){
        childList.add(child);
        child.setParent(this);
    }
}
```

```java
    Parent parent1 = em.find(Parent.class, id);
    parent1.getChildren().remove(0);
    //자식 엔티티를 컬렉션에서 제거
```
* 자동으로 Child delete쿼리 나감. -> DELETE FROM CHILD WHERE ID=?로 바로 나감

## 4.2 주의
* 참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로 보고 삭제하는 기능
* 참조하는 곳이 하나일때 사용해야 함.
* *특정 엔티티가 개인 소유 할 때 사용해야 함.* 
* @OneToOne, @OneToMany만 사용가능
* 부모 제거시 자식도 함께 제거됨을 항상 기억하자

# 5 영속성 전이 + 고아객체, 생명주기
## CascadeType.All + orphanRemoval=true
* 스스로 생명주기를 관리하는 엔티티는 em.persist()로 영속화, em.remove()로 제거
* 두 옵션을 모두 활성화 하면 부모 엔티티를 통해서 자식의 생명주기를 관리할 수 있음
* 도메인 주도 설계(DDD) Aggregate Root 개념을 구현할 때 유용함.
*  극단적으로 이야기하면 Child의 Repository가 없어도 됨 -> 생명주기를 Parent가 관리하기 때문
