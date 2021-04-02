package ctd.net.rpc.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MapUtil
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/1
 * @Version V1.0
 **/
public class MapUtil {

    public static String getDicAttr(String key,String attr, Object obj) {
        String value = null;
        Map map = null;
        Map nodeMap=null;
        if (obj != null) {
            if (obj instanceof HashMap) {
                map = (HashMap) obj;
                nodeMap=(HashMap)map.get(key);
            }

            if (StringUtils.isNotBlank(attr)) {
                value = nodeMap.get("#" + attr).toString();
            } else {
                value = nodeMap.get("@" + attr).toString();
            }
        }
        if ("null".equals(value)) {
            return null;
        }
        return value;
    }

    /**
     * 描述：获取所有的item 节点下的ItemCode的值
     *
     * @param map map参数
     * @param nodeName
     *            节点名称
     * @throws Exception
     * @author yinxu
     * @return List<Object> 返回list的ItemCode值
     * @Creating_Time 2018年8月31日
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List<Object> getItemCodes(Map<String, Object> map, String... nodeName) throws Exception {
        Map node = null;
        List<Object> itemCodeList = new ArrayList<Object>();
        if (nodeName.length > 0) {
            for (int i = 0; i < nodeName.length; i++) {
                if (!StringUtils.isEmpty(nodeName[i])) {
                    node = (Map) map.get(nodeName[i]);
                }
            }
        } else {
            throw new Exception("nodeName参数至少有一个！");
        }
        if (node.get("Item") == null)
            throw new Exception("没有Item节点！");
        if (node.get("Item") instanceof List) {
            List<Map<String, Object>> items = (ArrayList<Map<String, Object>>) node.get("Item");
            for (Map<String, Object> item : items) {
                itemCodeList.add(item.get("ItemCode"));
            }
        } else {
            Map<String, Object> item = (Map<String, Object>) node.get("Item");
            itemCodeList.add(item.get("ItemCode"));
        }
        return itemCodeList;
    }
}
