package az.edu.turing.controller;

import az.edu.turing.exception.GlobalErrorResponse;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.model.constants.ErrorCode;
import az.edu.turing.model.dto.request.CreateUserRequest;
import az.edu.turing.model.dto.request.UpdateUserRequest;
import az.edu.turing.model.enums.UserStatus;
import az.edu.turing.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


import java.util.Set;

import static az.edu.turing.constants.TestConstants.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_Should_ReturnSuccess() throws Exception {
        given(userService.create(CREATE_USER_REQUEST)).willReturn(USER_DTO);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CREATE_USER_REQUEST))
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO)))
                .andDo(print());
        then(userService).should(times(1)).create(CREATE_USER_REQUEST);
    }

    @Test
    void create_Should_Return400_When_PasswordDontMatch() throws Exception {
        CreateUserRequest createRequest = CreateUserRequest.builder()
                .username("user@gmail.com")
                .password("Test123!")
                .confirmPassword("User123!")
                .build();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_INPUT))
                .andExpect(jsonPath("$.errorMessage").value("Passwords do not match"))
                .andDo(print());
        then(userService).shouldHaveNoInteractions();
    }

    @Test
    void getAll_Should_ReturnSuccess() throws Exception {
        given(userService.findAll()).willReturn(Set.of(USER_DTO));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Set.of(USER_DTO))))
                .andDo(print());

        then(userService).should(times(1)).findAll();
    }

    @Test
    void getByUserName_Should_ReturnSuccess() throws Exception {
        given(userService.findByUsername(USERNAME)).willReturn(USER_DTO);

        mockMvc.perform(get(BASE_URL + "/{username}", USERNAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(USER_DTO)))
                .andDo(print());

        then(userService).should(times(1)).findByUsername(USERNAME);

    }

    @Test
    void getByUserName_Should_Return404_When_UsernameNotFound() throws Exception {
        GlobalErrorResponse notFoundErrorResponse = GlobalErrorResponse.builder()
                .requestId(null)
                .timeStamp(null)
                .errorCode(ErrorCode.NOT_FOUND)
                .errorMessage("There is not user with username " + USERNAME)
                .build();

        given(userService.findByUsername(USERNAME)).willThrow(NotFoundException.class);

        mockMvc.perform(get(BASE_URL + "/{username}", USERNAME))
                .andExpect(status().isNotFound())
//                .andExpect(content().json(objectMapper.writeValueAsString(notFoundErrorResponse)))
                .andDo(print());

        then(userService).should(times(1)).findByUsername(USERNAME);
    }

    @Test
    void getByUserName_Should_Return400_When_InvalidUsernameFormat() throws Exception {
        String invalidUsername = "invalid-username";

        mockMvc.perform(get(BASE_URL + "/{username}", invalidUsername)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.BAD_REQUEST))
                .andExpect(jsonPath("$.errorMessage").exists())
                .andDo(print());

        then(userService).shouldHaveNoInteractions();
    }

    @Test
    void update_Should_ReturnSuccess() throws Exception {

        given(userService.update(1L, UPDATE_USER_REQUEST)).willReturn(UPDATED_USER_DTO);

        mockMvc.perform(put(BASE_URL + "/{id}", ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UPDATE_USER_REQUEST)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(UPDATED_USER_DTO)))
                .andDo(print());

        then(userService).should(times(1)).update(1, UPDATE_USER_REQUEST);
    }

    @Test
    void update_Should_Return404_When_UserNotFound() throws Exception {

        long wrongId = 12;

        given(userService.update(wrongId, UPDATE_USER_REQUEST)).willThrow(
                new NotFoundException("There is not user with id " + wrongId)
        );

        mockMvc.perform(put(BASE_URL + "/{id}", wrongId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UPDATE_USER_REQUEST)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.NOT_FOUND))
                .andExpect(jsonPath("$.errorMessage").exists())
                .andDo(print());

        then(userService).should(times(1)).update(wrongId, UPDATE_USER_REQUEST);
    }

    @Test
    void update_Should_Return400_When_RequestBodyIsInvalid() throws Exception {

        String invalidUsername = "";
        String invalidPassword = "invalid_password";
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .username(invalidUsername)
                .password(invalidPassword)
                .build();

        mockMvc.perform(put(BASE_URL + "/{id}", ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.BAD_REQUEST))
                .andExpect(jsonPath("$.errorMessage").exists())
                .andDo(print());

        then(userService).shouldHaveNoInteractions();
    }

    @Test
    void updateStatus_Should_ReturnSuccess() throws Exception {
        UPDATED_USER_DTO.setStatus(UserStatus.INACTIVATE);

        given(userService.updateStatus(ID_2, UserStatus.INACTIVATE)).willReturn(UPDATED_USER_DTO);

        mockMvc.perform(patch(BASE_URL + "/{id}", ID_2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", UserStatus.INACTIVATE.name())
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(UPDATED_USER_DTO)))
                .andDo(print());

        then(userService).should(times(1)).updateStatus(ID_2, UserStatus.INACTIVATE);
    }

    @Test
    void updateStatus_Should_Return404_When_UserNotFound() throws Exception {
        long wrongId = 12;

        given(userService.updateStatus(wrongId, UserStatus.INACTIVATE))
                .willThrow(new NotFoundException("There is not user with id " + wrongId));

        mockMvc.perform(patch(BASE_URL + "/{id}", wrongId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", UserStatus.INACTIVATE.name())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.NOT_FOUND))
                .andExpect(jsonPath("$.errorMessage").exists())
                .andDo(print());

        then(userService).should(times(1)).updateStatus(wrongId, UserStatus.INACTIVATE);
    }

    @Test
    void updateStatus_Should_ReturnBadRequest_When_StatusIsNull() throws Exception {
        long userId = 1L;

        mockMvc.perform(patch(BASE_URL + "/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        then(userService).shouldHaveNoInteractions();
    }

    @Test
    void delete_Should_ReturnNoContent_When_Successful() throws Exception {
        long userId = 1L;

        willDoNothing().given(userService).deleteById(userId);

        mockMvc.perform(delete(BASE_URL + "/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(MockMvcResultHandlers.print());

        then(userService).should(times(1)).deleteById(userId);
    }
}
