<#ftl encoding="UTF-8">
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Task Statistics for ${name}</title>
</head>
<body>
<h1>Hello, ${name}!</h1>
<h2>Here are your task statistics for today:</h2>

<h3>Incomplete Tasks:</h3>
<#if incompleteTasks?size == 0>
    <p>You have no incomplete tasks!</p>
<#else>
    <#list incompleteTasks as task>
        <p>${task.title}</p>
    </#list>
</#if>

<h3>Completed Tasks:</h3>
<#if completeTasks?size == 0>
    <p>You have no completed tasks yet.</p>
<#else>
    <#list completeTasks as task>
        <p>${task.title}</p>
    </#list>
</#if>

<p>Keep up the great work!</p>
</body>
</html>
