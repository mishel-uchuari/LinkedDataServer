<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" 
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
	xmlns:qb="http://purl.org/linked-data/sdmx/2009/measure#">
	<xsl:output method="html" encoding="utf-8" />
	<xsl:template match="/">
		<html>
			<body>
					<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>
	<xsl:template match="rdf:RDF//rdf:Description">
		<p>About: <xsl:value-of select="rdfs:label" /></p>
		<p>Value: <xsl:value-of select="qb:obsValue" /></p>
		<p>Type: <xsl:value-of select="rdf:type/@rdf:resource" /></p>
	</xsl:template>
</xsl:stylesheet>