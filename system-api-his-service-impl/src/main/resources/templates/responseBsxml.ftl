<#setting number_format="#">
<BSXml>
    <MsgHeader>
        <Sender>HIS</Sender>
        <Status><#if code == 200>true<#else>false</#if></Status>
        <ErrCode>${code}</ErrCode>
        <Detail>${msg}</Detail>
        <#if result?exists>
    <#list result as map>
      <#list map?keys as key>
        <${key}>${map["${key}"]}</${key}>
      </#list>
    </#list>
    </#if>
    </MsgHeader>
</BSXml>
