package hellojpa.valuetype;

import java.time.LocalDateTime;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Period {

	private LocalDateTime startDate;
	private LocalDateTime endDate;
}
