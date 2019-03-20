<#include "./macros.ftl">

[${headers?join(", ")}]

{
<#list columns as k, v>
    ${k} : [${v?join(", ")}] , <br/>
</#list>
}