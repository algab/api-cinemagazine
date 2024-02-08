package br.com.cinemagazine.service.impl

import br.com.cinemagazine.constants.ApiMessage.EMAIL_ALREADY_EXISTS
import br.com.cinemagazine.constants.ApiMessage.USER_NOT_FOUND
import br.com.cinemagazine.document.UserDocument
import br.com.cinemagazine.dto.user.CreateUserRequestDTO
import br.com.cinemagazine.dto.user.LoginDTO
import br.com.cinemagazine.dto.user.LoginRequestDTO
import br.com.cinemagazine.dto.user.UpdatePasswordRequestDTO
import br.com.cinemagazine.dto.user.UpdateUserRequestDTO
import br.com.cinemagazine.dto.user.UserDTO
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.repository.UserRepository
import br.com.cinemagazine.service.UserService
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
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

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun login(data: LoginRequestDTO): LoginDTO {
        val user = userRepository.findByEmail(data.email)
        if (user.isEmpty) {
            logger.error("UserServiceImpl.login - {} - Email: [{}]", USER_NOT_FOUND.description, data.email)
            throw BusinessException(NOT_FOUND, USER_NOT_FOUND)
        }
        val matchPassword = passwordEncoder.matches(data.password, user.get().password)
        if (!matchPassword) {
            logger.error("UserServiceImpl.login - Password not matching - Email: [{}]", data.email)
            throw BusinessException(NOT_FOUND, USER_NOT_FOUND)
        }
        return LoginDTO("token")
    }

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
        val user = userRepository.findById(id).orElseThrow {
            logger.error("UserServiceImpl.getUser - {} - id: [{}]", USER_NOT_FOUND.description, id)
            throw BusinessException(NOT_FOUND, USER_NOT_FOUND)
        }
        return UserDTO(user.id, user.firstName, user.lastName, user.email, user.gender)
    }

    override fun updateUser(id: String, data: UpdateUserRequestDTO): UserDTO {
        val user = userRepository.findById(id).orElseThrow {
            logger.error("UserServiceImpl.updateUser - {} - id: [{}]", USER_NOT_FOUND.description, id)
            throw BusinessException(NOT_FOUND, USER_NOT_FOUND)
        }
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

    override fun updatePassword(id: String, data: UpdatePasswordRequestDTO): UserDTO {
        val user = userRepository.findById(id).orElseThrow {
            logger.error("UserServiceImpl.updatePassword - {} - id: [{}]", USER_NOT_FOUND.description, id)
            throw BusinessException(NOT_FOUND, USER_NOT_FOUND)
        }
        val userUpdated = UserDocument(
            user.id,
            user.firstName,
            user.lastName,
            user.email,
            passwordEncoder.encode(data.password),
            user.gender,
            user.createdDate,
            LocalDateTime.now()
        )
        userRepository.save(userUpdated)
        return UserDTO(userUpdated.id, userUpdated.firstName, userUpdated.lastName, userUpdated.email, userUpdated.gender)
    }

    override fun deleteUser(id: String) {
        val user = userRepository.findById(id).orElseThrow {
            logger.error("UserServiceImpl.updatePassword - {} - id: [{}]", USER_NOT_FOUND.description, id)
            throw BusinessException(NOT_FOUND, USER_NOT_FOUND)
        }
        userRepository.delete(user)
    }

    private fun validateEmail(email: String) {
        val existEmail = userRepository.existsByEmail(email)
        if (existEmail) {
            logger.error("UserServiceImpl.validateEmail - {} - Email: [{}]", EMAIL_ALREADY_EXISTS.description, email)
            throw BusinessException(CONFLICT, EMAIL_ALREADY_EXISTS)
        }
    }
}