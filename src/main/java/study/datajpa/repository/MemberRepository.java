package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
    @Query(name = "Member.findByUsername") //NamedQuery
    List<Member> findByUsername(@Param("username") String username); //JPQL로 명확한 :떙떙 을 지정해줬을떈 넣어줘야함. 이번에는 NamedQuery를 사용하였기에.
}
