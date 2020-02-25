package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint.Callback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class FetchContactsUseCaseTest {

    private static final String TERM = "term";
    private static final String ID = "id";
    private static final String FULL_NAME = "full-name";
    private static final String PHONE_NUMBER = "phone-number";
    private static final String IMAGE_URL = "image_url";
    private static final double AGE = 22.0;

    @Mock
    GetContactsHttpEndpoint getContactsHttpEndpointMock;
    @Mock
    FetchContactsUseCase.Listener listenerMock1;
    @Mock
    FetchContactsUseCase.Listener listenerMock2;

    @Captor
    ArgumentCaptor<List<Contact>> acListContacts;

    FetchContactsUseCase SUI;

    @Before
    public void setUp() {
        SUI = new FetchContactsUseCase(getContactsHttpEndpointMock);
        success();
    }

    // filter term is passed
    @Test
    public void fetchContact_correctTermPassed() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUI.getContacts(TERM);
        //Assert
        verify(getContactsHttpEndpointMock).getContacts(ac.capture(), any(Callback.class));
        assertThat(ac.getValue(), is(TERM));
    }

    // success - observers notified with data
    @Test
    public void fetchContact_success_observersNotified() {
        // Arrange
        // Act
        SUI.registerListener(listenerMock1);
        SUI.registerListener(listenerMock2);
        SUI.getContacts(TERM);
        // Assert
        verify(listenerMock1).onContactsFetched(acListContacts.capture());
        verify(listenerMock2).onContactsFetched(acListContacts.capture());
        List<List<Contact>> captures = acListContacts.getAllValues();
        assertThat(captures.get(0), is(getContact()));
        assertThat(captures.get(1), is(getContact()));
    }


    // success - unsubscribed observer not notified

    @Test
    public void fetchContact_success_unsubscribedObserverNotNotified() {
        // Arrange
        // Act
        SUI.registerListener(listenerMock1);
        SUI.registerListener(listenerMock2);
        SUI.unregisterListener(listenerMock2);
        SUI.getContacts(TERM);
        // Assert
        verify(listenerMock1).onContactsFetched(any(List.class));
        verifyNoMoreInteractions(listenerMock2);
    }

    // fail - observers notified with failure


    @Test
    public void fetchContact_fail_observersNotifiedWithFailure() {
        // Arrange
        generalError();
        // Act
        SUI.registerListener(listenerMock1);
        SUI.registerListener(listenerMock2);
        SUI.getContacts(TERM);
        // Assert
        verify(listenerMock1).onFetchContactsFailed();
        verify(listenerMock2).onFetchContactsFailed();
    }

    private void success() {
        doAnswer(new Answer() {
                     @Override
                     public Object answer(InvocationOnMock invocation) throws Throwable {
                         Object[] args = invocation.getArguments();
                         Callback callback = (Callback) args[1];
                         callback.onGetContactsSucceeded(getContactSchemas());
                         return null;
                     }
                 }
        ).when(getContactsHttpEndpointMock)
                .getContacts(any(String.class), any(Callback.class));
    }

    private void generalError() {
        doAnswer(new Answer() {
                     @Override
                     public Object answer(InvocationOnMock invocation) throws Throwable {
                         Object[] args = invocation.getArguments();
                         Callback callback = (Callback) args[1];
                         callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.GENERAL_ERROR);
                         return null;
                     }
                 }
        ).when(getContactsHttpEndpointMock)
                .getContacts(any(String.class), any(Callback.class));
    }

    private List<ContactSchema> getContactSchemas() {
        List contacts = new ArrayList();
        contacts.add(new ContactSchema(ID, FULL_NAME, PHONE_NUMBER, IMAGE_URL, AGE));
        return contacts;
    }

    private List<Contact> getContact() {
        List contacts = new ArrayList();
        contacts.add(new Contact(ID, FULL_NAME, IMAGE_URL));
        return contacts;
    }

}