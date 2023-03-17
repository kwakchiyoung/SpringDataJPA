package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter

public class Member {

    @Id @GeneratedValue //@GeneratedValue JPA가 알아서 시퀀스 제공
    private Long id;
    private String username;
}
