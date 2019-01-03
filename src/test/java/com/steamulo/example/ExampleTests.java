package com.steamulo.example;

import com.steamulo.ApiTests;
import com.steamulo.services.user.UserService;
import com.steamulo.utils.MailUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Exemple de test unitaire avec des mocks
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ExampleTests extends ApiTests {

    @Mock
    private List<String> mockedList;

    @Spy
    private List<String> spiedList = new ArrayList<String>();

    @InjectMocks
    private UserService userService; // @InjectMock permet d'injecter des instances de @Spy et de @Mock

    @Test
    public void useMockAnnotationTest() {
        mockedList.add("one");
        Mockito.verify(mockedList).add("one");
        assertEquals(0, mockedList.size());

        Mockito.when(mockedList.size()).thenReturn(100);
        assertEquals(100, mockedList.size());
    }

    @Test
    public void useSpyAnnotationTest() {
        spiedList.add("one");
        spiedList.add("two");

        Mockito.verify(spiedList).add("one");
        Mockito.verify(spiedList).add("two");

        assertEquals(2, spiedList.size());

        Mockito.doReturn(100).when(spiedList).size();
        assertEquals(100, spiedList.size());
    }

    @Test
    public void checkValidEmailShouldFail() {
        assertFalse(MailUtils.checkValidEmail("test@test"));
    }
}
