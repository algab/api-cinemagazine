package br.com.cinemagazine.service.impl

import br.com.cinemagazine.constants.ApiMessage.EMAIL_ALREADY_EXISTS
import br.com.cinemagazine.constants.ApiMessage.USER_NOT_FOUND
import br.com.cinemagazine.document.UserDocument
import br.com.cinemagazine.dto.user.CreateUserRequestDTO
import br.com.cinemagazine.dto.user.UpdateUserRequestDTO
import br.com.cinemagazine.dto.user.UserDTO
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.repository.UserRepository
import br.com.cinemagazine.service.UserService
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
): UserService {

    override fun createUser(data: CreateUserRequestDTO): UserDTO {
        validateEmail(data.email)
        val user = UserDocument(
            ObjectId().toString(),
            data.firstName,
            data.lastName,
            data.email,
            passwordEncoder.encode(data.password),
            data.gender,
            LocalDateTime.now(),
            LocalDateTime.now()
        )
        val userSaved = userRepository.save(user)
        return UserDTO(userSaved.id, user.firstName, user.lastName, user.email, user.gender)
    }

    override fun getUser(id: String): UserDTO {
        val user = userRepository.findById(id).orElseThrow{ BusinessException(NOT_FOUND, USER_NOT_FOUND) }
        return UserDTO(user.id, user.firstName, user.lastName, user.email, user.gender)
    }

    override fun updateUser(id: String, data: UpdateUserRequestDTO): UserDTO {
        val user = userRepository.findById(id).orElseThrow{ BusinessException(NOT_FOUND, USER_NOT_FOUND) }
        if (user.email != data.email) {
            validateEmail(data.email)
        }
        val userUpdated = UserDocument(
            user.id,
            data.firstName,
            data.lastName,
            data.email,
            user.password,
            data.gender,
            user.createdDate,
            LocalDateTime.now()
        )
        userRepository.save(userUpdated)
        return UserDTO(userUpdated.id, userUpdated.firstName, userUpdated.lastName, userUpdated.email, userUpdated.gender)
    }

    override fun deleteUser(id: String) {
        val user = userRepository.findById(id).orElseThrow{ BusinessException(NOT_FOUND, USER_NOT_FOUND) }
        userRepository.delete(user)
    }

    private fun validateEmail(email: String) {
        val existEmail = userRepository.existsByEmail(email)
        if (existEmail) {
            throw BusinessException(CONFLICT, EMAIL_ALREADY_EXISTS)
        }
    }
}