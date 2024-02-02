package br.com.cinemagazine.controller

import br.com.cinemagazine.dto.user.CreateUserRequestDTO
import br.com.cinemagazine.dto.user.UpdateUserRequestDTO
import br.com.cinemagazine.dto.user.UserDTO
import br.com.cinemagazine.service.UserService
import jakarta.validation.Valid
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

    @PostMapping
    fun createUser(@RequestBody @Valid body: CreateUserRequestDTO): ResponseEntity<UserDTO> {
        return ResponseEntity.status(CREATED).body(userService.createUser(body))
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.getUser(id))
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: String, @RequestBody @Valid body: UpdateUserRequestDTO): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.updateUser(id, body))
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }

}