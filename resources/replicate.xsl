<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" 
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
	xmlns:qb="http://purl.org/linked-data/sdmx/2009/measure#">
	<xsl:output method="html" encoding="utf-8" />
	<xsl:template match="/">
		<html>
			<head><title><xsl:value-of select="//rdfs:label"/></title></head>
			<body>
					<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="rdf:RDF//rdf:Description">
		<h1>About: <xsl:value-of select="rdfs:label" /></h1>
		<h1>Type: <xsl:value-of select="rdf:type/@rdf:resource" /></h1>
		<br /><xsl:value-of select="."/><br />	
	</xsl:template>
</xsl:stylesheet>