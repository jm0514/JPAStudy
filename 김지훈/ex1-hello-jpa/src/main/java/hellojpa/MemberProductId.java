// package hellojpa;
//
// import java.io.Serializable;
//
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
//
// @Getter @Setter
// @NoArgsConstructor
// public class MemberProductId implements Serializable {
//
// 	private Long member;
// 	private Long product;
//
// 	@Override
// 	public boolean equals(Object o) {
// 		if (this == o)
// 			return true;
// 		if (o == null || getClass() != o.getClass())
// 			return false;
//
// 		MemberProductId that = (MemberProductId)o;
//
// 		if (getMember() != null ? !getMember().equals(that.getMember()) : that.getMember() != null)
// 			return false;
// 		return getProduct() != null ? getProduct().equals(that.getProduct()) : that.getProduct() == null;
// 	}
//
// 	@Override
// 	public int hashCode() {
// 		int result = getMember() != null ? getMember().hashCode() : 0;
// 		result = 31 * result + (getProduct() != null ? getProduct().hashCode() : 0);
// 		return result;
// 	}
// }
