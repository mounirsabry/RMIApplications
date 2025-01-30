package io.tasks.classes;

import io.tasks.api.ClientAPI;
import java.sql.Timestamp;
import java.time.Instant;

public class LoggedUser {
    private final String name;
    private final ClientAPI registeredObject;
    private Timestamp lastHeartBeat;
;    
    public LoggedUser(String name, ClientAPI registeredObject) {
        this.name = name;
        this.registeredObject = registeredObject;
        lastHeartBeat = Timestamp.from(Instant.now());
    }

    public String getName() {
        return name;
    }

    public ClientAPI getRegisteredObject() {
        return registeredObject;
    }
    
    public Timestamp getLastHeartBeat() {
        return lastHeartBeat;
    }
    
    public void refreshLastHeartBeat() {
        lastHeartBeat = Timestamp.from(Instant.now());
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(LoggedUser.class.getName());
        builder.append('{');
        
        builder.append("name=");
        builder.append(name);
        
        builder.append(", registeredObject=");
        builder.append(registeredObject);
        
        builder.append(", lastHeartBeat=");
        builder.append(lastHeartBeat);
        
        builder.append('}');
        return builder.toString();
    }
}
