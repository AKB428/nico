package akb428.tkws.dao;

public class FactoryMediaUrlDao {

	public static AbstractMediaUrlDao create() {
		
		if ("H2".equals(applicationProperties.getProperty("application.mode"))) {
			
		} else if ("MariaDB"..equals(applicationProperties.getProperty("application.mode"))) {
			
		}
	}
	
}
