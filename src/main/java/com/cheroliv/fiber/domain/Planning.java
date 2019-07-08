package com.cheroliv.fiber.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Planning.
 */
@Entity
@Table(name = "planning", indexes = {
    @Index(name = "`uniq_idx_user_open`", columnList = "`user_id`,`open`", unique = true),
    @Index(name = "`idx_planning_initial_tech`", columnList = "`initial_tech`"),
    @Index(name = "`idx_planning_open`", columnList = "`open`"),
    @Index(name = "`idx_planning_date_time_creation`", columnList = "`date_time_creation`"),
    @Index(name = "`idx_planning_last_name_tech`", columnList = "`last_name_tech`"),
    @Index(name = "`idx_planning_first_name_tech`", columnList = "`first_name_tech`")})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Planning implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "initial_tech", nullable = false)
    private String initialTech;

    @NotNull
    @Column(name = "open", nullable = false)
    private Boolean open;

    @NotNull
    @Column(name = "date_time_creation", nullable = false)
    private ZonedDateTime dateTimeCreation;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "last_name_tech", length = 100, nullable = false)
    private String lastNameTech;

    @Size(max = 100)
    @Column(name = "first_name_tech", length = 100)
    private String firstNameTech;

    @OneToMany(mappedBy = "planning")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Inter> inters = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("plannings")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInitialTech() {
        return initialTech;
    }

    public Planning initialTech(String initialTech) {
        this.initialTech = initialTech;
        return this;
    }

    public void setInitialTech(String initialTech) {
        this.initialTech = initialTech;
    }

    public Boolean isOpen() {
        return open;
    }

    public Planning open(Boolean open) {
        this.open = open;
        return this;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public ZonedDateTime getDateTimeCreation() {
        return dateTimeCreation;
    }

    public Planning dateTimeCreation(ZonedDateTime dateTimeCreation) {
        this.dateTimeCreation = dateTimeCreation;
        return this;
    }

    public void setDateTimeCreation(ZonedDateTime dateTimeCreation) {
        this.dateTimeCreation = dateTimeCreation;
    }

    public String getLastNameTech() {
        return lastNameTech;
    }

    public Planning lastNameTech(String lastNameTech) {
        this.lastNameTech = lastNameTech;
        return this;
    }

    public void setLastNameTech(String lastNameTech) {
        this.lastNameTech = lastNameTech;
    }

    public String getFirstNameTech() {
        return firstNameTech;
    }

    public Planning firstNameTech(String firstNameTech) {
        this.firstNameTech = firstNameTech;
        return this;
    }

    public void setFirstNameTech(String firstNameTech) {
        this.firstNameTech = firstNameTech;
    }

    public Set<Inter> getInters() {
        return inters;
    }

    public Planning inters(Set<Inter> inters) {
        this.inters = inters;
        return this;
    }

    public Planning addInters(Inter inter) {
        this.inters.add(inter);
        inter.setPlanning(this);
        return this;
    }

    public Planning removeInters(Inter inter) {
        this.inters.remove(inter);
        inter.setPlanning(null);
        return this;
    }

    public void setInters(Set<Inter> inters) {
        this.inters = inters;
    }

    public User getUser() {
        return user;
    }

    public Planning user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Planning)) {
            return false;
        }
        return id != null && id.equals(((Planning) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Planning{" +
            "id=" + getId() +
            ", initialTech='" + getInitialTech() + "'" +
            ", open='" + isOpen() + "'" +
            ", dateTimeCreation='" + getDateTimeCreation() + "'" +
            ", lastNameTech='" + getLastNameTech() + "'" +
            ", firstNameTech='" + getFirstNameTech() + "'" +
            "}";
    }
}
