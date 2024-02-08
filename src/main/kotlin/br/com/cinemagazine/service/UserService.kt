package br.com.cinemagazine.service

import br.com.cinemagazine.dto.user.CreateUserRequestDTO
import br.com.cinemagazine.dto.user.LoginDTO
import br.com.cinemagazine.dto.user.LoginRequestDTO
import br.com.cinemagazine.dto.user.UpdatePasswordRequestDTO
import br.com.cinemagazine.dto.user.UpdateUserRequestDTO
import br.com.cinemagazine.dto.user.UserDTO

interface UserService {
    fun login(data: LoginRequestDTO): LoginDTO
    fun createUser(data: CreateUserRequestDTO): UserDTO
    fun getUser(id: String): UserDTO
    fun updateUser(id: String, data: UpdateUserRequestDTO): UserDTO
    fun updatePassword(id: String, data: UpdatePasswordRequestDTO): UserDTO
    fun deleteUser(id: String)
}