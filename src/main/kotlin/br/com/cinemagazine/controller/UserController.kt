package br.com.cinemagazine.controller

import br.com.cinemagazine.dto.user.CreateUserRequestDTO
import br.com.cinemagazine.dto.user.LoginDTO
import br.com.cinemagazine.dto.user.LoginRequestDTO
import br.com.cinemagazine.dto.user.UpdatePasswordRequestDTO
import br.com.cinemagazine.dto.user.UpdateUserRequestDTO
import br.com.cinemagazine.dto.user.UserDTO
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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UserController(private val userService: UserService) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("/login")
    fun login(@RequestBody @Valid body: LoginRequestDTO): ResponseEntity<LoginDTO> {
        logger.info("UserController.login - Start - Input: email [{}]", body.email)
        val login = userService.login(body)
        logger.info("UserController.login - End - Input: email [{}]", body.email)
        return ResponseEntity.ok(login)
    }

    @PostMapping
    fun createUser(@RequestBody @Valid body: CreateUserRequestDTO): ResponseEntity<UserDTO> {
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
    fun updateUser(@PathVariable id: String, @RequestBody @Valid body: UpdateUserRequestDTO): ResponseEntity<UserDTO> {
        logger.info("UserController.updateUser - Start - Input: id [{}], body [{}]", id, body)
        val user = userService.updateUser(id, body)
        logger.info("UserController.updateUser - End - Input: id [{}], body [{}] - Output: [{}]", id, body, user)
        return ResponseEntity.ok(user)
    }

    @PutMapping("/{id}/password")
    fun updatePassword(@PathVariable id: String, @RequestBody @Valid body: UpdatePasswordRequestDTO): ResponseEntity<UserDTO> {
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