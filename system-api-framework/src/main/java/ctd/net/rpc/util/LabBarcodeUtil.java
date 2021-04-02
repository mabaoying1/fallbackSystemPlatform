package ctd.net.rpc.util;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @ClassName util
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/10
 * @Version V1.0
 **/
@Component
public class LabBarcodeUtil {

    public List<HashMap<String, Object>> getRequstid(List<Map<String, Object>> list){
        //把list中的数据转换成msp,去掉同一id值多余数据，保留查找到第一个id值对应的数据
        List<HashMap<String, Object>> listMap = new ArrayList<>();
        HashMap<String, Object> msp = new HashMap<>();
        for(int i = list.size()-1 ; i>=0; i--){
            Map map = list.get(i);
            String id = (String)map.get("REQUESTID");
            msp.put(id, "");
        }
        //把msp再转换成list,就会得到根据某一字段去掉重复的数据的List<Map>
        Set<String> mspKey = msp.keySet();
        for(String key: mspKey){
            HashMap<String,Object> newMap = new HashMap<String,Object>();
            newMap.put("REQUESTID", key);
            listMap.add(newMap);
        }
        return listMap;
    }

    /**
     * 描述：得到所有的FYXH
     * @param map
     * @return
     * @return List
     */
    @SuppressWarnings("rawtypes")
    public List getFyxh(List<Map<String, Object>> map,String requestid){
        List<Object> list = new ArrayList<Object>();
        for (Map hashMap : map) {
            String reqid =(String)hashMap.get("REQUESTID");
            if (requestid.equals(reqid) ){
                list.add(hashMap.get("FYXH"));
            }
        }
        return list;
    }

    public List getFyxhPrehyid(List<Map<String, Object>> map,String requestid){
        List<Object> list = new ArrayList<Object>();
        for (Map hashMap : map) {
            String reqid =(String)hashMap.get("REQUESTID");
            if (requestid.equals(reqid) ){
                list.add(hashMap.get("PREHYID"));
            }
        }
        return list;
    }

    /**
     * 描述：得到item下  节点下所有的ItemCode值
     * @param map
     * @return
     * @return List
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object getAllItemCode(Map<String,Object> map) throws Exception{
        Map labBarcode = (Map) map.get("LabBarcode");
        List<HashMap<String,Object>> requestList =(ArrayList<HashMap<String,Object>>)labBarcode.get("LabRequest");
        List<String> itemCode = new ArrayList<String>();
        for (HashMap<String,Object> request:requestList
        ) {
            if(request.get("Item") instanceof List){
                List<HashMap> item  = (List<HashMap>) request.get("Item");
                for (int i = 0; i < item.size(); i++) {
                    if(item.get(i).get("ItemCode") instanceof Map){
                        Map map1 = (Map) item.get(i).get("ItemCode");
                        itemCode.add((String)map1.get("#text"));
                    }else{
                        itemCode.add(item.get(i).get("ItemCode").toString());
                    }
                }
            }else{
                HashMap item  = (HashMap)request.get("Item");
                if(item.get("ItemCode") instanceof Map){
                    Map map1 = (Map) item.get("ItemCode");
                    itemCode.add((String)map1.get("#text"));
                }else{
                    itemCode.add(item.get("ItemCode").toString());
                }
            }
        }
        return itemCode;
    }

    /**
     * 描述：取 LabBarcode 节点下 ExeDoctor 的值
     * @param map
     * @return
     * @return String
     */
    @SuppressWarnings({ "rawtypes" })
    public String getExeDoctor(Map map){
        Map labBarcode = (Map) map.get("LabBarcode");
        Map ExeDoctor = (Map) labBarcode.get("ExeDoctor");
        String ExeDoctorNum =  (String) ExeDoctor.get("#text");
        return ExeDoctorNum;
    }

    /**
     * 描述：得到 EXAMINAIM   EXAMINAIMCODE 这两个参数值 get(0) = EXAMINAIM
     *  get(1) = EXAMINAIMCODE
     * @param labBarcode 节点
     * @return
     * @return List<String>
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<String> getSbList(Map labBarcode){
        List<String> sbList = new ArrayList<String>();
        List<HashMap<String,Object>> labRequest  = (List<HashMap<String,Object>>) labBarcode.get("LabRequest");
        StringBuffer examinaim = new StringBuffer();
        StringBuffer examinaimcode = new StringBuffer();
        HashMap hashMap = null;
        for (HashMap<String,Object> request:labRequest
        ) {
            if(request.get("Item") instanceof  List){
                List<HashMap> item  = (List<HashMap>) request.get("Item");
                for (int i = 0; i < item.size(); i++) {
                    hashMap = (HashMap)item.get(i).get("ItemCode");
                    examinaimcode.append((String)hashMap.get("#text"));
                    examinaimcode.append("^");
                    examinaim.append((String)hashMap.get("@DisplayName"));
                    examinaim.append("+");
                }
            }else{
                hashMap  = (HashMap)request.get("Item");
                hashMap = (HashMap)hashMap.get("ItemCode");
                examinaimcode.append((String)hashMap.get("#text"));
                examinaimcode.append("^");
                examinaim.append((String)hashMap.get("@DisplayName"));
                examinaim.append("+");
            }
        }
        examinaim.deleteCharAt(examinaim.length() - 1);
        sbList.add(examinaim.toString());
        sbList.add(examinaimcode.toString());
        return sbList;
    }

    public Object getItemCode(Map<String,Object> map) throws Exception{
        List<String> itemCode = new ArrayList<String>();
        if(map.get("Item") instanceof List){
            List<HashMap> item  = (List<HashMap>) map.get("Item");
            for (int i = 0; i < item.size(); i++) {
                Map map1 = (Map) item.get(i).get("ItemCode");
                itemCode.add((String)map1.get("#text"));
            }
        }else{
            HashMap item  = (HashMap)map.get("Item");
            Map map1 = (Map) item.get("ItemCode");
            itemCode.add((String)map1.get("#text"));
        }
        return itemCode;
    }


}
