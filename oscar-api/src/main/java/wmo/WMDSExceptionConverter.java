package wmo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import wmo.beans.ExceptionResponse;

@Component
public class WMDSExceptionConverter extends AbstractHttpMessageConverter<ExceptionResponse> {

	private XmlMapper xmlMapper ;

	public WMDSExceptionConverter() {
		super(new MediaType("application", "wmdr"));
		this.xmlMapper  = new XmlMapper( );
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return ExceptionResponse.class.isAssignableFrom(clazz);
	}

	@Override
	protected void writeInternal(ExceptionResponse er, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		
		
		try {
			OutputStream outputStream = outputMessage.getBody();
			outputStream.write(  xmlMapper.writeValueAsString(er).getBytes() );
			outputStream.close();
		} catch (Exception e) {
		}
		
		
	}

	@Override
	protected ExceptionResponse readInternal(Class<? extends ExceptionResponse> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
