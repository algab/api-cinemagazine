package br.com.cinemagazine.controllers.integration

import br.com.cinemagazine.controllers.UserController
import br.com.cinemagazine.dto.user.CreateUserRequestDTO
import br.com.cinemagazine.exception.handler.ExceptionHandler
import br.com.cinemagazine.services.TokenService
import br.com.cinemagazine.services.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.mockk.mockk
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class UserControllerIntegrationTest: FunSpec({

    val userService = mockk<UserService>()
    val tokenService = mockk<TokenService>()
    val mockMvc = MockMvcBuilders
        .standaloneSetup(UserController(userService, tokenService))
        .setControllerAdvice(ExceptionHandler())
        .build()

    test("should validate the request fields successfully") {
        val body = CreateUserRequestDTO(null, "test", "test", "1234", "Masculine")

        val request = MockMvcRequestBuilders.post("/v1/users")
            .contentType(APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(body))

        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(BAD_REQUEST.reasonPhrase))
    }
})