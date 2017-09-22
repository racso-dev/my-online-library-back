package com.steamulo;

import com.steamulo.persistence.entity.User;
import com.steamulo.persistence.repository.UserRepository;
import org.junit.Before;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by tchekroun on 10/07/2017.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTests {

    @Mock
    @Autowired
    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    @Before
    public void setUp() {
        //Activer les annotations mock
        MockitoAnnotations.initMocks(this);
        createUserReturnMock();
    }

    public void createUserReturnMock() {
        Mockito.doReturn(new User()).when(userRepository).save((User) Matchers.anyObject());
    }

}
