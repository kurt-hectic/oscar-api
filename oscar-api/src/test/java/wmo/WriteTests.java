/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wmo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import wmo.StationRepository;
import wmo.beans.Deployment;
import wmo.beans.Location;
import wmo.beans.Observation;
import wmo.beans.Schedule;
import wmo.beans.Station;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.xpath.XPathExpressionException;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"doValidation=TRUE"})
@AutoConfigureMockMvc
public class WriteTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StationRepository stationRepository ;
	

	private static final Map<String,String> namespace; 
	static {
		namespace = new HashMap<String,String>();
		namespace.put("wmdr", "http://def.wmo.int/wmdr/2017");
		namespace.put("gml", "http://www.opengis.net/gml/3.2");
	}
	
	
	@Before
	public void init() throws Exception {
		//MockitoAnnotations.initMocks(this);
        //mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		stationRepository.deleteAll();
	}
	
	@Test
	public void shouldCreateEntityJson() throws Exception
	{
		
		String json = "{\"name\":\"Timo's test station\",\"established\":\"2017-02-24\",\"observations\":[{\"variable\":224,\"deployment\":{\"beginning\":\"2017-02-24T23:00:00.000+0000\",\"end\":null,\"observationsource\":\"automaticReading\",\"schedules\":[{\"monthfrom\":12,\"monthto\":0,\"dayfrom\":1,\"dayto\":7,\"hourfrom\":0,\"hourto\":23,\"minutefrom\":0,\"minuteto\":59,\"interval\":60,\"international\":true}]}}],\"locations\":[{\"longitude\":10.1,\"latitude\":20.2,\"elevation\":123.456,\"datefrom\":\"2017-02-24T23:00:00.000+0000\",\"dateto\":null}],\"countryCode\":\"DEU\",\"region\":\"Europe\",\"wigosID\":{\"wigosID\":\"0-20000-1-12334\",\"primary\":true}}";

		Station s = new Station();
		s.setId(1L);
		
		when(stationRepository.save( org.mockito.ArgumentMatchers.any(Station.class))  )
			.thenReturn( s );

		
		mockMvc.perform(post("/stations").contentType("application/json").content(
				json )).andExpect(
						status().isCreated()).andExpect(
								header().string("Location", containsString("stations/")));	
	}
	
	@Test
	public void shouldNotCreateEntityJson() throws Exception
	{
		
		String json = "{ \"unknown\":\"unknown\",\"name\":\"Timo's test station\",\"established\":\"2017-02-24\",\"observations\":[{\"variable\":224,\"deployment\":{\"beginning\":\"2017-02-24T23:00:00.000+0000\",\"end\":null,\"observationsource\":\"automaticReading\",\"schedules\":[{\"monthfrom\":12,\"monthto\":0,\"dayfrom\":1,\"dayto\":7,\"hourfrom\":0,\"hourto\":23,\"minutefrom\":0,\"minuteto\":59,\"interval\":60,\"international\":true}]}}],\"locations\":[{\"longitude\":10.1,\"latitude\":20.2,\"elevation\":123.456,\"datefrom\":\"2017-02-24T23:00:00.000+0000\",\"dateto\":null}],\"countryCode\":\"DEU\",\"region\":\"Europe\",\"wigosID\":{\"wigosID\":\"0-20000-1-12334\",\"primary\":true}}";

		Station s = new Station();
		s.setId(1L);
		
		when(stationRepository.save( org.mockito.ArgumentMatchers.any(Station.class))  )
			.thenReturn( s );
		
		mockMvc.perform(post("/stations").contentType("application/json").content(
				json )).andExpect(
						status().isCreated()).andExpect(
								header().string("Location", containsString("stations/")));	
	}
	
	
	@Test
	public void shouldCreateEntity() throws Exception {

		byte[] xml = readFile("test-files/new-station.xml");

		Station s = new Station();
		s.setId(1L);
		when(stationRepository.save( org.mockito.ArgumentMatchers.any(Station.class))  ).thenReturn( s );
		
		mockMvc.perform(post("/stations").contentType("application/xml").content(
				xml )).andExpect(
						status().isCreated()).andExpect(
								header().string("Location", containsString("stations/")));
	}
	
	
	@Test
	public void shouldNotCreateEntityFormat() throws Exception {

		byte[] xml = readFile("test-files/new-station-failure-format.xml");

		Station s = new Station();
		s.setId(1L);
		when(stationRepository.save( org.mockito.ArgumentMatchers.any(Station.class))  ).thenReturn( s );
		
		mockMvc.perform(post("/stations").contentType("application/xml").content(
				xml )).andExpect(
						status().isBadRequest());
							
	}



	@Test
	public void shouldCreateEntityFromWDMS() throws Exception {

		byte[] xml = readFile("test-files/wmds-file.xml");

		Station s = new Station();
		s.setId(1L);
		when(stationRepository.save( org.mockito.ArgumentMatchers.any(Station.class))  ).thenReturn( s );
		
		mockMvc.perform(post("/stations").contentType("application/wmdr").content(
				xml )).andExpect(
						status().isCreated()).andExpect(
								header().string("Location", containsString("stations/")));
	}

	@Test
	public void shouldNotCreateEntityFromWDMS() throws Exception {

		byte[] xml = readFile("test-files/wmds-file-error.xml");

		Station s = new Station();
		s.setId(1L);
		when(stationRepository.save( org.mockito.ArgumentMatchers.any(Station.class))  ).thenReturn( s );

		mockMvc.perform(post("/stations").contentType("application/wmdr").content(
				xml )).andExpect(
						status().isBadRequest()).andExpect( xpath("/ExceptionResponse/message/text()").string(containsString("XML validation failed")))
								.andExpect(xpath("/ExceptionResponse/details/text()").string(containsString("cvc-datatype-valid.1.2.3: '2017-02-24T23:00:00.000+0000' is not")))  ;
	}




	@Test
	public void shouldNotCreateEntityValidation() throws Exception {

		byte[] xml = readFile("test-files/new-station-failure.xml");

		Station s = new Station();
		s.setId(1L);
		when(stationRepository.save( org.mockito.ArgumentMatchers.any(Station.class))  ).thenReturn( s );
		
		mockMvc.perform(post("/stations").accept("application/json").contentType("application/xml").content(
				xml )).andExpect(
						status().isBadRequest() ).andExpect( jsonPath("$.message")
								.value("Domain validation failed")).andExpect(jsonPath("$.details",  org.hamcrest.Matchers.containsString("cannot be in name")  )) ;
	}


	@Test
	public void shouldReturnStationByWigosID() throws XPathExpressionException, Exception {
		
		List<Station> list = new ArrayList<Station>();
		list.add( createValidStation().get()  );
		String wid = list.get(0).getWigosID().toString();
		
		when(stationRepository.findByWigosIDs_WigosID(wid)).thenReturn( list );
				
		mockMvc.perform(  get("/stations/bywigosid/{wid}",wid).accept("application/xml") ).andExpect(status().isOk())
		.andExpect( xpath("/station/name/text()").string(containsString("Timo"))  );

	}
	
	
	//@Test
	public void shouldThrowException() {
		
	}
	

	@Test
	public void shouldReturnRepositoryIndex() throws Exception {

		mockMvc.perform(get("/stations").accept("application/xml")  ).andDo(print()).andExpect(status().isOk())
		.andExpect(	 xpath("/List").exists()   );
	}

	private static Optional<Station> createValidStation() {
		Date established = new GregorianCalendar(2017, 1, 25).getTime() ;

		Station s = new Station( "Timo's test station", "0-20000-1-12334" , new Location(10.10f,20.20f,123.456f, established )  , established, "DEU", "Europe" );
		Observation o = new Observation( 224 ,  new Deployment( established, Schedule.getSchedule(Schedule.int247) ) );
		s.getObservations().add(o);
		s.setId(1L);

		return Optional.of(s);
	}

	@Test
	public void shouldReturnStation() throws XPathExpressionException, Exception {

		when(stationRepository.findById(1L)).thenReturn(createValidStation());

		mockMvc.perform(  get("/stations/{id}",1L).accept("application/xml") ).andExpect(status().isOk())
		.andExpect( xpath("/station/name/text()").string(containsString("Timo"))  );
	}

	@Test
	public void shouldReturnStationInWMDS() throws XPathExpressionException, Exception {

		when(stationRepository.findById(1L)).thenReturn(createValidStation());

		mockMvc.perform(  get("/stations/{id}",1L).accept("application/wmdr") )
		.andExpect(status().isOk());
		
		/*
		.andExpect( xpath(
				"/wmdr:WIGOSMetadataRecord/wmdr:facility/wmdr:ObservingFacility/gml:name/text()",
				namespace
				).string(containsString("Timo"))  );
		*/
	}

	@Test
	public void shouldReturnStationInJSON() throws XPathExpressionException, Exception {

		when(stationRepository.findById(1L)).thenReturn(createValidStation());

		mockMvc.perform(  get("/stations/{id}",1L).accept("application/json") ).andExpect(status().isOk())
		.andExpect( jsonPath("$.name")
				.value(containsString("Timo"))  );

	}
	



	private byte[] readFile(String path) throws IOException {

		File file = new File(path);
		FileInputStream fin = null;
		fin = new FileInputStream(file);
		byte fileContent[] = new byte[(int)file.length()];
		fin.read(fileContent);
		fin.close();

		return fileContent;

	}


}