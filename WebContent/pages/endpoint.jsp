<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Mi hoja estilos -->

<link href='http://cdn.jsdelivr.net/yasqe/2.11.10/yasqe.min.css'
	rel='stylesheet' type='text/css'></link>
<link href='http://cdn.jsdelivr.net/yasr/2.10.8/yasr.min.css'
	rel='stylesheet' type='text/css'></link>
	
	<!-- external resources CDN -->
	<link href="https://fonts.googleapis.com/css?family=Roboto|Source+Code+Pro" rel="stylesheet">
	<script defer src="https://use.fontawesome.com/releases/v5.0.8/js/all.js"></script>

	
<title>Linked Open Data Donostia: SPARQL endpoint</title>
	<!-- JQuery -->
				<script src="http://code.jquery.com/jquery-latest.min.js"></script>
				<link rel="stylesheet"
					href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
					integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
					crossorigin="anonymous"></link>
				<link rel="stylesheet"
					href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
					integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp"
					crossorigin="anonymous"></link>
				<!-- Latest compiled and minified JavaScript -->
				<script
					src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
					integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
					crossorigin="anonymous"></script>
					<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet"></link>
</head>
<body>
	<header class="header">
		<div class="elementos-navbar">
			<img alt="Logo ayuntamiento San Sebastián"
				src="<%=request.getContextPath()%>/img/logo-vector-ayuntamiento-de-san-sebastian.jpg" width="175"
				height="80"></img>
				<h1>SPARQL endpoint</h1>
		</div>
	</header>
	</div>
	<div class="middle">
	<div class="space"></div>
	<div id='yasgui'></div>
	<div id="yasr"></div>
	<div class="space"></div>
	<script
		src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script src='http://cdn.jsdelivr.net/yasqe/2.2/yasqe.bundled.min.js'></script>
	<script src='http://cdn.jsdelivr.net/yasr/2.4/yasr.bundled.min.js'></script>
	<script type="text/javascript">
		$.ajaxSetup({
			type : "POST",
			cache : false,
			contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
			beforeSend : function(xhr) {
				xhr.setRequestHeader("Access-Control-Allow-Origin", "*");
				xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");

			},
			crossDomain : false
		});
		var yasqe = YASQE(document.getElementById("yasgui"), {
			backdrop : true,
			persistent : null,
			sparql : {
				endpoint : "${url}",
				showQueryButton : true
			}
		});
		var yasr = YASR(document.getElementById("yasr"), {
			//this way, the URLs in the results are prettified using the defined prefixes in the query
			getUsedPrefixes : yasqe.getPrefixesFromQuery
		});
		//link both together
		yasqe.options.sparql.handlers.success = function(data, status, response) {
			yasr.setResponse({
				response : data,
				contentType : response.getResponseHeader("Content-Type")
			});
		};
		yasqe.options.sparql.handlers.error = function(xhr, textStatus,
				errorThrown) {
			yasr.setResponse({
				exception : textStatus + ": " + errorThrown
			});
		};
		yasqe.options.sparql.callbacks.complete = yasr.setResponse;
		yasr.options.getUsedPrefixes = yasqe.getPrefixesFromQuery;
	</script>
	</div>
	</div>
	<div class="footer">
		<img alt="Logotipo Donostiako Udala"
			src="<%=request.getContextPath()%>/img/LOGO-blanco-sobre-azul.jpg" width="200" height="150"></img>
	</div>
</body>
</html>