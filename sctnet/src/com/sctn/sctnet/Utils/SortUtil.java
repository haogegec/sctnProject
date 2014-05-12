package com.sctn.sctnet.Utils;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class SortUtil  implements Comparator{

	public int compare(Object arg0, Object arg1) {
		Map<String, String> map0 = (Map<String, String>) arg0;
		Map<String, String> map1 = (Map<String, String>) arg1;

		int flag = map0.get("id").compareTo(map1.get("id"));
		return flag;
	}

}
