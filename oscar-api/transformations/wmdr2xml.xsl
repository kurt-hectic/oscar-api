<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:wmdr="http://def.wmo.int/wmdr/2017" xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:om="http://www.opengis.net/om/2.0" xmlns:xlink="http://www.w3.org/1999/xlink">
	<xsl:template match="/">
		<station>
			<name>
				<xsl:value-of select="/wmdr:WIGOSMetadataRecord/wmdr:facility/wmdr:ObservingFacility/gml:name"/>
			</name>
			<established>
				<xsl:value-of select="/wmdr:WIGOSMetadataRecord/wmdr:facility/wmdr:ObservingFacility/wmdr:dateEstablished"/>
			</established>
			<observations>
				<xsl:for-each select="/wmdr:WIGOSMetadataRecord/wmdr:facility/wmdr:ObservingFacility/wmdr:observation">
					<observations>
						<variable>
							<xsl:value-of select="/wmdr:WIGOSMetadataRecord/wmdr:facility/wmdr:ObservingFacility/wmdr:observation/wmdr:ObservingCapability/wmdr:observation/om:OM_Observation/om:observedProperty/@xlink:href"/>
						</variable>
						<deployment>
							<beginning>
								<xsl:value-of select="wmdr:ObservingCapability/wmdr:observation/om:OM_Observation/om:phenomenonTime/gml:TimePeriod/gml:beginPosition"/>
							</beginning>
							<end>
								<xsl:value-of select="wmdr:ObservingCapability/wmdr:observation/om:OM_Observation/om:phenomenonTime/gml:TimePeriod/gml:endPosition"/>
							</end>
							<observationsource>
							</observationsource>
							<schedules>
								<xsl:for-each select="wmdr:ObservingCapability/wmdr:observation/om:OM_Observation/om:procedure/wmdr:Process/wmdr:deployment/wmdr:Deployment/wmdr:dataGeneration">
									<schedules>
										<monthfrom>
											<xsl:value-of select="wmdr:DataGeneration/wmdr:schedule/wmdr:Schedule/wmdr:startMonth"/>
										</monthfrom>
										<monthto>
											<xsl:value-of select="wmdr:DataGeneration/wmdr:schedule/wmdr:Schedule/wmdr:endMonth"/>
										</monthto>
										<dayfrom>
											<xsl:value-of select="wmdr:DataGeneration/wmdr:schedule/wmdr:Schedule/wmdr:startWeekday"/>
										</dayfrom>
										<dayto>
											<xsl:value-of select="wmdr:DataGeneration/wmdr:schedule/wmdr:Schedule/wmdr:endWeekday"/>
										</dayto>
										<hourfrom>
											<xsl:value-of select="wmdr:DataGeneration/wmdr:schedule/wmdr:Schedule/wmdr:startHour"/>
										</hourfrom>
										<hourto>
											<xsl:value-of select="wmdr:DataGeneration/wmdr:schedule/wmdr:Schedule/wmdr:endMonth"/>
										</hourto>
										<minutefrom>
											<xsl:value-of select="wmdr:DataGeneration/wmdr:schedule/wmdr:Schedule/wmdr:startMinute"/>
										</minutefrom>
										<minuteto>
											<xsl:value-of select="wmdr:DataGeneration/wmdr:schedule/wmdr:Schedule/wmdr:endMinute"/>
										</minuteto>
										<interval>
											<xsl:value-of select="substring(substring(wmdr:DataGeneration/wmdr:reporting/wmdr:Reporting/wmdr:temporalReportingInterval,3),1, string-length(wmdr:DataGeneration/wmdr:reporting/wmdr:Reporting/wmdr:temporalReportingInterval)-3  )"/>
										</interval>
										<international>
											<xsl:value-of select="wmdr:DataGeneration/wmdr:reporting/wmdr:Reporting/wmdr:internationalExchange"/>
										</international>
									</schedules>
								</xsl:for-each>
							</schedules>
						</deployment>
					</observations>
				</xsl:for-each>
			</observations>
			<locations>
				<xsl:for-each select="/wmdr:WIGOSMetadataRecord/wmdr:facility/wmdr:ObservingFacility/wmdr:geospatialLocation">
					<locations>
						<xsl:call-template name="processPoint">
							<xsl:with-param name="list">
								<xsl:value-of select="wmdr:GeospatialLocation/wmdr:geoLocation/gml:Point/gml:pos"/>
							</xsl:with-param>
						</xsl:call-template>
						<datefrom><xsl:value-of select="wmdr:GeospatialLocation/wmdr:validPeriod/gml:TimePeriod/gml:beginPosition"/></datefrom>
						<dateto><xsl:value-of select="wmdr:GeospatialLocation/wmdr:validPeriod/gml:TimePeriod/gml:endPosition"/></dateto>
					</locations>
				</xsl:for-each>
			</locations>
			<countryCode><xsl:value-of select="substring-after( /wmdr:WIGOSMetadataRecord/wmdr:facility/wmdr:ObservingFacility/wmdr:territory/wmdr:Territory/wmdr:territoryName/@xlink:href, 'TerritoryName/' )"/></countryCode>
			<region><xsl:value-of select="substring-after( /wmdr:WIGOSMetadataRecord/wmdr:facility/wmdr:ObservingFacility/wmdr:wmoRegion/@xlink:href, 'WMORegion/' )"/></region>
			<wigosID>
				<wigosID><xsl:value-of select="/wmdr:WIGOSMetadataRecord/wmdr:facility/wmdr:ObservingFacility/gml:identifier"/></wigosID>
				<primary>true</primary>
			</wigosID>
		</station>
	</xsl:template>
	<xsl:template name="processPoint">
		<xsl:param name="list"/>
		<xsl:variable name="newlist" select="concat(normalize-space($list), ' ')"/>
		<xsl:variable name="latitude" select="substring-before($newlist, ' ')"/>
		<xsl:variable name="remaining" select="substring-after($newlist, ' ')"/>
		<xsl:variable name="longitude" select="substring-before($remaining, ' ')"/>
		<xsl:variable name="elevation" select="substring-after($remaining, ' ')"/>
		<longitude>
			<xsl:value-of select="$latitude"/>
		</longitude>
		<latitude>
			<xsl:value-of select="$longitude"/>
		</latitude>
		<elevation>
			<xsl:value-of select="$elevation"/>
		</elevation>
	</xsl:template>
</xsl:stylesheet>
