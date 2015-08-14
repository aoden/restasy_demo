package util;

public class PagingUtil {
	
	/**
	 * calculate the first index in collection of given page number
	 * @param page the page number
	 * @param limit number of items per page
	 * @return first index of given page number
	 */
	public static Integer doPaging(Integer page , Integer limit){
		try{
			
			return page*(limit-1);
		}catch(Exception e){
			
			e.printStackTrace();
			return null;
		}
		
	}
	
}
