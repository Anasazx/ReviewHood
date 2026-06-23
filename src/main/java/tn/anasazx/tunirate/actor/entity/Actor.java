package tn.anasazx.tunirate.actor.entity;

import jakarta.persistence.*;

import lombok.Getter;

import lombok.NoArgsConstructor;

import lombok.Setter;

import tn.anasazx.tunirate.enums.ActorType;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "actors")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "actor_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "actor_type", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private ActorType type;
}