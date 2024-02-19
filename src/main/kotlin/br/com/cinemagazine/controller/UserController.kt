package br.com.cinemagazine.controller

import br.com.cinemagazine.dto.token.RefreshTokenRequestDTO
import br.com.cinemagazine.dto.token.TokenDTO
import br.com.cinemagazine.dto.user.CreateUserRequestDTO
import br.com.cinemagazine.dto.user.LoginDTO
import br.com.cinemagazine.dto.user.LoginRequestDTO
import br.com.cinemagazine.dto.user.UpdatePasswordRequestDTO
import br.com.cinemagazine.dto.user.UpdateUserRequestDTO
import br.com.cinemagazine.dto.user.UserDTO
import br.com.cinemagazine.service.TokenService
import br.com.cinemagazine.service.UserService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UserController(
    private val userService: UserService,
    private val tokenService: TokenService
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody body: LoginRequestDTO,
        @RequestHeader("user-agent") userAgent: String
    ): ResponseEntity<LoginDTO> {
        logger.info("UserController.login - Start - Input: email [{}], user-agent [{}]", body.email, userAgent)
        val login = userService.login(body, userAgent)
        logger.info("UserController.login - End - Input: email [{}], user-agent [{}]", body.email, userAgent)
        return ResponseEntity.ok(login)
    }

    @PostMapping("/refresh-token")
    fun refreshToken(
        @Valid @RequestBody body: RefreshTokenRequestDTO,
        @RequestHeader("user-agent") userAgent: String
    ): ResponseEntity<TokenDTO> {
        logger.info("UserController.refreshToken - Start - Input: user-agent [{}]", userAgent)
        val token = tokenService.validateRefreshToken(body, userAgent)
        logger.info("UserController.refreshToken - End - Input: user-agent [{}]", userAgent)
        return ResponseEntity.ok(token)
    }

    @PostMapping
    fun createUser(@Valid @RequestBody body: CreateUserRequestDTO): ResponseEntity<UserDTO> {
        logger.info("UserController.createUser - Start - Input: name [{}], email: [{}]", "${body.firstName} ${body.lastName}", body.email)
        val user = userService.createUser(body)
        logger.info("UserController.createUser - End - Input: name [{}], email: [{}] - Output: [{}]", "${body.firstName} ${body.lastName}", body.email, user)
        return ResponseEntity.status(CREATED).body(user)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): ResponseEntity<UserDTO> {
        logger.debug("UserController.getUser - Start - Input: id [{}]", id)
        val user = userService.getUser(id)
        logger.debug("UserController.getUser - End - Input: id [{}] - Output: [{}]", id, user)
        return ResponseEntity.ok(user)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: String, @Valid @RequestBody body: UpdateUserRequestDTO): ResponseEntity<UserDTO> {
        logger.info("UserController.updateUser - Start - Input: id [{}], body [{}]", id, body)
        val user = userService.updateUser(id, body)
        logger.info("UserController.updateUser - End - Input: id [{}], body [{}] - Output: [{}]", id, body, user)
        return ResponseEntity.ok(user)
    }

    @PutMapping("/{id}/password")
    fun updatePassword(@PathVariable id: String, @Valid @RequestBody body: UpdatePasswordRequestDTO): ResponseEntity<UserDTO> {
        logger.info("UserController.updatePassword - Start - Input: id [{}]", id)
        val user = userService.updatePassword(id, body)
        logger.info("UserController.updatePassword - End - Input: id [{}]", id)
        return ResponseEntity.ok(user)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Void> {
        logger.info("UserController.updateUser - Start - Input: id [{}]", id)
        userService.deleteUser(id)
        logger.info("UserController.updateUser - End - Input: id [{}]", id)
        return ResponseEntity.noContent().build()
    }

}