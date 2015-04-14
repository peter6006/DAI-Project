<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:r="http://www.esei.uvigo.es/dai/proyecto">

	<xsl:template match="/">
		<html>
			<head>
				<META http-equiv="Content-Type" content="text/html; charset=utf-8" />
				<title>HTML</title>
			</head>
			<body>
				<div id="container">
					<h1>Documento HTML</h1>
					<div id="configuration">
						<xsl:apply-templates select="r:configuration" />
					</div>
				</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="r:connections">

		<h1>Connections</h1>
		<div class="connections">
			<ul>
				<li>
					<strong>http: </strong>
					<xsl:value-of select="r:http" />
				</li>
				<li>
					<strong>Servicio Web: </strong>
					<xsl:value-of select="r:webservice" />
				</li>
				<li>
					<strong>Numero de clientes: </strong>
					<xsl:value-of select="r:numClients" />
				</li>
			</ul>
		</div>
	</xsl:template>

	<xsl:template match="r:database">
		<div class="database">
			<h4>Database</h4>
			<ul>
				<li>
					<strong>User: </strong>
					<xsl:value-of select="r:user" />
				</li>
				<li>
					<strong>Password: </strong>
					<xsl:value-of select="r:password" />
				</li>
				<li>
					<strong>Url: </strong>
					<xsl:value-of select="r:url" />
				</li>
			</ul>
		</div>
	</xsl:template>

	<xsl:template match="r:servers">
		<div class="servers">
			<h4>Servers</h4>

			<xsl:for-each select="r:server">

				<li>
					<strong>Nombre: </strong>
					<xsl:value-of select="@name"></xsl:value-of>
				</li>
				<strong>wsdl: </strong>
				<xsl:value-of select="@wsdl"></xsl:value-of>
				<br></br>
				<strong>Namespace: </strong>
				<xsl:value-of select="@namespace"></xsl:value-of>
				<br></br>
				<strong>Service: </strong>
				<xsl:value-of select="@service"></xsl:value-of>
				<br></br>
				<strong>HTTPAdress: </strong>
				<xsl:value-of select="@httpAddress"></xsl:value-of>
				<br></br><br></br>

			</xsl:for-each>

		</div>
	</xsl:template>
</xsl:stylesheet>