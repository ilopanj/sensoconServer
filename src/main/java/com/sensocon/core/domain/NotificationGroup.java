package com.sensocon.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A NotificationGroup.
 */
@Entity
@Table(name = "notification_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "notificationgroup")
public class NotificationGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "notificationGroup")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SensorDevice> sensorDevices = new HashSet<>();

    @OneToMany(mappedBy = "notificationGroup")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Contact> contacts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<SensorDevice> getSensorDevices() {
        return sensorDevices;
    }

    public NotificationGroup sensorDevices(Set<SensorDevice> sensorDevices) {
        this.sensorDevices = sensorDevices;
        return this;
    }

    public NotificationGroup addSensorDevice(SensorDevice sensorDevice) {
        this.sensorDevices.add(sensorDevice);
        sensorDevice.setNotificationGroup(this);
        return this;
    }

    public NotificationGroup removeSensorDevice(SensorDevice sensorDevice) {
        this.sensorDevices.remove(sensorDevice);
        sensorDevice.setNotificationGroup(null);
        return this;
    }

    public void setSensorDevices(Set<SensorDevice> sensorDevices) {
        this.sensorDevices = sensorDevices;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public NotificationGroup contacts(Set<Contact> contacts) {
        this.contacts = contacts;
        return this;
    }

    public NotificationGroup addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setNotificationGroup(this);
        return this;
    }

    public NotificationGroup removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.setNotificationGroup(null);
        return this;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotificationGroup notificationGroup = (NotificationGroup) o;
        if (notificationGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificationGroup{" +
            "id=" + getId() +
            "}";
    }
}
