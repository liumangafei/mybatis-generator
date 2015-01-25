package ${package};

<#list importList as import>
import ${import};
</#list>

public class ${className} {

<#list propertyList as property>
    <#if property.tablePropertyRemarmk != "">
    /** ${property.tablePropertyRemarmk} */
    private ${property.propertyType} ${property.propertyName};
    <#else>
    private ${property.propertyType} ${property.propertyName};
    </#if>
</#list>
<#list propertyList as property>

    public ${property.propertyType} ${property.propertyNameGetStr}() {
        return ${property.propertyName};
    }

    public void ${property.propertyNameSetStr}(${property.propertyType} ${property.propertyName}) {
        this.${property.propertyName} = ${property.propertyName};
    }
</#list>

    @Override
    public String toString() {
        return "${className}{" +
            <#list propertyList as property>
                <#if property.propertyType = "String">
                    "<#if 0 < property_index>, </#if>${property.propertyName}='" + ${property.propertyName} + "\'" +
                <#elseif property.propertyType = "byte[]">
                    "<#if 0 < property_index>, </#if>${property.propertyName}=" + Arrays.toString(${property.propertyName}) +
                <#else>
                    "<#if 0 < property_index>, </#if>${property.propertyName}=" + ${property.propertyName} +
                </#if>
            </#list>
                    "}";
    }
}