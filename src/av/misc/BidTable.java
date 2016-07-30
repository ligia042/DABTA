package av.misc;

import java.util.HashMap;
import java.util.Map;

public class BidTable implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	public Map<String, Double> routeBidTable = new HashMap<String,Double>();
	
	public BidTable() {

	}
}