package com.SENA.DISTRIBUIDORA_LA_DORADA.Service;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.UserCreateDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.User;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IUserService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 🔹 para bcrypt

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        // 🔹 si quieres encriptar siempre la contraseña aquí:
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    // 🔹 update usando DTO
    @Override
    public User update(Long id, UserCreateDto dto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUserName(dto.getUserName());
            user.setEmail(dto.getEmail());

            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }

            user.setUserRole(dto.getUserRole());
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User login(String email, String password) {
        // 🔹 login con contraseña sin encriptar (no recomendado)
        Optional<User> optionalUser = userRepository.findByEmailAndPassword(email, password);
        return optionalUser.orElse(null);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        // 🔹 Aquí iría el envío real de correo
        System.out.println("Email enviado a " + to + " con asunto: " + subject + " | Contenido: " + body);
    }

    @Override
    public String recoverPassword(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            sendEmail(user.getEmail(), "Recuperación de contraseña", "Tu contraseña es: " + user.getPassword());
            return "Correo enviado";
        } else {
            return "Usuario no encontrado";
        }
    }

    @Override
    public User createUser(UserCreateDto dto) {
        User user = new User();
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());

        // 🔹 Encriptar contraseña al crear
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        user.setUserRole(dto.getUserRole());
        return userRepository.save(user);
    }

    @Override
    public User updatePassword(Long id, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(newPassword); // puedes usar passwordEncoder aquí
            return userRepository.save(user);
        }
        return null;
    }

}
