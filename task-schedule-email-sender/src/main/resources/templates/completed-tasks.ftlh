<#ftl encoding="UTF-8">
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Notification of completed tasks, ${name}</title>
    <style>
        #body {
            display: flex;
            flex-direction: column;
            font-family: "Segoe UI", serif;
            align-items: center;
            background-color: #607FF2;
            font-size: 20px;
            height: 100%;
        }

        #div {
            display: block;
            flex-direction: column;
            margin: 10%;
            font-family: "Segoe UI", serif;
            background-color: white;
            padding: 3%;
            border-radius: 20px;
            color: black;
        }

        #a {
            text-decoration: none;
        }
    </style>
</head>
<body>
<div id="body">
    <div id="div">
        <h1 style="text-align: center">Notification of completed tasks</h1>
        <p>Hello, ${name}!</p>
        <p>Do you have ${totalTasks} completed tasks.</p>
        <p>Here are ${tasks?size < 5 ? tasks?size : 5} of them:</p>
        <#list tasks as task>
            <p>${task.title}</p>
        </#list>
        <p>With best wishes, <br> Mikhail Dragomirov</p>
        <a id="a" href="https://github.com/DragomirovCode/task-scheduler">Github</a>
    </div>
</div>
</body>
</html>
