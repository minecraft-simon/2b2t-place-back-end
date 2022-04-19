package org.theancients.placebackend.bot;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Bot {

    @Id
    private String username;
    private int status;

    @Override
    public String toString() {
        return "Bot{" +
                "username='" + username + '\'' +
                ", status=" + status +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
