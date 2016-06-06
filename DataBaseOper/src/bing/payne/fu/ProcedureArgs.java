package bing.payne.fu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ProcedureArgs {
	public HashMap<Integer, String> args = new HashMap<>();
	public HashMap<Integer, String> argTypes = new HashMap<>();
	public int index = 0;
	public void setArgs(String value, String type)
	{
		args.put(index, value);
		argTypes.put(index, type);
		index++;
	}
	
	public String getArgValue(int idx)
	{
		return args.get(idx);		
	}
	
	public String[] getArgsValues()
	{
		String[] values = null;
		Iterator iter = args.entrySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
		Map.Entry entry = (Map.Entry) iter.next();
			//Integer key = (Integer)entry.getKey();
			String val = (String)entry.getValue();
			values[i] = val;
			i++;
		}
		return values;
	}

	public String getArgType(int idx)
	{
		return argTypes.get(idx);		
	}
	
	public String[] getArgsTypes()
	{
		String[] values = null;
		Iterator iter = argTypes.entrySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
		Map.Entry entry = (Map.Entry) iter.next();
			//Integer key = (Integer)entry.getKey();
			String val = (String)entry.getValue();
			values[i] = val;
			i++;
		}
		return values;
	}
}
