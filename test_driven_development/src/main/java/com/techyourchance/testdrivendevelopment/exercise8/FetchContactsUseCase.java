package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

public class FetchContactsUseCase {

    private GetContactsHttpEndpoint getContactsHttpEndpoint;
    private List<Listener> listeners = new ArrayList<>();

    public FetchContactsUseCase(GetContactsHttpEndpoint getContactsHttpEndpoint) {
        this.getContactsHttpEndpoint = getContactsHttpEndpoint;
    }

    public void getContacts(String term) {
        getContactsHttpEndpoint.getContacts(term, new GetContactsHttpEndpoint.Callback() {

            @Override
            public void onGetContactsSucceeded(List<ContactSchema> contactSchemas) {
                for (Listener listener : listeners) {
                    listener.onContactsFetched(getFormattedContacts(contactSchemas));
                }
            }

            @Override
            public void onGetContactsFailed(GetContactsHttpEndpoint.FailReason failReason) {
                for (Listener listener: listeners){
                    listener.onFetchContactsFailed();
                }
            }
        });
    }

    private List<Contact> getFormattedContacts(List<ContactSchema> contactSchemas) {
        List<Contact> contacts = new ArrayList<>();
        for (ContactSchema schema : contactSchemas) {
            contacts.add(new Contact(schema.getId(), schema.getFullName(), schema.getImageUrl()));
        }
        return contacts;
    }

    public void registerListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void unregisterListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public interface Listener {
         void onContactsFetched(List<Contact> contacts);

        void onFetchContactsFailed();
    }
}
