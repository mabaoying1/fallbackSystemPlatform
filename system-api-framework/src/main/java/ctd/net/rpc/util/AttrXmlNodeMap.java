package ctd.net.rpc.util;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings({ "serial", "rawtypes"})
public class AttrXmlNodeMap extends LinkedHashMap implements Map
{
  public String toString()
  {
    if (containsKey("#value")) {
      return get("#value").toString();
    }
    return "";
  }
  
  public String getText()
  {
    return toString();
  }
  
  @SuppressWarnings("unchecked")
 public void setText(String value)
  {
	  put("#value", value);
  }
  
  public String getAttribute(String attribute)
  {
    if (containsKey("@" + attribute)) {
      return get("@" + attribute).toString();
    }
    return null;
  }
  
  @SuppressWarnings("unchecked")
public void setAttribute(String attribute, String value)
  {
    put("@" + attribute, value);
  }
}
