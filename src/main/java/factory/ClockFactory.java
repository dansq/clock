package factory;

public class ClockFactory {

	public static Clocks getClocks (String type){
		if (type == null){
			return null;
		}
		if (type.equalsIgnoreCase("Clocky")){
			return new Clocky();
		}
		
		return null;
	}
}
