package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","name"})
public class Team {
    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;


    //자신이 Member의 team에 매핑되어 있으므로 team으로 설정해준 것 입니다.
    //연관관계의 주인을 설정할 때 주인을 따로 설정하는 것이 아니고 자신이 이 연관관계의 주인이 아님을 설정해줘야 합니다.
    @OneToMany(mappedBy = "team") //FK가 없는쪽에 mappedBy를 써주는것이 좋다.
    private List<Member> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
