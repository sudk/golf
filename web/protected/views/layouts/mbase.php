<!DOCTYPE html>
<html>
<head>
    <title id="title"><?php echo CHtml::encode(Yii::app()->name); ?></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <!-- Bootstrap -->
    <link rel="stylesheet" href="./css/bootstrap.min.css">
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="./js/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="./js/bootstrap.min.js"></script>
</head>
<body>
    <div class="container">
        <?php echo $content; ?>
    </div>
</body>
</html>