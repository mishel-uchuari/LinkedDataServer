<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:qb="http://purl.org/linked-data/sdmx/2009/measure#">
	<xsl:template match="/">
		<html>
			<head>
				<!-- Fuente -->
				<link href="https://fonts.googleapis.com/css?family=Bellefair"
					rel="stylesheet"></link>
				<!-- Mi hoja estilos -->
				<link href="/ROOT/css/style.css" rel="stylesheet"></link>
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
				<title>
					<xsl:value-of select="//rdfs:label" />
				</title>
			</head>
			<body>
				<header class="header">
					<div class="elementos-navbar">
						<img alt="Logo ayuntamiento San Sebastián" src="<%=request.getContextPath()%>/img/logo-vector-ayuntamiento-de-san-sebastian.jpg"
							width="175" height="80"></img>
						<div class="btn-group">
							<button type="button" class="btn btn-primary btn-xs">SPARQLEndpoint</button>
						</div>
					</div>
				</header>
				<div class="resource-info">
					<xsl:variable name="recurso">
						<xsl:value-of select="rdf:RDF/rdf:Description/@rdf:about" />
					</xsl:variable>
					<div class="resource">
						<h1>
							<a href="{$recurso}">
								<xsl:value-of select="//rdfs:label" />
							</a>
						</h1>
					</div>
				</div>
				<div class="middle">
					<div class="space"></div>
					<table id="table" class="table">
						<thead class="thead-dark">
							<tr>
								<th>Property</th>
								<th>Value</th>
							</tr>
						</thead>
						<xsl:apply-templates />
					</table>
					<div class="space"></div>
				</div>
				<div class="footer">
					<img alt="Logotipo Donostiako Udala" src="<%=request.getContextPath()%>/img/LOGO-blanco-sobre-azul.jpg"
						width="200" height="150"></img>
				</div>
			</body>
		</html>

	</xsl:template>

	<xsl:output method="html" encoding="utf-8" />

	<xsl:template match="rdf:RDF//rdf:Description/*">
		<xsl:variable name="href">
			<xsl:value-of select="./@rdf:resource" />
		</xsl:variable>
		<xsl:variable name="recurso">
			<xsl:value-of select="./@rdf:about" />
		</xsl:variable>
		<tr>
			<td>
				<xsl:value-of select="name()" />
			</td>
			<xsl:choose>
				<xsl:when test="not($href = '')">
					<td>
						<a href="{$href}">
							<xsl:value-of select="$href" />
						</a>
					</td>
				</xsl:when>
				<xsl:when test="$href = ''">
					<td>
						<xsl:value-of select="." />
					</td>
				</xsl:when>
			</xsl:choose>
		</tr>
	</xsl:template>
</xsl:stylesheet>
