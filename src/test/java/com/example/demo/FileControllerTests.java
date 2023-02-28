package com.example.demo;

import com.example.demo.controllers.FileController;
import com.example.demo.entities.FileEntity;
import com.example.demo.services.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(FileController.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class FileControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", "This is a test file content".getBytes());
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName("test.txt");
        fileEntity.setContent("This is a test file content".getBytes());
        fileEntity.setId(1L);
        when(fileService.save(file)).thenReturn(fileEntity);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/file/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.mostOccurringLetter").value("t"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("File test.txt uploaded successfully."));
    }

    @Test
    public void testUploadFileWithInvalidExtension() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.doc",
                "text/plain", "This is a test file content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/file/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error uploading file: File extension is not allowed"));
    }

    @Test
    public void testUploadEmptyFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", new byte[0]);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/file/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error uploading file: File is empty"));
    }

}
