package br.com.cinemagazine.services.impl

import br.com.cinemagazine.constants.ApiMessage.EMAIL_ALREADY_EXISTS
import br.com.cinemagazine.constants.ApiMessage.USER_NOT_FOUND
import br.com.cinemagazine.constants.Gender
import br.com.cinemagazine.documents.RefreshTokenDocument
import br.com.cinemagazine.documents.UserDocument
import br.com.cinemagazine.dto.user.CreateUserRequestDTO
import br.com.cinemagazine.dto.user.LoginDTO
import br.com.cinemagazine.dto.user.LoginRequestDTO
import br.com.cinemagazine.dto.user.UpdatePasswordRequestDTO
import br.com.cinemagazine.dto.user.UpdateUserRequestDTO
import br.com.cinemagazine.dto.user.UserDTO
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.repository.RefreshTokenRepository
import br.com.cinemagazine.repository.UserRepository
import br.com.cinemagazine.services.TokenService
import br.com.cinemagazine.services.UserService
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserServiceImpl(
    private val tokenService: TokenService,
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder
): UserService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun login(data: LoginRequestDTO, agent: String): LoginDTO {
        val user = userRepository.findByEmail(data.email!!)
        if (user.isEmpty) {
            logger.error("UserServiceImpl.login - {} - Email: [{}]", USER_NOT_FOUND.description, data.email)
            throw BusinessException(NOT_FOUND, USER_NOT_FOUND)
        }
        val matchPassword = passwordEncoder.matches(data.password, user.get().password)
        if (!matchPassword) {
            logger.error("UserServiceImpl.login - Password not matching - Email: [{}]", data.email)
            throw BusinessException(NOT_FOUND, USER_NOT_FOUND)
        }
        val userLogged = UserDTO(user.get().id, user.get().firstName, user.get().lastName, user.get().email, user.get().gender)
        val accessToken = tokenService.generateAccessToken(userLogged)
        val refreshToken = tokenService.generateRefreshToken(userLogged)
        val refreshTokenDocument = RefreshTokenDocument(ObjectId().toString(), refreshToken, agent, LocalDateTime.now())
        refreshTokenRepository.save(refreshTokenDocument)
        return LoginDTO(accessToken, refreshToken)
    }

    override fun createUser(data: CreateUserRequestDTO): UserDTO {
        validateEmail(data.email!!)
        val password = passwordEncoder.encode(data.password)
        val user = UserDocument(
            ObjectId().toString(),
            data.firstName!!,
            data.lastName!!,
            data.email,
            password,
            Gender.valueOf(data.gender!!.uppercase()),
            LocalDateTime.now(),
            LocalDateTime.now()
        )
        val userSaved = userRepository.save(user)
        logger.info("UserServiceImpl.createUser - Successful Operation - id: [{}]", userSaved.id)
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
            validateEmail(data.email!!)
        }
        val userUpdated = userRepository.save(UserDocument(
            user.id,
            data.firstName!!,
            data.lastName!!,
            data.email,
            user.password,
            Gender.valueOf(data.gender!!.uppercase()),
            user.createdDate,
            LocalDateTime.now()
        ))
        logger.info("UserServiceImpl.updateUser - Successful Operation - id: [{}]", id)
        return UserDTO(userUpdated.id, userUpdated.firstName, userUpdated.lastName, userUpdated.email, userUpdated.gender)
    }

    override fun updatePassword(id: String, data: UpdatePasswordRequestDTO): UserDTO {
        val user = userRepository.findById(id).orElseThrow {
            logger.error("UserServiceImpl.updatePassword - {} - id: [{}]", USER_NOT_FOUND.description, id)
            throw BusinessException(NOT_FOUND, USER_NOT_FOUND)
        }
        val password = passwordEncoder.encode(data.password)
        val userUpdated = UserDocument(
            user.id,
            user.firstName,
            user.lastName,
            user.email,
            password,
            user.gender,
            user.createdDate,
            LocalDateTime.now()
        )
        userRepository.save(userUpdated)
        logger.info("UserServiceImpl.updatePassword - Successful Operation - id: [{}]", id)
        return UserDTO(userUpdated.id, userUpdated.firstName, userUpdated.lastName, userUpdated.email, userUpdated.gender)
    }

    override fun deleteUser(id: String) {
        val user = userRepository.findById(id).orElseThrow {
            logger.error("UserServiceImpl.updatePassword - {} - id: [{}]", USER_NOT_FOUND.description, id)
            throw BusinessException(NOT_FOUND, USER_NOT_FOUND)
        }
        logger.info("UserServiceImpl.deleteUser - Successful Operation - id: [{}]", id)
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