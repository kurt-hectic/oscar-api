package wmo;
import java.util.Date;

import javax.xml.transform.TransformerException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.xml.sax.SAXException;

import wmo.StationNotFoundException;
import wmo.beans.ExceptionResponse; ;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
    ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
        request.getDescription(false));
    return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(StationNotFoundException.class)
  public final ResponseEntity<Object> handleStationNotFoundException(StationNotFoundException ex, WebRequest request) {
    ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
        request.getDescription(false));
    return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
  }
  
  @Override
  protected ResponseEntity<java.lang.Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
          HttpHeaders headers,
          HttpStatus status,
          WebRequest request) {
	  if (ex.contains(SAXException.class)) {
		  ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "XML validation failed",
			        ex.getMessage());
			    return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	  } 
	  else if ( ex.contains(TransformerException.class) ) { 
		  ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "XML transformation failed",
			        ex.getMessage());
			    return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
		  
	  }
	  else {
		  return super.handleHttpMessageNotReadable(ex, headers, status, request);
	  }
  }
  
  

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Domain validation failed",
        ex.getBindingResult().toString());
    return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
  } 
}