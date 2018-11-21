package wmo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wmo.beans.Station;

@Component
public class WMDSConverter extends AbstractHttpMessageConverter<Station> {


	ObjectMapper xmlMapper; 
	Transformer transformer; 
	Transformer back_transformer;
	Validator validator;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	boolean doValidation;


	@Autowired 
	public WMDSConverter(@Value("${doValidation}") final boolean doValidation ) throws TransformerConfigurationException, MalformedURLException, SAXException {
		super(new MediaType("application", "wmdr"));
		xmlMapper  = new XmlMapper( );
		xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		xmlMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd")) ; //FIXME: this should better be configured with a bean and autoconfigured

		TransformerFactory tFactory  = TransformerFactory.newInstance();

		transformer = tFactory.newTransformer( new StreamSource("transformations/xml2wmdr.xsl"));
		back_transformer = tFactory.newTransformer( new StreamSource("transformations/wmdr2xml.xsl"));

		this.doValidation=doValidation;

		if (doValidation) {
			logger.info("preparing schema");

			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(new URL("http://schemas.wmo.int/wmdr/1.0RC8/wmdr.xsd"));

			validator = schema.newValidator();

			logger.info("schema validator created");
		}

	}


	@Override
	protected boolean supports(Class<?> clazz) {
		return Station.class.isAssignableFrom(clazz);
	}

	@Override
	protected Station readInternal(Class<? extends Station> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		String requestBody = toString(inputMessage.getBody());

		StringReader reader = new StringReader(  requestBody );
		Station station = null;

		try {

			if (this.doValidation) {
				logger.info("validating schema of input");
				validator.validate(new StreamSource(reader));
				logger.info("schema validated");
			}

			StringWriter writer = new StringWriter();

			back_transformer.transform(
					new StreamSource( new StringReader(requestBody)  ) , 
					new StreamResult(writer));

			station = xmlMapper.readValue(writer.toString(), Station.class);

		} catch (TransformerException | SAXException e) {
			throw new HttpMessageNotReadableException(e.getMessage(),e);
		}

		return station;
	}

	@Override
	protected void writeInternal(Station station, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		try {
			OutputStream outputStream = outputMessage.getBody();

			StringReader reader = new StringReader(  xmlMapper.writeValueAsString(station) );
			StringWriter writer = new StringWriter();

			transformer.transform(
					new StreamSource(reader), 
					new StreamResult(writer));

			outputStream.write(writer.toString().getBytes() );
			outputStream.close();
		} catch (Exception e) {
		}
	}

	private static String toString(InputStream inputStream) {
		Scanner scanner = new Scanner(inputStream, "UTF-8");
		String ret = scanner.useDelimiter("\\A").next();
		scanner.close();
		return ret;
	}

}
