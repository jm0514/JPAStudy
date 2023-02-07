// package hellojpa;
//
// import java.time.LocalDateTime;
//
// import javax.persistence.Entity;
// import javax.persistence.GeneratedValue;
// import javax.persistence.Id;
// import javax.persistence.JoinTable;
// import javax.persistence.ManyToOne;
//
// @Entity
// public class MemberProduct {
// 	@Id
// 	@GeneratedValue
// 	private Long id;
//
// 	@ManyToOne
// 	@JoinTable(name = "MEMBER_ID")
// 	private Member member;
//
// 	@ManyToOne
// 	@JoinTable(name = "PRODUCT_ID")
// 	private Product product;
//
// 	private int count;
// 	private int price;
//
// 	private LocalDateTime orderDateTime;
// }
