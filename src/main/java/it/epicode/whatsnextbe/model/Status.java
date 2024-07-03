package it.epicode.whatsnextbe.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "status")
@ToString(exclude = "tasks")
public class Status extends BaseEntity{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private StatusType status;

    @OneToMany(
            cascade = CascadeType.PERSIST,
            mappedBy = "status",
            fetch = FetchType.EAGER
    )
    @JsonIgnore
    private List<Task> tasks;

    public Status(StatusType status) {
        this.status = status;
    }

    public static class StatusDeserializer extends com.fasterxml.jackson.databind.JsonDeserializer<Status> {
        @Override
        public Status deserialize(com.fasterxml.jackson.core.JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws java.io.IOException {
            String statusValue = p.getText();
            StatusType statusType = StatusType.fromString(statusValue);
            return new Status(statusType);
        }
    }

    public enum StatusType {
        COMPLETATO,
        NON_ACCETTATO,
        IN_CORSO;

        @JsonCreator
        public static StatusType fromString(String key) {
            for (StatusType type : StatusType.values()) {
                if (type.name().equalsIgnoreCase(key)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown status: " + key);
        }
    }
}
