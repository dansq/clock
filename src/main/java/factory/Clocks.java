package factory;

import java.sql.Timestamp;
import java.time.LocalTime;

public interface Clocks {

	
	Long getId();

	void setId(Long id);

	LocalTime getTime();

	void setTime(LocalTime time);

	Timestamp getCreatedAt();

	void setCreatedAt(Timestamp createdAt);
	
	float getAngle();

	void setAngle(float angle);

}
