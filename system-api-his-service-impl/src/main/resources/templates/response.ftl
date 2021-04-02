<#setting number_format="#">
<BSXml>
    <code>${code}</code>
    <msg>${msg}</msg>
    <result>
        <#list result as map>
            <#list map?keys as key>
                <${key}>${map["${key}"]}</${key}>
            </#list>
        </#list>
    </result>
</BSXml>