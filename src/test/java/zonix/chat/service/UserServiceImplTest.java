package zonix.chat.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import zonix.chat.dto.UserDto;
import zonix.chat.entity.Role;
import zonix.chat.entity.User;
import zonix.chat.repository.RoleRepository;
import zonix.chat.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void saveUser() {

        UserDto userDto = new UserDto(
                1000l,
                "Dima",
                "ml@gb.com",
                "123123"
        );
        Role role = roleRepository.findByName("ROLE_ADMIN");

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        user.setRoles(Arrays.asList(role));
        userRepository.save(user);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    void findUserByEmail() {
        // Arrange
        String userEmail = "ml@gb.com";
        User expectedUser = new User();
        expectedUser.setName("Dima");
        expectedUser.setEmail(userEmail);

        // Mock UserRepository to return the expected user when findByEmail is called
        when(userRepository.findByEmail(userEmail)).thenReturn(expectedUser);

        // Act
        User result = userService.findUserByEmail(userEmail);

        // Assert
        assertNotNull(result);
        String actualUserEmail = result.getEmail();
        assertThat(actualUserEmail).isEqualTo(userEmail);

        // Optionally, you can verify that the findByEmail method was called with the correct argument
        verify(userRepository).findByEmail(userEmail);
    }
}