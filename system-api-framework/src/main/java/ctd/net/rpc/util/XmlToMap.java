package ctd.net.rpc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;


public class XmlToMap {

	/**
	 * 描述：xml转xml 无属性
	 * 
	 * @param xmlString
	 * @return
	 * @throws DocumentException
	 * @author yinxu
	 * @return Object
	 * @Creating_Time 2018年8月23日
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object xml2Map(String xmlString) throws DocumentException {
		if ((xmlString == null) || (xmlString.isEmpty())) {
			return null;
		}
		Document doc = DocumentHelper.parseText(xmlString);
		Element root = doc.getRootElement();
		Map tempMap = new LinkedHashMap();
		if (root.elements().size() == 0) {
			tempMap.put(root.getName(), xml2MapRecursion(root));
		} else {
			tempMap = (Map) xml2MapRecursion(root);
		}
		if ((tempMap.size() == 1) && (tempMap.containsKey("_li"))) {
			return tempMap.get("_li");
		}
		return tempMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object xml2MapRecursion(Element ele) {
		List<Element> elements = ele.elements();
		if (elements.size() == 0) {
			if (getAttrSize(ele) > 0) {
				Map tempMap = new AttrXmlNodeMap();

				tempMap.put("#value", ele.getText());

				processAttr(tempMap, ele);
				return tempMap;
			}
			return ele.getText();
		}
		String _g;
		if (elements.size() == 1) {
			Map tempMap = new LinkedHashMap();
			Object cObject = xml2MapRecursion((Element) elements.get(0));
			_g = ((Element) elements.get(0)).attributeValue("_g");
			if ((_g != null) && (_g.equals("1"))) {
				List al = new ArrayList();
				al.add(cObject);
				tempMap.put(((Element) elements.get(0)).getName(), al);
			} else {
				tempMap.put(((Element) elements.get(0)).getName(), cObject);
			}
			processAttr(tempMap, ele);
			return tempMap;
		}
		Map tempMap = new LinkedHashMap();
		for (Element element : elements) {
			Object cObject = xml2MapRecursion(element);
			String __g = element.attributeValue("_g");
			if ((!tempMap.containsKey(element.getName())) && ((__g == null) || (!__g.equals("1")))) {
				tempMap.put(element.getName(), cObject);
			} else if ((tempMap.get(element.getName()) instanceof List)) {
				((List) tempMap.get(element.getName())).add(cObject);
			} else {
				List al = new ArrayList();
				if (tempMap.containsKey(element.getName())) {
					al.add(tempMap.get(element.getName()));
				}
				al.add(cObject);
				tempMap.put(element.getName(), al);
			}
		}
		processAttr(tempMap, ele);
		return tempMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void processAttr(Map node, Element ele) {
		for (Object atr : ele.attributes()) {
			DefaultAttribute atre = (DefaultAttribute) atr;
			if (!atre.getName().equals("_g")) {
				node.put("@" + atre.getName(), atre.getText());
			}
		}
	}

	private static int getAttrSize(Element ele) {
		int size = 0;
		for (Object atr : ele.attributes()) {
			DefaultAttribute atre = (DefaultAttribute) atr;
			if (!atre.getName().equals("_g")) {
				size++;
			}
		}
		return size;
	}

	/**
	 * 描述：xml转Map 带属性
	 * 
	 * @param xmlStr
	 * @param needRootKey
	 * @return
	 * @throws DocumentException
	 * @author yinxu
	 * @return Map
	 * @Creating_Time 2018年8月23日
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map xml2mapWithAttr(String xmlStr, boolean needRootKey) throws DocumentException {
		Document doc = DocumentHelper.parseText(xmlStr);
		Element root = doc.getRootElement();
		Map<String, Object> map = (Map<String, Object>) xml2mapWithAttr(root);
		if (root.elements().size() == 0 && root.attributes().size() == 0) {
			return map; // 根节点只有一个文本内容
		}
		if (needRootKey) {
			// 在返回的map里加根节点键（如果需要）
			Map<String, Object> rootMap = new HashMap<String, Object>();
			rootMap.put(root.getName(), map);
			return rootMap;
		}
		return map;
	}

	/**
	 * xml转map 带属性
	 * 
	 * @param e
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map xml2mapWithAttr(Element element) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		List<Element> list = element.elements();
		List<Attribute> listAttr0 = element.attributes(); // 当前节点的所有属性的list
		for (Attribute attr : listAttr0) {
			map.put("@" + attr.getName(), attr.getValue());
		}
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Element iter = list.get(i);
				List mapList = new ArrayList();

				if (iter.elements().size() > 0) {
					Map m = xml2mapWithAttr(iter);
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!(obj instanceof List)) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(m);
						}
						if (obj instanceof List) {
							mapList = (List) obj;
							mapList.add(m);
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), m);
				} else {

					List<Attribute> listAttr = iter.attributes(); // 当前节点的所有属性的list
					Map<String, Object> attrMap = null;
					boolean hasAttributes = false;
					if (listAttr.size() > 0) {
						hasAttributes = true;
						attrMap = new LinkedHashMap<String, Object>();
						for (Attribute attr : listAttr) {
							attrMap.put("@" + attr.getName(), attr.getValue());
						}
					}

					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!(obj instanceof List)) {
							mapList = new ArrayList();
							mapList.add(obj);
							// mapList.add(iter.getText());
							if (hasAttributes) {
								attrMap.put("#text", iter.getText());
								mapList.add(attrMap);
							} else {
								mapList.add(iter.getText());
							}
						}
						if (obj instanceof List) {
							mapList = (List) obj;
							// mapList.add(iter.getText());
							if (hasAttributes) {
								attrMap.put("#text", iter.getText());
								mapList.add(attrMap);
							} else {
								mapList.add(iter.getText());
							}
						}
						map.put(iter.getName(), mapList);
					} else {
						// map.put(iter.getName(), iter.getText());
						if (hasAttributes) {
							attrMap.put("#text", iter.getText());
							map.put(iter.getName(), attrMap);
						} else {
							map.put(iter.getName(), iter.getText());
						}
					}
				}
			}
		} else {
			// 根节点的
			if (listAttr0.size() > 0) {
				map.put("#text", element.getText());
			} else {
				map.put(element.getName(), element.getText());
			}
		}
		return map;
	}
	
	/**
	 * 描述：截取指定字符串区间的值
	 * @param str
	 * @param strStart
	 * @param strEnd
	 * @return
	 * @throws Exception 
	 * @author yinxu
	 * @return String
	 * @Creating_Time 2018年8月23日
	 */
	public static String subString(String str, String strStart, String strEnd) throws Exception {
        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);
        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0)
            throw new Exception( "字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串");
        if (strEndIndex < 0)
        	throw new Exception( "字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串");
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        //返回BSXml节点下的xml
        StringBuffer resultStr = new StringBuffer();
        resultStr.append("<BSXml>");
        resultStr.append(result);
        resultStr.append("</BSXml>");
        return resultStr.toString();
    }
}
