<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
    <title>World of Warcraft Quest</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body class="container mt-5">

<h1>Беспокойные бородавочники</h1>
<hr>
<p>
    Сумерки опустились на Эльвиннский лес, и спокойствие, что обычно царило среди деревьев, нарушилось.
    Жители у стен Штормграда жалуются на кабанов, которые необычно агрессивны и беспокойны: они ломают заборы,
    топчут посевы и даже нападают на путников. Старейшины считают, что это не обычное поведение зверей — кто-то
    или что-то спровоцировало их ярость.
</p>
<p>
    Твоя задача — отправиться в лес, выяснить причину беспокойства бородавочников и, если получится,
    восстановить мир между человеком и лесной стихией… или же столкнуться с последствиями их ярости лицом к лицу.
</p>
</hr>

<br><br>
<h5>Введите имя Вашего персонажа</h5>

<form action="process.jsp" method="post" accept-charset="UTF-8">
    <input type="text" name="userName" placeholder="место для имени">
    <input type="submit" value="Начать квест">
</form>

<% if ("empty".equals(request.getParameter("error"))) { %>
<p style="color:red;">Пожалуйста, введите имя =)</p>
<% } %>

<div class="text-center my-4">
    <img src="images/start.jpg" class="img-fluid rounded" alt="Бородавочник">
</div>

</body>
</html>
