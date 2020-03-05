<#include "./macros.ftl">


<script>
    let a = ['${header?join("', '")}'];

    let b = {
        <#list columns as k, v>
        "${k}": [${v?join(", ")}],
        </#list>
    };

    console.log(a, b)

</script>