package com.SENA.DISTRIBUIDORA_LA_DORADA.IService;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.UserCreateDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> findAll();

    List<User> getAll();

    Optional<User> findById(Long id);

    User save(User user);

    // 🔹 update con DTO
    User update(Long id, UserCreateDto dto);

    void delete(Long id);

    User login(String email, String password);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    void sendEmail(String to, String subject, String body);

    String recoverPassword(String email);

    User createUser(UserCreateDto dto);

    User updatePassword(Long id, String newPassword);

}
