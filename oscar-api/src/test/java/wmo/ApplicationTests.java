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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import wmo.StationRepository;
import wmo.beans.Deployment;
import wmo.beans.Location;
import wmo.beans.Observation;
import wmo.beans.Schedule;
import wmo.beans.Station;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.xml.xpath.XPathExpressionException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private StationRepository stationRepository = org.mockito.Mockito.mock(StationRepository.class);

	
	private static final Map<String,String> namespace; 
	static {
		namespace = new HashMap<String,String>();
		namespace.put("wmdr", "http://def.wmo.int/wmdr/2017");
		namespace.put("gml", "http://www.opengis.net/gml/3.2");
	}


	@Before
	public void deleteAllBeforeTests() throws Exception {
		stationRepository.deleteAll();
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

		return Optional.of(s);
	}

	public void shouldReturnStation() throws XPathExpressionException, Exception {


		//TODO: create data


		mockMvc.perform(  get("/stations/1").accept("application/xml") ).andExpect(status().isOk())
		.andExpect( xpath("/station/name/text()").string(containsString("Timo"))  );

	}

	@Test
	public void shouldReturnStationInWMDS() throws XPathExpressionException, Exception {


		when(stationRepository.findById(1L)).thenReturn(createValidStation());


		mockMvc.perform(  get("/stations/{id}",1L).accept("application/wmds") ).andExpect(status().isOk())
		.andExpect( xpath(
				"/wmdr:WIGOSMetadataRecord/wmdr:facility/wmdr:ObservingFacility/gml:name/text()",
				namespace
				).string(containsString("Timo"))  );

	}

	@Test
	public void shouldCreateEntity() throws Exception {

		byte[] xml = readFile("test-files/new-station.xml");

		mockMvc.perform(post("/stations").contentType("application/xml").content(
				xml )).andExpect(
						status().isCreated()).andExpect(
								header().string("Location", containsString("stations/")));
	}

	@Test
	public void shouldCreateEntityFromWDMS() throws Exception {

		byte[] xml = readFile("test-files/wmds-file.xml");

		mockMvc.perform(post("/stations").contentType("application/wmds").content(
				xml )).andExpect(
						status().isCreated()).andExpect(
								header().string("Location", containsString("stations/")));
	}

	@Test
	public void shouldNotCreateEntityFromWDMS() throws Exception {

		byte[] xml = readFile("test-files/wmds-file-error.xml");

		mockMvc.perform(post("/stations").contentType("application/wmds").content(
				xml )).andExpect(
						status().isBadRequest()).andExpect( jsonPath("$.message")
								.value("XML validation failed")).andExpect(jsonPath("$.details",  org.hamcrest.Matchers.containsString("cvc-datatype-valid.1.2.3: '2017-02-24T23:00:00.000+0000' is not")  )) ;
	}


	@Test
	public void shouldCreateEntityAndReturCorrectly() throws Exception {

		byte[] xml = readFile("test-files/new-station.xml");

		MvcResult res = mockMvc.perform(post("/stations").contentType("application/xml").content(
				xml )).andExpect(
						status().isCreated()).andExpect(
								header().string("Location", containsString("stations/"))).andReturn();

		String resource = res.getResponse().getHeader("Location");
		String[] tmp = resource.split("/");
		String id = tmp[tmp.length-1];

		mockMvc.perform(get( "/stations/"+id ).accept("application/xml") ).andExpect(xpath("/station/name/text()" ).string(  containsString("Timo") )   )
		.andExpect(xpath("/station/observations/observations/deployment/beginning/text()  ").string(  containsString("2017-02-24T23:00:00.000+0000") )  );

	}


	@Test
	public void shouldNotCreateEntity() throws Exception {

		byte[] xml = readFile("test-files/new-station-failure.xml");

		mockMvc.perform(post("/stations").contentType("application/xml").content(
				xml )).andExpect(
						status().isBadRequest() ).andExpect( jsonPath("$.message")
								.value("Domain validation failed")).andExpect(jsonPath("$.details",  org.hamcrest.Matchers.containsString("cannot be in name")  )) ;
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