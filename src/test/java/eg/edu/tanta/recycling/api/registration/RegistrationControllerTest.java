package eg.edu.tanta.recycling.api.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import eg.edu.tanta.recycling.api.ApiResponse;
import eg.edu.tanta.recycling.api.dto.UserDto;
import eg.edu.tanta.recycling.domain.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.util.Locale;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RegistrationController.class)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class RegistrationControllerTest {


  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private RegistrationService registrationService;

  @MockBean
  private UserService userService;

  @MockBean
  private HttpServletRequest request;

  public static final String TOKEN_VALID = "valid";



  private final UserDto userDto = new UserDto("username", "user@email.com", "password", "01234567891");

  String successfullyRegistrationResponse = """
  {
    "status": 201,
    "message": "please check your email and click on the verification link."
   }""";


  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @BeforeEach
  public void setUp(RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(documentationConfiguration(restDocumentation))
        .alwaysDo(document("{method-name}",
            preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
        .build();
  }


  @Test
  public void givenValidUserDtoThenReturn201CreatedAndLocationHeader() throws Exception {
    when(registrationService.registerUser(any(), any())).thenReturn(1L);


    mockMvc.perform(post("/api/register")
          .content(mapper.writeValueAsBytes(userDto))
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andExpect(header().string(HttpHeaders.LOCATION, "/api/users/1"))
        .andExpect(content().json(successfullyRegistrationResponse));

    verify(registrationService, times(1)).registerUser(any(), any());
  }


  @Test
  public void givenAValidConfirmationTokenReturn200() throws Exception {
    final String validToken = UUID.randomUUID().toString();

    when(userService.validateVerificationToken(validToken)).thenReturn(new ApiResponse<>(200, TOKEN_VALID, null));
    when(request.getLocale()).thenReturn(Locale.US);

    mockMvc.perform(get("/api/registrationConfirmation")
            .param("token", validToken)
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.status", Matchers.is(200)))
        .andExpect(jsonPath("$.message", Matchers.is(TOKEN_VALID)));

  }
}