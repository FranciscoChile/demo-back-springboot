package com.coopeuch.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.coopeuch.model.APIResponse;
import com.coopeuch.model.TaskDTO;
import com.coopeuch.service.TaskService;
import com.coopeuch.web.TaskController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

@WebMvcTest(value = TaskController.class)
class DemoApplicationTests {


	@Autowired
    private MockMvc mvc;

    @MockBean
    private TaskService taskService;


    @Test
    public void listAllTask_whenGetMethod()
            throws Exception {

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setDescription("description");

        List<TaskDTO> list = List.of(taskDTO);

        when(taskService.findAll()).thenReturn(list);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request)
                .andExpect(status().is2xxSuccessful())                
				.andReturn();
					
		String data = result.getResponse().getContentAsString();		
		ObjectMapper objectMapper = new ObjectMapper();
		APIResponse resp = objectMapper.readValue(data, APIResponse.class);
		
		List listData = (List) resp.getData();
		Map.Entry<String, String> entryTest = (Entry<String, String>) ((LinkedHashMap)listData.get(0)).entrySet().toArray()[1];

        assertNotNull(result.getResponse().getContentAsString());
		assertEquals(entryTest.getValue(), "description");
				
        }


    @Test
    public void getTaskById_whenGetMethod()
            throws Exception {

        TaskDTO taskDTO = new TaskDTO();
		taskDTO.setId(1L);
        taskDTO.setDescription("description");

        when(taskService.findById(1l)).thenReturn(taskDTO);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request)
                .andExpect(status().is2xxSuccessful())                
				.andReturn();
					
        assertNotNull(result.getResponse().getContentAsString());
				
        }


		@Test
    	public void deleteTaskById_whenDeleteMethod()
            throws Exception {

        TaskDTO taskDTO = new TaskDTO();
		taskDTO.setId(1L);
        taskDTO.setDescription("description");

        List<TaskDTO> list = List.of(taskDTO);

        when(taskService.findAll()).thenReturn(list);

        RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request)
                .andExpect(status().is2xxSuccessful())     
				.andExpect(jsonPath("$.message", Matchers.is("Successfully deleted")))           
				.andReturn();
					
        assertNotNull(result.getResponse().getContentAsString());
				
        }


		@Test
    	public void createTask_whenPostMethod()
            throws Exception {

        TaskDTO taskDTO = new TaskDTO();
		taskDTO.setId(1L);
        taskDTO.setDescription("description");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson=ow.writeValueAsString(taskDTO);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/tasks")
                .param("taskData", requestJson)
				//.content(requestJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request)
                .andExpect(status().is2xxSuccessful())        
				.andExpect(jsonPath("$.message", Matchers.is("Successfully created")))
				.andReturn();
					
        assertNotNull(result.getResponse().getContentAsString());
				
        }


		@Test
    	public void updateTask_whenPatchMethod()
            throws Exception {

        TaskDTO taskDTO = new TaskDTO();
		taskDTO.setId(1L);
        taskDTO.setDescription("description");

		when(taskService.findById(1l)).thenReturn(taskDTO);

        TaskDTO taskDTOToUpdate = new TaskDTO();
		taskDTO.setId(1L);
        taskDTO.setDescription("description2");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson=ow.writeValueAsString(taskDTOToUpdate);

        RequestBuilder request = MockMvcRequestBuilders
                .patch("/api/tasks")
                .param("taskData", requestJson)
				//.content(requestJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(request)
                .andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.message", Matchers.is("Successfully updated")))
				.andReturn();

        assertNotNull(result.getResponse().getContentAsString());
				
        }

}
