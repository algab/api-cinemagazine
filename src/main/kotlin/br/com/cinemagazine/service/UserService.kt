package br.com.cinemagazine.service

import br.com.cinemagazine.dto.user.CreateUserRequestDTO
import br.com.cinemagazine.dto.user.UpdateUserRequestDTO
import br.com.cinemagazine.dto.user.UserDTO

interface UserService {
    fun createUser(data: CreateUserRequestDTO): UserDTO
    fun getUser(id: String): UserDTO
    fun updateUser(id: String, data: UpdateUserRequestDTO): UserDTO
    fun deleteUser(id: String)
}