FreeMarker Template example: ${message}

=======================
===  County List   ====
=======================

[${countries?join(", ")}]

<#list countries as country>
    ${country_index + 1}. ${country}
</#list>