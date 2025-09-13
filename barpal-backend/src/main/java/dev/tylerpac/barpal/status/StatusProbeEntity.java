package dev.tylerpac.barpal.status;

import jakarta.persistence.*;

@Entity
@Table(name = "status_probe")
public class StatusProbeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "note", length = 64)
    private String note;

    public StatusProbeEntity() {}
    public StatusProbeEntity(String note) { this.note = note; }

    public Long getId() { return id; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
