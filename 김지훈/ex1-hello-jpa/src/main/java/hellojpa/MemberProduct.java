// package hellojpa;
//
// import java.time.LocalDateTime;
//
// import javax.persistence.Entity;
// import javax.persistence.GeneratedValue;
// import javax.persistence.Id;
// import javax.persistence.IdClass;
// import javax.persistence.JoinColumn;
// import javax.persistence.JoinTable;
// import javax.persistence.ManyToOne;
//
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
//
// @Entity
// @IdClass(MemberProductId.class)
// @Getter @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// public class MemberProduct {
//
// 	@Id
// 	@ManyToOne
// 	@JoinColumn(name = "MEMBER_ID")
// 	private Member member; // MemberProductId.member와 연결
//
// 	@Id
// 	@ManyToOne
// 	@JoinColumn(name = "PRODUCT_ID")
// 	private Product product; // MemberProductId.product와 연결
//
// 	private int count;
// 	private int price;
//
// 	private LocalDateTime orderDateTime;
// }
