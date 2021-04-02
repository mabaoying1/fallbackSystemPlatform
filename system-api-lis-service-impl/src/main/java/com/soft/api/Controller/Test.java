package com.soft.api.Controller;

import ctd.net.rpc.util.ObjectToMap;
import org.dom4j.DocumentException;

import java.util.Map;

/**
 * @ClassName Test
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/13
 * @Version V1.0
 **/
public class Test {
    //String tx_no = UUID.randomUUID().toString();


    public static void main(String[] args) throws DocumentException {

        String str1 = "03";
        if(str1.substring(0,1).equals("0")){
            str1 = str1.substring(1,str1.length());
        }
        System.out.println(str1);

        String param = "<BSXml>" +
                "    <MsgHeader>" +
                "        <RecordTitle>检查申请单接收消息</RecordTitle>" +
                "    </MsgHeader>" +
                "    <ExmRequest>" +
                "       <Item _g=\"1\">" +
                "            <ItemCode><![CDATA[<0]]></ItemCode>" +
                "        </Item>" +
                " <Item> " +
                "            <ItemCode><![CDATA[>123]]></ItemCode>" +
                "        </Item>" +
                "    </ExmRequest>" +
                "</BSXml>";
        Map<String, Object> paramMap = ObjectToMap.getObjectToMap(param);
        System.out.println("");

        String str = " <![CDATA[ <BSXML><hospitalId> <![CDATA[45075129-0]]></hospitalId>  ]]>";
        str = str.replaceAll("<!\\[CDATA\\[","").replaceAll("]]>","").trim();
        System.out.println(str);
    }
}
