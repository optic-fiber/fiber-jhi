package com.cheroliv.fiber.domain;

import com.cheroliv.fiber.domain.enumeration.ContractEnum;
import com.cheroliv.fiber.domain.enumeration.TypeInterEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Inter.
 */
@Entity
@Table(name = "inter", indexes = {
    @Index(name = "`uniq_idx_nd_type`", columnList = "`nd`,`type_inter`", unique = true),
    @Index(name = "`idx_inter_type`", columnList = "`type_inter`"),
    @Index(name = "`idx_inter_contract`", columnList = "`contract`"),
    @Index(name = "`idx_inter_date_time_inter`", columnList = "`date_time_inter`"),
    @Index(name = "`idx_inter_first_name_client`", columnList = "`first_name_client`"),
    @Index(name = "`idx_inter_last_name_client`", columnList = "`last_name_client`"),
    @Index(name = "`idx_inter_complex`", columnList = "`complex`")
})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Inter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 10, max = 10)
    @Column(name = "nd", length = 10, nullable = false)
    private String nd;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_inter", nullable = false)
    private TypeInterEnum typeInter;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "contract", nullable = false)
    private ContractEnum contract;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "last_name_client", length = 100, nullable = false)
    private String lastNameClient;

    @Size(max = 100)
    @Column(name = "first_name_client", length = 100)
    private String firstNameClient;

    @NotNull
    @Column(name = "date_time_inter", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mmZ")
//        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime dateTimeInter;

    @Column(name = "complex")
    private Boolean complex;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("inters")
    private Planning planning;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public Inter nd(String nd) {
        this.nd = nd;
        return this;
    }

    public TypeInterEnum getTypeInter() {
        return typeInter;
    }

    public void setTypeInter(TypeInterEnum typeInter) {
        this.typeInter = typeInter;
    }

    public Inter typeInter(TypeInterEnum typeInter) {
        this.typeInter = typeInter;
        return this;
    }

    public ContractEnum getContract() {
        return contract;
    }

    public void setContract(ContractEnum contract) {
        this.contract = contract;
    }

    public Inter contract(ContractEnum contract) {
        this.contract = contract;
        return this;
    }

    public String getLastNameClient() {
        return lastNameClient;
    }

    public void setLastNameClient(String lastNameClient) {
        this.lastNameClient = lastNameClient;
    }

    public Inter lastNameClient(String lastNameClient) {
        this.lastNameClient = lastNameClient;
        return this;
    }

    public String getFirstNameClient() {
        return firstNameClient;
    }

    public void setFirstNameClient(String firstNameClient) {
        this.firstNameClient = firstNameClient;
    }

    public Inter firstNameClient(String firstNameClient) {
        this.firstNameClient = firstNameClient;
        return this;
    }

    public ZonedDateTime getDateTimeInter() {
        return dateTimeInter;
    }

    public void setDateTimeInter(ZonedDateTime dateTimeInter) {
        this.dateTimeInter = dateTimeInter;
    }

    public Inter dateTimeInter(ZonedDateTime dateTimeInter) {
        this.dateTimeInter = dateTimeInter;
        return this;
    }

    public Boolean isComplex() {
        return complex;
    }

    public Inter complex(Boolean complex) {
        this.complex = complex;
        return this;
    }

    public void setComplex(Boolean complex) {
        this.complex = complex;
    }

    public Planning getPlanning() {
        return planning;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    public Inter planning(Planning planning) {
        this.planning = planning;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inter)) {
            return false;
        }
        return id != null && id.equals(((Inter) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Inter{" +
            "id=" + getId() +
            ", nd='" + getNd() + "'" +
            ", typeInter='" + getTypeInter() + "'" +
            ", contract='" + getContract() + "'" +
            ", lastNameClient='" + getLastNameClient() + "'" +
            ", firstNameClient='" + getFirstNameClient() + "'" +
            ", dateTimeInter='" + getDateTimeInter() + "'" +
            ", complex='" + isComplex() + "'" +
            "}";
    }
}
