<#setting number_format="#">
<BSXml>
    <MsgHeader>
        <Sender>HIS</Sender>
        <Status><#if code == 200>true<#else>false</#if></Status>
        <ErrCode>${code}</ErrCode>
        <Detail>${msg}</Detail>
    </MsgHeader>

    <#if result?exists>
        <#list result as map>
            <QueryResult>
            <#list map?keys as key>
                <${key}>${map["${key}"]}</${key}>
            </#list>
            </QueryResult>
        </#list>
    </#if>

</BSXml>
