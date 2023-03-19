package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","username","age"})
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
public class Member {

    @Id @GeneratedValue //@GeneratedValue JPA가 알아서 시퀀스 제공
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;


    @ManyToOne(fetch = FetchType.LAZY) //ManyToOne관계는 //실무에서 JPA에서 모든 연관관계는 지연로딩으로 셋팅을 해줘야 한다. //Defalt인 즉시로 되어있으면 성능 최적화하기 힘들다.
    @JoinColumn(name = "team_id") //FK 컬럼명이 된다.
    private Team team;


    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team ) {
        this.username = username;
        this.age = age;
        if(team !=null){
            chageTeam(team);
        }

    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public void chageTeam(Team team) {
        this.team = team;
        team.getMembers().add(this); //팀에도 가서 맴버를 넣어줘야함.
    }

}
